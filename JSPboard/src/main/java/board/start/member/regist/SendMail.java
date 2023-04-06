package board.start.member.regist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.start.util.VerifyEmail;

@WebServlet("/sendMail")
public class SendMail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//받은 데이터
		HttpSession session = req.getSession();
		req.setCharacterEncoding("UTF-8");
		String email = req.getParameter("email");
		String code = VerifyEmail.sendMail(email);
		int result = -1;
		
		if(code != null) {
			//5분 유지
			int timer = 5 * 60;
			session.setAttribute("emailAuthCode", code);
			session.setMaxInactiveInterval(timer);
			result = timer;
		}
	
		//데이터전송
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		writer.print(result);
		writer.close();

	}
}
