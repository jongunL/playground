package board.start.board.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.util.VisitCategory;

@WebServlet("/removeVisitCategory")
public class RemoveVisitCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VisitCategory.removeVisitCategory(req, resp, req.getParameter("category"));
	}
	
}
