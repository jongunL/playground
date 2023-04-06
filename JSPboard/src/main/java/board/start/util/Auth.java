package board.start.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;

public class Auth {

	public static Map<String, String> getAuth(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String auth = (String)session.getAttribute("auth");
		HashMap<String, String> memberStatus = null;
		
		//세션에 인증이 없는경우
		if(auth == null) {
			Cookie[] cookies = req.getCookies();
			for(Cookie c : cookies) {
				if(c.getName().equals("auth")) {
					auth = c.getValue();
					MemberDAO dao = new MemberDAO();
					MemberDTO member = dao.getIdByEmail(auth);
					if(member != null) {
						memberStatus = new HashMap<>();
						session.setAttribute("auth", auth);
						session.setAttribute("seq", member.getSeq());
						session.setAttribute("active", member.getActive());
						session.setAttribute("grade", member.getGrade());
						memberStatus = setAuth(session);
					}
					
				}
			}
		//세션에 인증이 있는경우
		} else if (auth != null) {
			memberStatus = setAuth(session);
		}
		
		return memberStatus;
	}
	
	private static HashMap<String, String> setAuth(HttpSession session) {
		HashMap<String, String> memberStatus = null;
		
		if(session.getAttribute("auth") != null) {
			memberStatus = new HashMap<>();
			memberStatus.put("auth", (String)session.getAttribute("auth"));
			memberStatus.put("seq", (String)session.getAttribute("seq"));
			memberStatus.put("active", (String)session.getAttribute("active"));
			memberStatus.put("grade", (String)session.getAttribute("grade"));
		}
		return memberStatus;
	}
	
	public static boolean CheckAuth(HttpServletRequest req) {
		boolean result = false;
		HashMap<String, String> memberStatus = (HashMap<String, String>) getAuth(req);
		if(memberStatus != null && memberStatus.get("active").equals("y")) {
			result = true;
		}
		return result;
	}
	
}
