package board.start.board.comment.hanlder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import board.start.util.Auth;

@WebServlet("/board/comment")
public class BoardComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HashMap<String, String> auth = (HashMap<String, String>)Auth.getAuth(req);
		String boardTitleSeq = req.getParameter("boardTitleSeq");
		String boardSeq = req.getParameter("boardSeq");
		String memberSeq = null;

		//comment 댓글 가공을 위해 로그인 여부확인
		if(auth != null) {
			String active = auth.get("active");
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				memberSeq = auth.get("seq");
			}
		}
		
		//댓글 정렬을 위한 변수
		String commentSortType = null;
		//댓글 페이징 처리를 위한 변수
		int nowPage = 0;	//현재 페이지
		int begin = 0;		//페이지 시작
		int end = 0;		//페이지 끝
		int pageSize = 50;	//한 페이지당 출력할 댓글 수 default = 10
		int totalCount = 0;	//총 댓글 수
		int totalPage = 0;	//총 페이지 수
		//댓글 정렬 옵션 - 최신순(newest), 등록일순(registrationDate)
		if(req.getParameter("sort") != null && req.getParameter("sort") != "") {
			commentSortType = req.getParameter("sort");
		} else {
			commentSortType = "registrationDate";
		}
				
		//현재 유저가 보려고 하는 페이지
		if(req.getParameter("page") != null && req.getParameter("page") != "") {
			nowPage = Integer.parseInt(req.getParameter("page"));
		} else {
			nowPage = 1;
		}
		
		//화면에 출력할 댓글범위
		begin = ((nowPage-1)*pageSize) + 1;
		end = begin + pageSize - 1;
		
		//페이징, 검색옵션 세팅
		Map<String, String> commentOption = new HashMap<>();
		commentOption.put("boardSeq", boardSeq);
		commentOption.put("category", boardTitleSeq);
		commentOption.put("begin", String.valueOf(begin));
		commentOption.put("end", String.valueOf(end));
		commentOption.put("sort", commentSortType);
		
		
		//가져온 데이터가 있다면 댓글을 가져온다.
		ArrayList<BoardCommentDTO> commentList = null;
		BoardCommentDAO boardCommentDAO = new BoardCommentDAO();
		if(boardTitleSeq != null && boardSeq != null) {
			commentList = boardCommentDAO.getCommentList(commentOption);
		}

		//가져온 댓글이 존재한다면 데이터 후처리를 한다.
		if(commentList != null) {
			for(BoardCommentDTO dto : commentList) {
				dto.setBoardComment(dto.getBoardComment().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
				if(memberSeq != null && dto.getMemberSeq().equals(memberSeq)) dto.setAuthorCk(true);
				else dto.setAuthorCk(false);
			}
			//화면에 표시할 페이징 상태
			if(commentList.size() > 0 && boardSeq != null) {
				totalCount = Integer.parseInt(boardCommentDAO.getCommentCountByBoardSeq(boardSeq));
				totalPage = (int)Math.ceil((double)(totalCount)/pageSize);
			}
		}

		//가져온 댓글을 전송한다.
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("commentList", new Gson().toJsonTree(commentList));
		jsonObject.put("pageSize", pageSize);
		jsonObject.put("totalPage", totalPage);
		jsonObject.put("nowPage", nowPage);
		jsonObject.put("sort", commentSortType);
		jsonObject.put("totalCount", totalCount);
		PrintWriter writer = resp.getWriter();
		writer.write(jsonObject.toString());
	}

}
