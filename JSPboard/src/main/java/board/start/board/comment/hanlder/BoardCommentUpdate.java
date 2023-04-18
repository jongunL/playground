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
import board.start.util.Auth;

@WebServlet("/board/comment/update")
public class BoardCommentUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		boolean result = false;
		String memberSeq = null;
		String commentSeq = req.getParameter("replyNo");
		String comment = req.getParameter("comment");
		Map<String, String> auth = Auth.getAuth(req);
		if(auth != null) {
			String active = auth.get("active");			
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				memberSeq = auth.get("seq");
			}
		}

		if(commentSeq != null && comment != null && memberSeq != null) {
			if(commentLengthCk(comment)) {
				BoardCommentDAO boardCommentDAO = new BoardCommentDAO();
				BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
				boardCommentDTO.setBoardCommentSeq(commentSeq);
				boardCommentDTO.setBoardComment(comment);
				boardCommentDTO.setMemberSeq(memberSeq);
				result = boardCommentDAO.updateComment(boardCommentDTO);
			}
		}

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().write(new Gson().toJson(result));
	}
	
	//1~300 글자 사이라면 등록 가능
	//TODO 중복되는데 따로 빼서 묶을지 고민해보기
	private boolean commentLengthCk(String comment) {
		boolean result = false;
		if(!(comment.length() < 1 || comment.length() > 300)) result = true;
		return result;
	}
}
