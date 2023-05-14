package board.start.member.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import board.start.member.MemberDAO;
import board.start.util.Auth;

@WebServlet("/member/change/profile")
public class MemberChangeProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		req.setCharacterEncoding("UTF-8");
		boolean result = false;
		String memberSeq = null;
		Map<String, String> auth = Auth.getAuth(req);
		
		if(auth == null) {
			resp.sendRedirect("/login");
		}
		
		if(auth != null) {
			String active = auth.get("active");
			if(active != null && active.equals("y")) {
				memberSeq = auth.get("seq");
			}
		}
		
		boolean nicknameChage = false;
		boolean imgChange = false;
		String nickname = null;
		String filename = null;
		
		if(memberSeq != null) {
			
			//변경되는 데이터 가져오기
			try {
				String path = req.getSession().getServletContext().getRealPath("/asset/images/profile");
				int size = 1024 * 1024 * 100;
				MultipartRequest multi = new MultipartRequest(req, path, size, "UTF-8", new DefaultFileRenamePolicy());
				
				if(multi.getParameter("nickname_change").equals("true")) {
					nicknameChage = true;
					nickname = multi.getParameter("nickname");
				}
				
				if(multi.getParameter("img_change").equals("true")) {
					imgChange = true;
					filename = multi.getFilesystemName("profile_img");
				}		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if((nicknameChage && nicknameValidationCk(nickname)) || imgChange) {
				Map<String, String> option = new HashMap<>();
				option.put("memberSeq", memberSeq);
				option.put("nicknameChage", String.valueOf(nicknameChage));
				option.put("nickname", nickname);
				option.put("imgChange", String.valueOf(imgChange));
				option.put("filename", filename);
				
				MemberDAO memberDAO = new MemberDAO();
				result = memberDAO.updateMemberProfile(option);
			}			
		}

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().print(new Gson().toJson(result));
	}
	
	private boolean nicknameValidationCk(String nickname) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ]{1,10}$");
		Matcher matcher = pattern.matcher(nickname);		
		return matcher.find();
	}
	
}
