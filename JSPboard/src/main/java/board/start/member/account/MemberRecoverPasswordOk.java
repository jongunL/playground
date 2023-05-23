package board.start.member.account;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;
import board.start.util.SHA256;

@WebServlet("/member/recoverPwOk")
public class MemberRecoverPasswordOk extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String code = req.getParameter("code");
		String pwd1 = req.getParameter("pwd1");
		String pwd2 = req.getParameter("pwd2");
		boolean result = false;
		
		MemberDAO memberDAO = new MemberDAO();
		MemberDTO memberDTO = null;
		memberDTO = memberDAO.getIdByEmail(code);
		
		if(memberDTO != null && passwordCheck(pwd1, pwd2)) {
			memberDTO.setPwd(SHA256.getHash(pwd1));
			result = memberDAO.updateMemberPassword(memberDTO);
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().print(new Gson().toJson(result));
	}
	
	private boolean passwordCheck(String pwd1, String pwd2) {
		boolean result = false;
		
		if((pwd1 != null || pwd2 != null) && pwd1.equals(pwd2)) {
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!@#$%^&*()_+|<>?]{6,16}$");
			Matcher matcher = pattern.matcher(pwd1);				
			result = matcher.find();
		}
		
		return result;
	}
}
