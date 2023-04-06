package board.start.member.regist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/compareCode")
public class CompareCode extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		//세션에 저장되어있는 이메일코드 가져오기
		HttpSession session = req.getSession();
		String sendCode = (String)session.getAttribute("emailAuthCode");
		String inputCode = req.getParameter("code");
		boolean success = false;
		//입력된 값과 동일성 체크
		if(sendCode != null && inputCode != null) {
			success = sendCode.equals(inputCode);
		}	
		//결과값 전송	
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.printf("{ \"success\": %b }", success);
		writer.close();
	}
}
