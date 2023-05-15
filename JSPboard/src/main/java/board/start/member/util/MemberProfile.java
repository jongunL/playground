package board.start.member.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;
import board.start.util.Auth;

@WebServlet({"/member/profile", "/member/alarm", "/member/board", "/member/comment"})
public class MemberProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> auth = Auth.getAuth(req);
		String memberSeq = null;
		
		if(auth == null) {
			resp.sendRedirect("/login");
		}
		
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}
		
		if(memberSeq != null) {
			MemberDAO memberDAO = new MemberDAO();
			MemberDTO memberDTO = memberDAO.getMemberProfileBySeq(memberSeq);
			req.setAttribute("memberProfile", memberDTO);
		}
		
		req.setAttribute("path", req.getRequestURI());
		
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_setting.jsp");
		requestDispatcher.forward(req, resp);
	}
	
}

