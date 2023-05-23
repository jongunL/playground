package board.start.member.account;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import board.start.member.MemberDAO;
import board.start.util.SHA256;
import board.start.util.VerifyEmail;


//TODO sendMail과 합쳐보기
@WebServlet("/member/findAccountOk")
public class MemberFindAccountOk extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		boolean result = false;
		String email = req.getParameter("email");
		MemberDAO memberDAO = new MemberDAO();
		String id = memberDAO.findMemberIdByEmail(SHA256.getHash(email));

		if(id != null) {
			VerifyEmail.sendMail(email, id);
			result = true;
		}

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().print(new Gson().toJson(result));
	}
	
}
