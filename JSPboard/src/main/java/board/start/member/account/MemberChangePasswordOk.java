package board.start.member.account;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

@WebServlet("/member/changePasswordOk")
public class MemberChangePasswordOk extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		boolean result = false;
		String memberSeq = null;
		String id = null;
		String pwd = req.getParameter("pwd");
		String changePwd = req.getParameter("change_pwd");
		String changePwdCk = req.getParameter("change_pwd_ck");
		
		Map<String, String> auth = Auth.getAuth(req);
		MemberDAO memberDAO = new MemberDAO();
		MemberDTO memberDTO = new MemberDTO();
		
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
			id = memberDAO.getMemberIdBySeq(memberSeq);
		}
		
		if(id != null && pwd != null) {
			memberDTO.setId(id);
			memberDTO.setPwd(SHA256.getHash(pwd));
			memberDTO = memberDAO.signIn(memberDTO);
			
			if(checkPwd(pwd, changePwd, changePwdCk) && memberDTO != null) {
				memberDTO.setPwd(SHA256.getHash(changePwd));
				result = memberDAO.updateMemberPassword(memberDTO);
			}
		}
		
		req.setAttribute("result", result);
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_change_passwordOk.jsp");
		dispatcher.forward(req, resp);
	}
	
	private boolean checkPwd(String password, String changePassword, String changePasswordCheck) {
		boolean result = false;
		
		if(password != null && changePassword != null && changePasswordCheck != null) {
			if((!password.equals(changePassword) && changePassword.equals(changePasswordCheck))) {
				Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!@#$%^&*()_+|<>?]{6,16}$");
				Matcher matcher = pattern.matcher(changePassword);				
				result = matcher.find();				
			} else {
				result = false;
			}
		}
		return result;
	}
	
}
