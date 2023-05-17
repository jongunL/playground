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
import board.start.util.SHA256;

@WebServlet({"/member/profile", "/member/alarm", "/member/board", "/member/comment", "/member/account"})
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
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String path = req.getRequestURI();
		boolean result = false;

		if(path.equals("/member/account")) {
			String id = null;
			String pwd = req.getParameter("pwd");
			
			Map<String, String> auth = Auth.getAuth(req);
			String memberSeq = null;
			if(auth != null) {
				String active = auth.get("active");
				if(active != null && active.equals("y")) {
					memberSeq = auth.get("seq");
				}
			}
			
			if(memberSeq != null) {
				MemberDAO memberDAO = new MemberDAO();
				id = memberDAO.getMemberIdBySeq(memberSeq);
			}
			
			if(id != null && pwd != null) {
				MemberDTO memberDTO = new MemberDTO();
				memberDTO.setId(id);
				memberDTO.setPwd(SHA256.getHash(pwd));
				
				MemberDAO memberDAO = new MemberDAO();
				if( memberDAO.signIn(memberDTO) != null) {
					result = true;
				}
			}
		}
		
		req.setAttribute("path", path);
		req.setAttribute("result", result);
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_setting.jsp");
		requestDispatcher.forward(req, resp);
	}
	
}

