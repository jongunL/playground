package board.start.board.comment.hanlder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import board.start.board.comment.BoardCommentDAO;
import board.start.board.comment.BoardCommentDTO;
import board.start.board.comment.BoardCommentRecommendDTO;
import board.start.util.Auth;

@WebServlet("/board/comment/recommend")
public class BoardCommentRecommend extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> auth = Auth.getAuth(req);
		//already - 이미 추천한경우, success - 값 변경 성공여부, auth - 로그인 여부
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		String memberSeq = null;
		String memberActive = null;
		String commentSeq = req.getParameter("comment_no");
		String recommend = req.getParameter("recommend");
		
		//실행도 하지 못한경우
		result.put("already", false);
		result.put("success", false);
		result.put("auth", false);
		
		//로그인이 되어있는 상태
		if(auth != null) {
			memberActive = auth.get("active");
			if(memberActive != null && memberActive.equals("y")) {
				memberSeq = auth.get("seq");
				result.put("auth", true);			
			}
		}
		
		//조건에 부합하면 실행
		if((memberActive != null && memberActive.equals("y")) && memberSeq != null && commentSeq != null && (recommend != null)) {
			BoardCommentDAO boardCommentDAO = new BoardCommentDAO();
			BoardCommentRecommendDTO boardCommentRecommendDTO = new BoardCommentRecommendDTO();
			boardCommentRecommendDTO.setCommentSeq(commentSeq);
			boardCommentRecommendDTO.setMemberSeq(memberSeq);
			boardCommentRecommendDTO.setRecommend(Integer.parseInt(recommend));
			result = boardCommentDAO.updateRecommend(boardCommentRecommendDTO);
		}
	
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().write(new Gson().toJson(result));
	}
}
