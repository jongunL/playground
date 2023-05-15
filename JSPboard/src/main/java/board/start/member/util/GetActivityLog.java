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
import board.start.board.BoardDAO;
import board.start.board.BoardDTO;
import board.start.board.comment.BoardCommentDAO;
import board.start.board.comment.BoardCommentDTO;
import board.start.util.Auth;

@WebServlet({"/member/getBoard", "/member/getComment"})
public class GetActivityLog extends HttpServlet {
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
		
		//작성글 페이징 처리를 위한 변수
		int nowPage = 0;	//현재 페이지
		int begin = 0;		//페이지 시작
		int end = 0;		//페이지 끝
		int pageSize = 10;	//한 페이지당 출력할 댓글 수 default = 10
		int blockSize = 5;	//페이징 최대 블럭수
		
		//현재 유저가 보려고 하는 페이지
		if(req.getParameter("page") != null && req.getParameter("page") != "") {
			nowPage = Integer.parseInt(req.getParameter("page"));
		} else {
			nowPage = 1;
		}
		
		//화면에 출력할 게시판 페이지 범위
		begin = ((nowPage-1)*pageSize) + 1;
		end = begin + pageSize - 1;
		
		Map<String, String> option = new HashMap<>();
		option.put("memberSeq", memberSeq);
		option.put("begin", String.valueOf(begin));
		option.put("end", String.valueOf(end));
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		
		if(path.equals("/member/board")) {
			getMemberAuthorBoard(option, jsonObject, pageSize);
		} else if(path.equals("/member/comment")) {
			getMemberAuthorComment(option, jsonObject, pageSize);
		}
		
		jsonObject.put("nowPage", nowPage);
		jsonObject.put("pageSize", pageSize);
		jsonObject.put("blockSize", blockSize);
		PrintWriter writer = resp.getWriter();
		writer.write(jsonObject.toString());
	}

	@SuppressWarnings("unchecked")
	private void getMemberAuthorBoard(Map<String, String> option, JSONObject jsonObject, int pageSize) {
		BoardDAO boardDAO = new BoardDAO();
		List<BoardDTO> boardList = null;
		int totalCount = 0;	//총 알람 수
		int totalPage = 0;	//총 페이지 수
		
		if(option.get("memberSeq") != null) {
			boardList = boardDAO.getMemberAuthorBoardList(option);			
		}
		
		if(boardList.size() > 0 && option.get("memberSeq") != null) {
			totalCount = boardDAO.getMemberAuthorBoardCount(option.get("memberSeq"));
			totalPage = (int)Math.ceil((double)(totalCount)/pageSize);
		}
		
		jsonObject.put("list", new Gson().toJsonTree(boardList));
		jsonObject.put("totalPage", totalPage);
		jsonObject.put("totalCount", totalCount);
	}
	
	@SuppressWarnings("unchecked")
	private void getMemberAuthorComment(Map<String, String> option, JSONObject jsonObject, int pageSize) {
		BoardCommentDAO boardCommentDAO = new BoardCommentDAO();
		List<BoardCommentDTO> commentList = null;
		int totalCount = 0;	//총 알람 수
		int totalPage = 0;	//총 페이지 수
		
		if(option.get("memberSeq") != null) {
			commentList = boardCommentDAO.getMemberAuthorCommentList(option);
		}
		
		if(commentList.size() > 0 && option.get("memberSeq") != null) {
			totalCount = boardCommentDAO.getMemberAuthorCommentCount(option.get("memberSeq"));
			totalPage = (int)Math.ceil((double)(totalCount)/pageSize);
		}
		
		jsonObject.put("list", new Gson().toJsonTree(commentList));
		jsonObject.put("totalPage", totalPage);
		jsonObject.put("totalCount", totalCount);
	}

}
