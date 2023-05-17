package board.start.board.comment.hanlder;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import board.start.board.comment.BoardCommentDAO;
import board.start.board.comment.BoardCommentDTO;
import board.start.member.MemberAlarmDTO;
import board.start.member.MemberAlarmDAO;
import board.start.util.Auth;

@WebServlet("/board/comment/delete")
public class BoardCommentDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		boolean result = false;
		String boardCommentSeq = req.getParameter("replyNo");
		
		//권한을 가져오고
		String memberSeq = null;
		Map<String, String> auth = Auth.getAuth(req);
		if(auth != null) {
			String active = auth.get("active");			
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				memberSeq = auth.get("seq");
			}
		}
		
		System.out.println(memberSeq);
		System.out.println(boardCommentSeq);
		
		//댓글 삭제
		if(memberSeq != null && boardCommentSeq != null) {
			BoardCommentDAO boardCommentDAO = null;
			BoardCommentDTO boardCommentDTO = null;
			boardCommentDAO = new BoardCommentDAO();
			boardCommentDTO = new BoardCommentDTO();
			boardCommentDTO.setMemberSeq(memberSeq);
			boardCommentDTO.setBoardCommentSeq(boardCommentSeq);
			result = boardCommentDAO.deleteComment(boardCommentDTO);
			System.out.println(result);
		}
		
		//댓글을 삭제했으면, 보낸 알람도 함께 삭제한다.
		if(result == true) {
			result = false;
			MemberAlarmDAO memberAlramDAO = new MemberAlarmDAO();
			result = memberAlramDAO.deleteMemberAlramByCommentSeq(memberSeq);
		}

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().write(new Gson().toJson(result));
	}
	
}
