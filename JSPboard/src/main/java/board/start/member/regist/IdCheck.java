package board.start.member.regist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.member.MemberDAO;
import board.start.member.MemberDTO;

@WebServlet("/idCheck")
public class IdCheck extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//받은 데이터
		req.setCharacterEncoding("UTF-8");
		String id = req.getParameter("id");
		
		//DB작업
		MemberDTO dto = new MemberDTO();
		dto.setId(id);
		
		MemberDAO dao = new MemberDAO();
		int result = dao.duplicationCheckById(dto);
		
		//데이터전송
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		writer.print(result);
		writer.close();
		
	}
}
