package board.start.board.handler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@WebServlet("/board/write/changeSummerNoteImageFile")
public class ChangeSummerNoteImageFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		//req가 파일인 경우 확인
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		int maxSize = 100 * 1024 * 1024;					//최대 저장크기
		String saveDir = "C:\\summernoteImg\\temp\\";		//저장 경로
		File currentDir = new File(saveDir);				//파일을 저장시키기 위한 객체
		JSONArray jsonArray = new JSONArray();				//JSP페이지에 전송하기 위한 JSON Array 객체 생성
		String showPath = "\\asset\\files\\temp\\";			//JSP페이지에서 저장된 파일에 접근하기 위한 Path
		
		//파일이라면
		if(isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory(maxSize, currentDir);
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				//전송된 FormField의 값을 받아온 뒤
				List<FileItem> items = upload.parseRequest(req);
				for(FileItem item : items) {
					//업로드한 파일이 파일인경우
					if(!item.isFormField()) {						
						//업로드한 파일들의 정보를 갖고, 원하는 처리를 한다.
						//업로드한 파일명을 가져온뒤
						String directoryName = getTodayStr();
						String origin = item.getName();
						//오늘 일자의 디렉터리에 파일에 저장
						File upPath = new File(currentDir + "\\" + directoryName);
						//만약 오늘 일자의 디렉터리가 존재하지 않는다면
						if(!upPath.exists()) {
							//생성해준다.
							upPath.mkdir();
						}
						//이후 파일을 저장하기 전 파일이름 중복체크를 한 뒤
						String saveFileName = getFileName(upPath, origin);
						//저장될 파일이름을 설정한 뒤
						File saveFile = new File(upPath, saveFileName);
						//파일을 저장하고
						item.write(saveFile);
						//JSON Object에 값을 세팅해준 뒤
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("path", showPath + directoryName + "\\");		//이때 showPath는 Server.xml에 mapping해둔 저장경로
						jsonObject.put("originalFileName", origin);
						jsonObject.put("saveFileName", saveFileName);
						//JSON Array에 추가한다.
						jsonArray.add(jsonObject);
					}//if
				}//for
			} catch (Exception e) {
				e.printStackTrace(); 
			}//try-catch

			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			resp.getWriter().print(jsonArray.toString());
		}
		
	}
	
	private String getFileName(File upPath, String origin) {
		String newFileName = origin;
		int count = 1;
		
		if(upPath.isDirectory()) {
			File[] files = upPath.listFiles();
			for(File f : files) {
				//중복되는 파일이름이 있는경우
				if(f.getName().equals(newFileName)) {
					//origin파일네임에 현재 count를 붙인 파일을 받아온 뒤, 반복
					newFileName = fileRename(origin, count);
					count++;
				}
			}
		}
		
		return newFileName;
	}

	//파일이름과 숫자를 주면 뒤에 붙여주고 반환하는 메서드
	private String fileRename(String origin, int count) {
		String name = null;
		String ext = null;
		int dot = origin.indexOf(".");
		if(dot != -1) {
			//확장자가 있는경우
			name = origin.substring(0, dot);
			ext = origin.substring(dot);
		} else {
			//확장자가 없는경우
			name = origin;
			ext = "";
		}
		return String.format("%s%03d%s", name, count, ext);
	}
	
	//오늘 일자를 얻어오는 메서드
	private String getTodayStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
	}
}
