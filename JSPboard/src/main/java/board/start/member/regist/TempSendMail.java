package board.start.member.regist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.start.util.CreateCode;

@WebServlet("/temp")
public class TempSendMail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		String code = CreateCode.getCode();
		session.setAttribute("emailAuthCode", code);
		
		System.out.println("===발급 코드===");
		System.out.println(session.getAttribute("emailAuthCode"));
		
		//데이터전송
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		writer.print(300);
		writer.close();

	}
	
}

