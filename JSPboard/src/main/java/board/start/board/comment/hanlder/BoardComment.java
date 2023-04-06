package board.start.board.comment.hanlder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;

import board.start.board.BoardDAO;
import board.start.board.comment.BoardCommentDAO;
import board.start.board.comment.BoardCommentDTO;
import board.start.util.Auth;

@WebServlet("/board/comment")
public class BoardComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HashMap<String, String> auth = (HashMap<String, String>)Auth.getAuth(req);
		String boardTitleSeq = req.getParameter("boardTitleSeq");
		String boardSeq = req.getParameter("boardSeq");
		String memberSeq = null;
		ArrayList<BoardCommentDTO> comment = null;
		
		//comment 댓글 가공을 위해 로그인 여부확인
		if(auth != null) {
			String active = auth.get("active");
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				memberSeq = auth.get("seq");
			}
		}
		//가져온 데이터가 있다면 댓글을 가져온다.
		if(boardTitleSeq != null && boardSeq != null && memberSeq != null) {
			BoardCommentDAO boardCommentDAO = new BoardCommentDAO();
			BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
			boardCommentDTO.setBoardSeq(boardSeq);
			boardCommentDTO.setBoardTitleSeq(boardTitleSeq);
			comment = boardCommentDAO.getCommentListByboardSeq(boardCommentDTO);
		}
		
		//가져온 댓글이 존재한다면 데이터 후처리를 한다.
		if(comment != null) {
			for(BoardCommentDTO dto : comment) {
				dto.setBoardComment(dto.getBoardComment().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
				if(memberSeq != null && dto.getMemberSeq().equals(memberSeq)) dto.setAuthorCk(true);
				else dto.setAuthorCk(false);
			}
		}
		
		//가져온 댓글을 전송한다.
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		resp.getWriter().write(gson.toJson(comment));
	}
	
}