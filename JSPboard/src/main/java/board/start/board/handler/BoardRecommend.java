package board.start.board.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import board.start.board.BoardDAO;
import board.start.board.BoardRecommendDTO;
import board.start.util.Auth;

@WebServlet("/board/recommend")
public class BoardRecommend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> auth = Auth.getAuth(req);
		//already - 이미 추천한경우, success - 값 변경 성공여부, auth - 로그인 여부
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		String memberSeq = null;
		String memberActive = null;
		String boardSeq = req.getParameter("board_seq");
		String recommend = req.getParameter("recommend");
		
		//실행도 하지 못한경우
		result.put("already", false);
		result.put("success", false);
		result.put("auth", false);
		
		//로그인이 되어있는 상태
		if(auth != null) {
			memberSeq = auth.get("seq");
			memberActive = auth.get("active");
		}
		//조건에 부합하면 실행
		if((memberActive != null && memberActive.equals("y")) && memberSeq != null && boardSeq != null && (recommend != null)) {
			BoardDAO boardDAO = new BoardDAO();
			BoardRecommendDTO boardRecommendDTO = new BoardRecommendDTO();
			boardRecommendDTO.setBoardSeq(boardSeq);
			boardRecommendDTO.setMemberSeq(memberSeq);
			boardRecommendDTO.setRecommend(Integer.parseInt(recommend));
			result = boardDAO.updateRecommend(boardRecommendDTO);
		}
		
		JSONObject success = new JSONObject();
		success.put("result", result);
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().write(success.toString());
	}
	
}
