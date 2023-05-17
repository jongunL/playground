package board.start.board.comment.hanlder;

import java.util.Map;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import com.google.gson.Gson;
import board.start.board.comment.BoardCommentDAO;
import board.start.board.comment.BoardCommentDTO;
import board.start.member.MemberAlarmDTO;
import board.start.member.MemberAlarmDAO;
import board.start.member.MemberDAO;
import board.start.util.Auth;

@WebServlet("/board/comment/add")
public class BoardCommentAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		BoardCommentDAO boardCommentDAO = new BoardCommentDAO();
		//데이터를 가져오고
		String comment = req.getParameter("comment");
		String commentGroupSeq = req.getParameter("rootNo");
		String commentGroupOrderSeq = req.getParameter("orderNo");
		String boardFormData = req.getParameter("boardFormData");
		String nickname = req.getParameter("nickname");
		JSONObject jsonObject = (JSONObject)JSONValue.parse(boardFormData);
		String boardSeq = (String)jsonObject.get("board_num");
		String boardTitleSeq = (String)jsonObject.get("board_title_num");
		String boardAuthorSeq = (String)jsonObject.get("board_author_num");
		
		//로그인된 사용자인지 확인한다.
		String memberSeq = null;
		Map<String, String> auth = Auth.getAuth(req);
		if(auth != null) {
			String active = auth.get("active");
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				memberSeq = auth.get("seq");				
			}
		}

		//업로드 정보와, 로그인된 사용자이며, 글작성이 가능하다면 댓글을 등록한다.
		String commentSeq = null;
		BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
		if(boardSeq != null && memberSeq != null && boardAuthorSeq != null && commentLengthCk(comment)) {
			boardCommentDTO.setBoardComment(comment);
			boardCommentDTO.setBoardSeq(boardSeq);
			boardCommentDTO.setBoardTitleSeq(boardTitleSeq);
			boardCommentDTO.setBoardAuthSeq(boardAuthorSeq);
			boardCommentDTO.setMemberSeq(memberSeq);
			boardCommentDTO.setBoardCommentGroupSeq(commentGroupSeq);
			boardCommentDTO.setBoardCommentGroupOrderSeq(commentGroupOrderSeq);
			commentSeq = boardCommentDAO.saveBoardComment(boardCommentDTO);
		}
		
		//댓글 등록에 성공한 경우, 동적 태그 추가를 위한 쿼리실행
		BoardCommentDTO commentResult = null;
		if(commentSeq != null && boardTitleSeq != null) {
			boardCommentDTO.setBoardCommentSeq(commentSeq);
			boardCommentDTO.setBoardTitleSeq(boardTitleSeq);
			commentResult = boardCommentDAO.getComment(boardCommentDTO);
		}
		
		//동적 태그 생성에 필요한 데이터 가공
		if(commentResult != null) {
			if(memberSeq != null && memberSeq.equals(commentResult.getMemberSeq())) commentResult.setAuthorCk(true);
			else commentResult.setAuthorCk(false);
			commentResult.setBoardComment(commentResult.getBoardComment().replaceAll("<", "&lt").replaceAll(">", "&gt"));
		}
		
		//알람 전송하기
		if(commentResult != null) {
			String memberReceiverSeq = null;
			if(commentGroupSeq != null && commentGroupOrderSeq != null && nickname != "") {
				MemberDAO memberDAO = new MemberDAO();
				memberReceiverSeq = memberDAO.getMemberSeqByNickname(nickname);
			} else {
				memberReceiverSeq = boardAuthorSeq;
			}
			
			//자기 자신이 아닌경우에 발송
			if(!memberSeq.equals(memberReceiverSeq)) {
				MemberAlarmDAO memberAlramDAO = new MemberAlarmDAO();
				MemberAlarmDTO memberAlarmDTO = new MemberAlarmDTO();
				memberAlarmDTO.setBoardSeq(boardSeq);
				memberAlarmDTO.setMemberReceiverSeq(memberReceiverSeq);
				memberAlarmDTO.setMemberSenderSeq(memberSeq);
				memberAlarmDTO.setCommentSeq(commentSeq);
				memberAlarmDTO.setMessage(commentAlarmLengthCk(commentResult.getBoardComment()));
				memberAlramDAO.sendMemberAlarm(memberAlarmDTO);
			}
		}
		
		//코멘트 전송
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().write(new Gson().toJson(commentResult));
	}
	
	//1~300 글자 사이라면 등록 가능
	private boolean commentLengthCk(String comment) {
		boolean result = false;
		if(!(comment.length() < 1 || comment.length() > 300)) result = true;
		return result;
	}
	
	//알람은 최대 88글자
	private String commentAlarmLengthCk(String comment) {
		if(comment.length() > 88) return comment.substring(0, 87);
		else return comment;
	}
}

