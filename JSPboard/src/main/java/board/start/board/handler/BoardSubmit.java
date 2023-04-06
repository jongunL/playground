package board.start.board.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import board.start.board.BoardDAO;
import board.start.board.BoardDTO;
import board.start.util.Auth;

@WebServlet("/board/submit")
public class BoardSubmit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//데이터를 가져오고
		req.setCharacterEncoding("UTF-8");
		boolean result = false;
		Map<String, String> auth = Auth.getAuth(req);
		String category = req.getParameter("category");
		String subTitle = req.getParameter("board_sub_title");
		String subject = req.getParameter("board_subject");
		String content = req.getParameter("content");
		String filesData = req.getParameter("filesData");
		List<Map<String, String>> files = null;
		
		//이미지 파일에 대한 후처리를 해준다.
		if(filesData != null) {
			//지금까지 사용자가 입력한 이미지에 대한 정보를 가져옴
			files = getFileList(filesData);
			//실제로 넘어온 정보와 비교해서, 만약 포함되지 않는 파일이 있다면 복사대상에서 제외시킴
			for(int i=0; i<files.size(); i++) {
				if(!content.contains(files.get(i).get("path") + files.get(i).get("saveFileName"))) {
					files.remove(i);
					i--;
				}
			}
			//이제 실제로 넘어온 DB에서 관리할 경로로 파일을 복사시킨다.
			copyFile(files);
			//복사가 완료되었다면 summerynote에 의해 보여줄 path로 변경시킨다.
			content = content.replaceAll("\\\\asset\\\\files\\\\temp\\\\", "\\\\asset\\\\files\\\\board_file\\\\");			
		}
		
		
		//잘못된 값이 유입되거나, 로그인되지 않은경우
		if(category != null && subTitle != null && subject != null && content != null && auth != null) {
			//게시판 작성양식을 준수하고 활성화된 계정이라면 진행
			if(subject.length() > 2 && (content.length() > 5 && content.length() < 500) && "y".equals(auth.get("active"))) {
				//이제 실제로 게시판에 작성한 글을 처리한다.
				BoardDAO dao = new BoardDAO();
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setBoardTitleSeq(category);
				boardDTO.setBoardSubTitleSeq(subTitle);
				boardDTO.setBoardSubject(subject);
				boardDTO.setBoardContent(content);
				boardDTO.setMemberSeq(auth.get("seq"));
				result = dao.saveBoard(boardDTO, files);	
			}//if - 게시판 작성양식, 활성화 계정
		}//if - 잘못된 값 유입, 로그인되지 않은경우

		req.setAttribute("result", result);
		req.setAttribute("category", category);
		RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/board/board_writeOk.jsp");
		dispatcher.forward(req, resp);
	}
	
	private void copyFile(List<Map<String, String>> files) {
		
		//summernote가 이미지를 보여주기위한 showPath를 실제 패스인 realPath로 치환한 뒤,
		//해당 경로에 있는 파일을, 실제로 board_file들을 관리하는 폴더로 이동시키기 위한 메서드
		String showPath = "\\\\asset\\\\files\\\\temp\\\\";
		String realPath = "C:\\\\summernoteImg\\\\temp\\\\";
		String copyPath = "C:\\summernoteImg\\board_file\\";
		
		files.stream().forEach(file -> {
			String path = file.get("path").replaceAll(showPath , realPath);
			String[] pathElements = path.split("\\\\");
			String date = pathElements[pathElements.length-1];
			//실제로 DB에서 관리될 디렉터리 패스가 존재하지 않는다면, 만든 다음
			File upPath = new File(copyPath + date);
			if(!upPath.exists()) upPath.mkdir();
			
			//파일을 복사한다.
			File tempFile = new File(path + "\\" + file.get("saveFileName"));
			File copyFile = new File(upPath.getPath() + "\\" + file.get("saveFileName"));
			
			try {
				Files.copy(tempFile.toPath(), copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		
	}

	//넘어온 JSON데이터 파싱해서 담아주기
	private List<Map<String, String>> getFileList(String filesData) {
		List<Map<String, String>> result = null;
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonArray = (JSONArray)jsonParser.parse(filesData);
			Iterator it = jsonArray.iterator();
			result = new ArrayList<>();
			while (it.hasNext()) {
				JSONObject object = (JSONObject)it.next();
				Map<String, String> temp = new HashMap<>();
				temp.put("path", (String) object.get("path"));
				temp.put("saveFileName", (String) object.get("saveFileName"));
				temp.put("originalFileName", (String) object.get("originalFileName"));
				result.add(temp);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}




