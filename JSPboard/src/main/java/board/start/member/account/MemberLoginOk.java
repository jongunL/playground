package board.start.member.account;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;
import board.start.util.SHA256;

@WebServlet("/loginOk")
public class MemberLoginOk extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		MemberDTO result = null;
		
		//form으로부터 넘어온 데이터
		String id = req.getParameter("id");
		String pwd = req.getParameter("pwd");
		//remember, auto 두가지옵션
		String auto = req.getParameter("login_status_auto");
		String remember = req.getParameter("login_status_remember");
		
		//DB작업
		if(id != null && pwd != null) {
			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setId(id);
			memberDTO.setPwd(SHA256.getHash(pwd));
			
			MemberDAO memberDAO = new MemberDAO();
			result = memberDAO.signIn(memberDTO);
			
			//아이디, 비밀번호 재확인이 성공되고 활성화된 계정일 경우
			if(result != null && result.getActive().equals("y")) {
				//로그인 성공시 세션에도 저장
				String auth = result.getEmail();
				HttpSession session = req.getSession();
				session.setAttribute("auth", auth);
				session.setAttribute("seq", result.getSeq());
				session.setAttribute("active", result.getActive());
				session.setAttribute("grade", result.getGrade());
				session.setMaxInactiveInterval(24*60*60);
				
				//로그인 옵션이 존재하는 경우는 쿠키에 정보저장
				//옵션에 따라서 쿠키에 저장한다.
				String auto_login = "false";
				String remember_id = "false";
				ArrayList<Cookie> cookies = new ArrayList<>();

				//자동로그인일 경우
				if(auto != null && auto.equals("true")) {
					cookies.add(new Cookie("auth", auth));
					auto_login = auto;
				} 
				
				//아이디를 기억할 경우
				if(remember != null && remember.equals("true")) {
					cookies.add(new Cookie("id", result.getId()));
					remember_id = remember;
				//기억하지 않는경우
				} else {
					//id쿠키를 삭제하고 remember 옵션 false로 설정
					for(Cookie c : req.getCookies()) {
						if(c.getName().equals("id")) {
							c.setMaxAge(0);
							resp.addCookie(c);
						}
					}
				}
				
				//옵션 체크
				cookies.add(new Cookie("auto", auto_login));
				cookies.add(new Cookie("remember", remember_id));
				
				for(Cookie c : cookies) {
					//30일동안 쿠키유지
					c.setMaxAge(30*24*60*60);
					resp.addCookie(c);
				}
				
				//로그인 성공여부 - 활성화시 성공
				req.setAttribute("success", true);
				req.setAttribute("active", result.getActive());
			} else if(result != null && result.getActive().equals("n")) {
				req.setAttribute("success", true);
				req.setAttribute("active", result.getActive());
				req.setAttribute("code", result.getEmail());
			}
		}
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_loginOk.jsp");
		dispatcher.forward(req, resp);
		
	}

}