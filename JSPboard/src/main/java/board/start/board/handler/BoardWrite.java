package board.start.board.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.board.BoardDAO;
import board.start.board.BoardSubTitleDTO;
import board.start.board.BoardTitleDTO;
import board.start.util.Auth;

@WebServlet("/board/write")
public class BoardWrite extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		if(!Auth.CheckAuth(req)) {
			resp.sendRedirect("/login");
			return;	
		}
		
		BoardDAO boardDAO = new BoardDAO();
		String category = req.getParameter("num");
		
		if(category != null && category.equals("1")) {
			resp.sendRedirect("/board/lists?num=1");
			return;
		}

		//board Title 가져오기
		BoardTitleDTO boardTitleDTO = new BoardTitleDTO();
		boardTitleDTO.setBoardTitle(category);
		boardTitleDTO =  boardDAO.getBoardTitle(boardTitleDTO);
		
		//구독유무 확인하기
		HashMap<String, String> auth = (HashMap<String, String>)Auth.getAuth(req);
		if(auth != null) {
			String active = auth.get("active");
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				int result = boardDAO.checkSubScribe(category, auth.get("seq"));
				//구독 여부 추가하기
				if(result > 0) req.setAttribute("subscribe", true);
				else req.setAttribute("subscribe", false);
			}	
		}
		
		//게시판 소분류 가져오기
		BoardSubTitleDTO boardSubTitleDTO = new BoardSubTitleDTO();
		boardSubTitleDTO.setBoardTitleSeq(category);
		List<BoardSubTitleDTO> boardSubTitleList = boardDAO.getBoardSubTitle(boardSubTitleDTO);
		req.setAttribute("category", category);
		req.setAttribute("boardTitle", boardTitleDTO);
		req.setAttribute("boardSubTitleList", boardSubTitleList);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/board/board_write.jsp");
		dispatcher.forward(req, resp);
	}

}
