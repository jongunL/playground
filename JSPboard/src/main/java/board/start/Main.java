package board.start;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import board.start.board.BoardDAO;
import board.start.board.MainBoardDTO;

@WebServlet("/")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, ArrayList<MainBoardDTO>> mainBoardMap = null;
	private LocalDateTime currentTime = LocalDateTime.now();
	private int maxSubjectLength = 20;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		
		BoardDAO boardDAO = new BoardDAO();
		ArrayList<MainBoardDTO> mainBoardList = boardDAO.getMainBoardList();

		if(mainBoardList != null) { 
			getMainBoardMap(mainBoardList);
		}

		req.setAttribute("mainBoard", mainBoardMap);
		
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/view/index.jsp");
		requestDispatcher.forward(req, resp);
	}

	private void getMainBoardMap(ArrayList<MainBoardDTO> mainBoardList) {
		mainBoardMap = new HashMap<>();
		
		for(MainBoardDTO mainBoardDTO : mainBoardList) {
			String key = mainBoardDTO.getBoardTitleSeq();
			String regdate = mainBoardDTO.getBoardRegdate();
			String subject = mainBoardDTO.getBoardSubject();			
			//출력형식 변경
			if(regdate != null) mainBoardDTO.setBoardRegdate(setTimeFormatting(regdate));
			if(subject != null) mainBoardDTO.setBoardSubject(setSubjectFormatting(subject));
			//Map에 추가
			addGroup(key);
			mainBoardMap.get(key).add(mainBoardDTO);
		}

	}

	private void addGroup(String boardTitleSeq) {
		if(!mainBoardMap.containsKey(boardTitleSeq)) {
			mainBoardMap.put(boardTitleSeq, new ArrayList<>());
		}
	}
	
	//시간 출력형식
	private String setTimeFormatting(String boardRegdate) {
		String result = null;
		
		LocalDateTime inputTime = LocalDateTime.parse(boardRegdate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Duration duration = Duration.between(inputTime, currentTime);
		long totalSecond = Math.abs(duration.getSeconds());
		long hour = totalSecond / (60 * 60);
		long minute = totalSecond / 60;
		
		//하루 이상 경과한 경우
		if(hour > 23) {
			result = inputTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		//1시간 이상 경과한 경우
		} else if(hour > 0) {
			result = hour + "시간전";
		//분단위인 경우
		} else {
			result = minute + "분전";
		}
		
		return result;
	}
	//제목 최대 길이 표시
	private String setSubjectFormatting(String subject) {
		String result = null;
		
		if(subject.length() > maxSubjectLength) result = subject.substring(0, maxSubjectLength) + "...";
		else result = subject;
		
		return result;
	}


}