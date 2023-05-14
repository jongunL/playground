package board.start.member.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import board.start.member.MemberAlarmDAO;
import board.start.member.MemberAlarmDTO;
import board.start.util.Auth;

@WebServlet("/member/getAlarm")
public class GetAlarm extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Map<String, String> auth = Auth.getAuth(req);
		String path = req.getParameter("path");
		String memberSeq = null;
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		
		List<MemberAlarmDTO> alarmList = null;
		if(path != null && path.equals("/member/alarm")) {
			alarmList = getAlarmPaging(req, memberSeq, jsonObject);
		} else {
			alarmList = getAlarmNoPaging(memberSeq);
		}

		jsonObject.put("alarmList", new Gson().toJsonTree(alarmList));
		PrintWriter writer = resp.getWriter();
		writer.write(jsonObject.toString());
	}
	
	private List<MemberAlarmDTO> getAlarmNoPaging(String memberSeq) {
		List<MemberAlarmDTO> result = null;
		Map<String, String> option = new HashMap<>();
		option.put("memberSeq", memberSeq);
		
		if(memberSeq != null) {
			MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
			result = memberAlarmDAO.getAlarmList(option);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<MemberAlarmDTO> getAlarmPaging(HttpServletRequest req, String memberSeq, JSONObject jsonObject) {
		List<MemberAlarmDTO> result = null;
		//댓글 페이징 처리를 위한 변수
		int nowPage = 0;	//현재 페이지
		int begin = 0;		//페이지 시작
		int end = 0;		//페이지 끝
		int pageSize = 10;	//한 페이지당 출력할 댓글 수 default = 10
		int totalCount = 0;	//총 알람 수
		int totalPage = 0;	//총 페이지 수
		
		//현재 유저가 보려고 하는 페이지
		if(req.getParameter("page") != null && req.getParameter("page") != "") {
			nowPage = Integer.parseInt(req.getParameter("page"));
		} else {
			nowPage = 1;
		}
		
		//화면에 출력할 댓글범위
		begin = ((nowPage-1)*pageSize) + 1;
		end = begin + pageSize - 1;
		
		Map<String, String> option = new HashMap<>();
		option.put("memberSeq", memberSeq);
		option.put("begin", String.valueOf(begin));
		option.put("end", String.valueOf(end));
		
		MemberAlarmDAO memberAlarmDAO = new MemberAlarmDAO();
		if(memberSeq != null) {
			result = memberAlarmDAO.getAlarmList(option);
		}
		
		if(result.size() > 0 && memberSeq != null) {
			totalCount = memberAlarmDAO.getAlarmCount(memberSeq);
			totalPage = (int)Math.ceil((double)(totalCount)/pageSize);
		}
		jsonObject.put("nowPage", nowPage);
		jsonObject.put("pageSize", pageSize);
		jsonObject.put("totalPage", totalPage);
		jsonObject.put("totalCount", totalCount);
		
		return result;
	}	
}

