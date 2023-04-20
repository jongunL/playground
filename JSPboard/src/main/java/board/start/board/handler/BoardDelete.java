package board.start.board.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.board.BoardDAO;
import board.start.board.BoardDTO;
import board.start.util.Auth;

@WebServlet("/board/delete")
public class BoardDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		boolean result = true;
		String active = null;
		String memberSeq = null;
		String boardSeq = req.getParameter("board");
		String boardTitleSeq = req.getParameter("num");
		Map<String, String> auth = Auth.getAuth(req);
		
		if(auth != null) {
			active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}

		if(boardSeq != null && memberSeq != null && (active != null && active.equals("y"))) {
			BoardDAO boardDAO = new BoardDAO();
			BoardDTO boardDTO = new BoardDTO();
			boardDTO.setBoardSeq(boardSeq);
			boardDTO.setMemberActive(active);
			boardDTO.setMemberSeq(memberSeq);
			result = boardDAO.deleteBoard(boardDTO);
		}
		
		req.setAttribute("title", boardTitleSeq);
		req.setAttribute("result", result);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/board/board_delete.jsp");
		dispatcher.forward(req, resp);
	}
	
}
