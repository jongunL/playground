package board.start.member.account;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.member.MemberDAO;

@WebServlet("/login")
public class MemberLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Cookie[] cookies = req.getCookies();
		//쿠키가 있다면
		if(cookies != null) {
			//auth와 remember가 있는지 확인
			String id = null;
			String remember = null;
			for(Cookie c : cookies) {
				if(c.getName().equals("id")) id = c.getValue();
				if(c.getName().equals("remember")) remember = c.getValue();
			}
			//만약 기억옵션이 true이고 아이디가 있다면
			if(id != null && (remember != null && remember.equals("true"))) {
				req.setAttribute("id", id);
			}
		}
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_login.jsp");
		dispatcher.forward(req, resp);
		
	}

}
