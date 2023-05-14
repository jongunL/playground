package board.start.member.account;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;
import board.start.util.Auth;

@WebServlet("/member/getProfile")
public class GetMemberProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		Map<String, String> auth = Auth.getAuth(req);
		String memberSeq = null;
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}
		
		MemberDTO memberDTO = null;
		if(memberSeq != null) {
			memberDTO = new MemberDTO();
			MemberDAO memberDAO = new MemberDAO();
			memberDTO = memberDAO.getMemberProfileBySeq(memberSeq);
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().print(new Gson().toJson(memberDTO));
	}
	
}
