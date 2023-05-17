package board.start.member.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;
import board.start.util.Auth;
import board.start.util.SHA256;

@WebServlet("/loginCheck")
public class MemberLoginCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//데이터 받고
		req.setCharacterEncoding("UTF-8");
		String id = req.getParameter("id");
		String pwd = req.getParameter("pwd");
		boolean success = false;
		
		if(id != null && pwd != null) {
			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setId(id);
			memberDTO.setPwd(SHA256.getHash(pwd));
			
			MemberDAO memberDAO = new MemberDAO();
			if( memberDAO.signIn(memberDTO) != null) {
				success = true;
			}
		}
		
		//결과 전송
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.printf("{ \"success\": %b }", success);
		writer.close();
		
	}

}