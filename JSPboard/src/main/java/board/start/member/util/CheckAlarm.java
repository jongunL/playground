package board.start.member.util;

import java.io.IOException;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import board.start.member.MemberAlarmDAO;
import board.start.member.MemberAlarmDTO;
import board.start.util.Auth;

@WebServlet({"/member/checkAlarm", "/member/checkAllAlarm"})
public class CheckAlarm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Map<String, String> auth = Auth.getAuth(req);
		String memberSeq = null;
		String[] notificationSeq = req.getParameterValues("notificationSeq[]");
		
		boolean result = false;
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}

		if(memberSeq != null && req.getRequestURI().equals("/member/checkAlarm")) {
			result = checkAlarm(memberSeq, notificationSeq);
		} else if(memberSeq != null && req.getRequestURI().equals("/member/checkAllAlarm")) {
			result = checkAllAlarm(memberSeq);
		} else {
			result = false;
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		resp.getWriter().print(gson.toJson(result));
	}
	
	private boolean checkAllAlarm(String memberSeq) {
		MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
		MemberAlarmDTO memberAlarmDTO = new MemberAlarmDTO();
		memberAlarmDTO.setMemberReceiverSeq(memberSeq);		
		return memberAlarmDAO.checkAllAlarm(memberAlarmDTO);
	}
	
	private boolean checkAlarm(String memberSeq, String[] notificationSeq) {
		if(notificationSeq != null) {
			MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
			MemberAlarmDTO memberAlarmDTO = new MemberAlarmDTO();
			memberAlarmDTO.setMemberAlarmSeq(String.join(",", notificationSeq));
			memberAlarmDTO.setMemberReceiverSeq(memberSeq);
			return memberAlarmDAO.checkAlarm(memberAlarmDTO);	
		} else {
			return false;
		}
	}
}
