package board.start.board.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.start.board.BoardDAO;
import board.start.board.BoardDTO;
import board.start.board.BoardSubTitleDTO;
import board.start.board.BoardTitleDTO;
import board.start.board.comment.BoardCommentDTO;
import board.start.util.Auth;
import board.start.util.VisitCategory;

@WebServlet(urlPatterns = {"/board/lists", "/board/view"})
public class BoardList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//카테고리
		String category = req.getParameter("num");
		
		//board Title 가져오기
		BoardTitleDTO boardTitleDTO = new BoardTitleDTO();
		boardTitleDTO.setBoardTitle(category);
		
		BoardDAO boardDAO = new BoardDAO();
		boardTitleDTO =  boardDAO.getBoardTitle(boardTitleDTO);
		
		//방문한 카테고리에 추가 및 리스트 가져오기
		String visitCategoryList = null;
		if(category != null && boardTitleDTO.getBoardTitle() != null) {
			VisitCategory.addVisitCategory(req, resp, category, boardTitleDTO.getBoardTitle());
			visitCategoryList = VisitCategory.getVisitCategoryList(req, resp);
		}
		
		//구독유무 확인하기
		String memberSeq = null;
		HashMap<String, String> auth = (HashMap<String, String>)Auth.getAuth(req);
		if(auth != null) {
			String active = auth.get("active");
			if(auth.get("auth") != null && (active != null && active.equals("y"))) {
				memberSeq = auth.get("seq");
				int result = boardDAO.checkSubScribe(category, auth.get("seq"));
				//구독 여부 추가하기
				if(result > 0) req.setAttribute("subscribe", true);
				else req.setAttribute("subscribe", false);
			}	
		}
		
		//만약 /board/view로 들어온경우 게시판 내용도 가져오기
		String requestURI = req.getRequestURI();
		BoardDTO board = null;
		String boardSeq = null;
		if(requestURI.equals("/board/view")) {
			boardSeq = req.getParameter("board");
			if(category != null && boardSeq != null) {
				board = new BoardDTO();
				board.setBoardTitleSeq(category);
				board.setBoardSeq(boardSeq);
				board = boardDAO.getBoard(board);
				//로그인 사용자와, board 작성자 일치여부 확인하기
				if(board.getMemberSeq() != null && memberSeq != null) {
					board.setBoardAuthor(board.getMemberSeq().equals(memberSeq));
				} else {
					board.setBoardAuthor(false);
				}
			}
			System.out.println(board.toString());
		}
		//가져온 board데이터 후처리
		if(board != null) {
			board.setBoardSubject(board.getBoardSubject().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		}
		
		//게시판 소분류 가져오기
		BoardSubTitleDTO boardSubTitleDTO = new BoardSubTitleDTO();
		boardSubTitleDTO.setBoardTitleSeq(category);
		List<BoardSubTitleDTO> boardSubTitleList = boardDAO.getBoardSubTitle(boardSubTitleDTO);
		
		//게시판 리스트 가져오기
		//카테고리 검색옵션
		HashMap<String, String> board_option = new HashMap<>();
		String listType = "all";
		String subTitle = "all";
		//검색여부
		String search = null;
		String keyword = null;
		
		//페이징 처리를 위한 변수
		int nowPage = 0;	//현재 페이지
		int begin = 0;		//페이지 시작
		int end = 0;		//페이지 끝
		int pageSize = 50;	//한 페이지당 출력할 게시물 수 default = 50
		int totalCount = 0;	//총 게시물 수
		int totalPage = 0;	//총 페이지 수
		
		//type = all, recommend
		if(req.getParameter("type") != null) {
			listType =req.getParameter("type");
		}
		//head = 게시판 서브타이블 seq
		if(req.getParameter("head") != null) {
			subTitle = req.getParameter("head");
		}
		//list_size = 한 페이지당 출력할 게시물 수
		if(req.getParameter("page_size") != null) {
			pageSize = Integer.parseInt(req.getParameter("page_size"));
		}
		//접근방식 변경
		//만약 page를 클릭했을 경우 해당 page를 보여주고, 없으면 기본 1페이지를 보여준다.
		if(req.getParameter("page") != null) {
			nowPage = Integer.parseInt(req.getParameter("page"));
		} else {
			nowPage = 1;
		}
		//검색을 통해 게시판을 보는것인지
		// - subject_contents
		// - subject
		// - contents
		// - author
		if(req.getParameter("search") != null) {
			search = req.getParameter("search");
		}
		
		if(req.getParameter("keyword") != null) {
			keyword = req.getParameter("keyword");
		}
		//화면에 출력할 게시물 갯수
		begin = ((nowPage-1)*pageSize) + 1;
		end = begin + pageSize - 1;
				
		//검색 옵션을 세팅한 다음에
		board_option.put("category", category);
		board_option.put("listType", listType);
		board_option.put("subTitle", subTitle);
		board_option.put("search", search);
		board_option.put("keyword", keyword);
		board_option.put("begin", begin+"");
		board_option.put("end", end+"");
		//DB작업을 통해 가져온다.
		BoardDAO dao = new BoardDAO();
		ArrayList<BoardDTO> list = dao.getBoardList(board_option);
		
		//화면에 출력하기 위한 데이터 조작
		Calendar now = Calendar.getInstance();
		String strNow = String.format("%tF", now);
		
		for(BoardDTO boardDTO : list) {
			String regdate = boardDTO.getBoardRegdate();
			//오늘 등록된 게시물은 시간으로
			if(regdate.startsWith(strNow)) {
				boardDTO.setBoardRegdate(regdate.substring(11));
			//어제 등록된 게시물은 날짜로
			} else {
				boardDTO.setBoardRegdate(regdate.substring(0, 10));
			}
			//제목이 길면 잘라주기
			String subject = boardDTO.getBoardSubject();
			if(subject != null && subject.length() > 25) {
				boardDTO.setBoardSubject(subject.substring(0, 30) + "...");
			}

			//만약 태그를 직접 입력했을 경우 비활성화
			boardDTO.setBoardSubject(boardDTO.getBoardSubject()
					.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		}
		
		//총 페이지 수를 구한다.
		if(list.size() > 0) {
			totalCount = Integer.parseInt(list.get(0).getBoardCount());
			totalPage = (int)Math.ceil((double)(totalCount)/pageSize);
		}

		//최대 몇개의 블럭을 표현할것인지
		int blockSize = 10;
		//블럭의 시작이 몇번인지
		int startBlock = ((nowPage - 1)/blockSize)*blockSize+1;
		
		//검색옵션과 board list 전달
		req.setAttribute("boardSeq", boardSeq);
		req.setAttribute("boardTitleSeq", category);
		req.setAttribute("boardTitle", boardTitleDTO);
		req.setAttribute("visitCategoryList", visitCategoryList);
		req.setAttribute("boardSubTitleList", boardSubTitleList);
		req.setAttribute("head", subTitle);
		req.setAttribute("type", listType);
		req.setAttribute("search", search);
		req.setAttribute("keyword", keyword);
		req.setAttribute("list", list);
		req.setAttribute("board", board);
		//페이징을 위한 값들
		req.setAttribute("pageSize", pageSize);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("nowPage", nowPage);
		req.setAttribute("blockSize", blockSize);
		req.setAttribute("startBlock", startBlock);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/board/board.jsp");
		dispatcher.forward(req, resp);
	}
	
}
