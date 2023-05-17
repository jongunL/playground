package board.start.member.util;

import java.io.IOException;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import board.start.board.BoardDAO;
import board.start.board.BoardDTO;
import board.start.board.comment.BoardCommentDAO;
import board.start.board.comment.BoardCommentDTO;
import board.start.member.MemberAlarmDAO;
import board.start.member.MemberAlarmDTO;
import board.start.util.Auth;

@WebServlet({"/member/checkAlarm", "/member/checkAllAlarm", "/member/deleteBoard", "/member/deleteComment"})
public class DeleteContents extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Map<String, String> auth = Auth.getAuth(req);
		String memberSeq = null;
		String[] seq = req.getParameterValues("seq[]");
		
		boolean result = false;
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}
		
		if(memberSeq != null) {
			String uri = req.getRequestURI();
			if(uri.equals("/member/checkAlarm")) {
				result = checkAlarm(memberSeq, seq);
			} else if(uri.equals("/member/checkAllAlarm")) {
				result = checkAllAlarm(memberSeq);
			} else if(uri.equals("/member/deleteBoard")) {
				result = removeBoard(memberSeq, seq);
			} else if(uri.equals("/member/deleteComment")) {
				result = removeComment(memberSeq, seq);
			} else {
				result = false;
			}
		}
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		resp.getWriter().print(gson.toJson(result));
	}
	
	private boolean removeComment(String memberSeq, String[] seq) {
		boolean result = false;
		BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
		BoardCommentDAO boardCommentDAO = new BoardCommentDAO();
		boardCommentDTO.setMemberSeq(memberSeq);
		boardCommentDTO.setBoardCommentSeq(String.join(",", seq));
		result = boardCommentDAO.deleteComment(boardCommentDTO);
		
		//댓글을 삭제했으면, 보낸 알람도 함께 삭제한다.
		if(result == true) {
			result = false;
			MemberAlarmDAO memberAlramDAO = new MemberAlarmDAO();
			result = memberAlramDAO.deleteMemberAlramByCommentSeq(String.join(",", seq));
		}
		
		return result;
	}

	private boolean removeBoard(String memberSeq, String[] seq) {
		boolean result = false;
		BoardDTO boardDTO = new BoardDTO();
		BoardDAO boardDAO = new BoardDAO();
		boardDTO.setMemberSeq(memberSeq);
		boardDTO.setBoardSeq(String.join(",", seq));
		result = boardDAO.deleteBoard(boardDTO);
		
		//게시판을 삭제했으면, 해당 게시판을 통해 보내진 알람을 모두 삭제
		if(result = true) {
			result = false;
			MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
			result = memberAlarmDAO.deleteMemberAlramByBoardSeq(String.join(",", seq));
		}
		
		return result;
	}

	private boolean checkAllAlarm(String memberSeq) {
		MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
		MemberAlarmDTO memberAlarmDTO = new MemberAlarmDTO();
		memberAlarmDTO.setMemberReceiverSeq(memberSeq);		
		return memberAlarmDAO.checkAllAlarm(memberAlarmDTO);
	}
	
	private boolean checkAlarm(String memberSeq, String[] seq) {
		if(seq != null) {
			MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
			MemberAlarmDTO memberAlarmDTO = new MemberAlarmDTO();
			memberAlarmDTO.setMemberAlarmSeq(String.join(",", seq));
			memberAlarmDTO.setMemberReceiverSeq(memberSeq);
			return memberAlarmDAO.checkAlarm(memberAlarmDTO);	
		} else {
			return false;
		}
	}
}
