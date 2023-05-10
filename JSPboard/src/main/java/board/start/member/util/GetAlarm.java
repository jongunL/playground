package board.start.member.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import board.start.member.MemberAlarmDAO;
import board.start.member.MemberAlarmDTO;
import board.start.util.Auth;

@WebServlet("/member/getAlarm")
public class GetAlarm extends HttpServlet {

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
		
		List<MemberAlarmDTO> alarmList = null;
		if(memberSeq != null) {
			MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
			alarmList = memberAlarmDAO.getAlarmList(memberSeq);
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		resp.getWriter().print(gson.toJson(alarmList));
	}
	
}
