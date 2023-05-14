package board.start.member.regist;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;
import board.start.util.SHA256;


@WebServlet("/registerOk")
public class MemberRegisterOk extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Pattern pattern;
	private Matcher matcher;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		int result = -1;
		
		String id = req.getParameter("id");
		String pwd1 = req.getParameter("pwd1");
		String pwd2 = req.getParameter("pwd2");
		String nickname = req.getParameter("nickname");
		String email = req.getParameter("email");
		//입력된값이 null이 아닌경우, 유효성검사를 한 뒤 DB insert
		if(id != null || pwd1 != null || pwd2 != null || nickname != null || email != null) {
			if(!idValidationCk(id)) return;
			if(!(pwd1.equals(pwd2)) && !pwdValidationCk(pwd1)) return;
			if(!nicknameValidationCk(nickname)) return;
			if(!emailValidationCk(email)) return;

			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setId(id);
			memberDTO.setPwd(SHA256.getHash(pwd1));
			memberDTO.setNickname(nickname);
			memberDTO.setEmail(SHA256.getHash(email));
			
			MemberDAO memberDAO = new MemberDAO();
			result = memberDAO.signUp(memberDTO);
		}
		//세션 코드 삭제
		HttpSession session = req.getSession();
		session.removeAttribute("emailAuthCode");
		
		//결과값반환
		req.setAttribute("result", result);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/member/member_registerOk.jsp");
		dispatcher.forward(req, resp);
		
	}
	
	private boolean idValidationCk(String id) {
		pattern = Pattern.compile("^[a-zA-Z0-9]{4,16}$");
		matcher = pattern.matcher(id);		
		return matcher.find();
	}
	private boolean pwdValidationCk(String pwd) {
		pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ!@#$%^&*()_+|<>?]{6,16}$");
		matcher = pattern.matcher(pwd);		
		return matcher.find();
	}
	private boolean nicknameValidationCk(String nickname) {
		pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{1,10}$");
		matcher = pattern.matcher(nickname);		
		return matcher.find();
	}
	private boolean emailValidationCk(String email) {
		pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		matcher = pattern.matcher(email);		
		return matcher.find();
	}

}
