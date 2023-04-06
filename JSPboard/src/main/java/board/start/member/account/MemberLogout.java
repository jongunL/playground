package board.start.member.account;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class MemberLogout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Cookie[] cookies = req.getCookies();
		//쿠키가 있다면 auth와 auto의 value 변경 및 제거
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("auth")) {
					c.setMaxAge(0);
					resp.addCookie(c);
				}
				if(c.getName().equals("auto")) {
					c.setValue("false");
					resp.addCookie(c);
				}
			}
		}
		HttpSession session = req.getSession();
		session.removeAttribute("auth");
		session.removeAttribute("seq");
		session.removeAttribute("active");
		session.removeAttribute("grade");
		
	}

}