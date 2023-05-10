package board.start.board.handler;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import board.start.board.BoardDAO;
import board.start.board.BoardTitleDTO;

@WebServlet("/board/search")
public class BoardSearchChannel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				
		//게시판 목록 가져오기
		BoardDAO boardDAO = new BoardDAO();
		List<BoardTitleDTO> list = boardDAO.getBoardTitleList();
		req.setAttribute("list", list);
		
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/view/board/board_search.jsp");
		requestDispatcher.forward(req, resp);
	}
	
}
