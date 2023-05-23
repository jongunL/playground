package board.start.member.account;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.member.MemberDAO;
import board.start.util.SHA256;

@WebServlet("/member/recovery")
public class MemberRecovery extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {			
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_recovery.jsp");
		dispatcher.forward(req, resp);
	}	
}
