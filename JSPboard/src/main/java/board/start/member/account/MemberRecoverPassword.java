package board.start.member.account;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;

@WebServlet("/member/recoverPw")
public class MemberRecoverPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("code");
		
		boolean result = false;
		MemberDAO memberDAO = new MemberDAO();
		MemberDTO memberDTO = null;
		memberDTO = memberDAO.getIdByEmail(code);
		
		if(memberDTO != null) result = true;
		
		req.setAttribute("result", result);
		req.setAttribute("code", code);
		req.setAttribute("active", memberDTO.getActive());
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_recover_password.jsp");
		dispatcher.forward(req, resp);
	}
}
