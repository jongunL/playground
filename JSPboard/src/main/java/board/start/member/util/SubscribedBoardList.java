package board.start.member.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.board.BoardDAO;
import board.start.board.BoardTitleDTO;
import board.start.util.Auth;

@WebServlet("/board/subscribed")
public class SubscribedBoardList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Map<String, String> auth = Auth.getAuth(req);
		String memberSeq = null;
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}
		
		//게시판 목록 가져오기
		BoardDAO boardDAO = new BoardDAO();
		List<BoardTitleDTO> list = boardDAO.getBoardSubscribedTitleList(memberSeq);
		req.setAttribute("list", list);
		
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/view/board/board_search.jsp");
		requestDispatcher.forward(req, resp);
	}
	
}
