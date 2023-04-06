package board.start.board.comment.hanlder;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import board.start.board.BoardDAO;
import board.start.board.comment.BoardCommentDTO;
import board.start.util.Auth;

@WebServlet("/board/comment/add")
public class BoardCommentAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		BoardDAO boardDAO = new BoardDAO();
		//데이터를 가져오고
		String comment = req.getParameter("comment");
		String boardFormData = req.getParameter("boardFormData");
		JSONObject jsonObject = (JSONObject)JSONValue.parse(boardFormData);
		String boardSeq = (String)jsonObject.get("board_num");
		String boardTitleSeq = (String)jsonObject.get("board_title_num");

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
		if(boardSeq != null && memberSeq != null && commentLengthCk(comment)) {
			boardCommentDTO.setBoardSeq(boardSeq);
			boardCommentDTO.setMemberSeq(memberSeq);
			boardCommentDTO.setBoardComment(comment);
			commentSeq = boardDAO.saveBoardComment(boardCommentDTO);
		}
		
		//댓글 등록에 성공한 경우, 동적 태그 추가를 위한 쿼리실행
		BoardCommentDTO commentResult = null;
		if(commentSeq != null && boardTitleSeq != null) {
			boardCommentDTO.setBoardCommentSeq(commentSeq);
			boardCommentDTO.setBoardTitleSeq(boardTitleSeq);
			commentResult = boardDAO.getComment(boardCommentDTO);
		}
		
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
	
}

