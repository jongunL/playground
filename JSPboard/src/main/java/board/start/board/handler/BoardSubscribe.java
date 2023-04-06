package board.start.board.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.board.BoardDAO;
import board.start.util.Auth;

@WebServlet("/boardSubscribe")
public class BoardSubscribe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean result = false;
		Map<String, String> auth = Auth.getAuth(req);
		if(auth != null) {
			String category = req.getParameter("category");
			String active = auth.get("active");
			
			BoardDAO boardDAO = new BoardDAO();
			//로그인이 되어있고, 활성화된 계정이라면
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				int success = boardDAO.changeSubScribe(category, auth.get("seq"));
				if(success > 0) result = true;
			}
		}

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.printf("{\"success\": %b}", result);
		writer.close();
		
	}
	
}
