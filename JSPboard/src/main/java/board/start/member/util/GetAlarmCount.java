package board.start.member.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import board.start.member.MemberAlarmDAO;
import board.start.util.Auth;

@WebServlet("/member/getAlarmCount")
public class GetAlarmCount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Map<String, String> auth = Auth.getAuth(req);
		String memberSeq = null;
		int result = 0;
		
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}
		
		if(memberSeq != null) {
			MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
			result = memberAlarmDAO.getAlarmCount(memberSeq);
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		resp.getWriter().print(gson.toJson(result));
		
	}
	
}
