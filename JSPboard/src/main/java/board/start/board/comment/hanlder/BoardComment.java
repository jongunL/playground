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
		String boardTitleSeq = req.getParameter("boardTitleSeq");
		String boardSeq = req.getParameter("boardSeq");
		ArrayList<BoardCommentDTO> comment = null;

		//가져온 데이터가 있다면 댓글을 가져온다.
		if(boardTitleSeq != null && boardSeq != null) {
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
			}
		}
		
		//가져온 댓글을 전송한다.
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		resp.getWriter().write(gson.toJson(comment));
	}
	
}