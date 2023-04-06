package board.start.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VisitCategory {

	public static void addVisitCategory(HttpServletRequest req, HttpServletResponse resp, String titleSeq, String title) {
		//공백문자 치환 및 쿠키 구분자 사용
		String visitCategory = null;
		try {
			visitCategory = URLEncoder.encode(titleSeq + "," + title + ";", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Cookie[] cookies = req.getCookies();
		boolean searchFlag = false;
		//쿠키가 있는경우 탐색
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("visitList")) {
					searchFlag = true;
					if(!c.getValue().contains(visitCategory)) {
						c.setValue(c.getValue()+visitCategory);
						c.setMaxAge(24*60*60);
						resp.addCookie(c);
					}
				}
			}
		}
		
		//쿠키를 탐색했는데 찾지 못한경우
		if(searchFlag == false) {
			Cookie cookie = new Cookie("visitList", visitCategory);
			cookie.setMaxAge(24*60*60);
			resp.addCookie(cookie);
		}
		
	}
	
	public static void removeVisitCategory(HttpServletRequest req, HttpServletResponse resp, String category) {
		String visitCategoryList = getVisitCategoryList(req, resp);

		if(visitCategoryList != null && visitCategoryList.contains(category)) {
			visitCategoryList = visitCategoryList.replaceAll(category, "");
			Cookie cookie = new Cookie("visitList", URLEncoder.encode(visitCategoryList, StandardCharsets.UTF_8));
			cookie.setMaxAge(24*60*60);
			resp.addCookie(cookie);
		}
	}
	
	public static String getVisitCategoryList(HttpServletRequest req, HttpServletResponse resp) {
		String result = null;
		
		Cookie[] cookies = req.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("visitList")) {
					result = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
				}
			}
		}
		return result;
	}
	
	public static void removeAll(HttpServletRequest req, HttpServletResponse resp) {
		Cookie[] cookies = req.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals("visitList")) {
						c.setMaxAge(0);
						resp.addCookie(c);
					}
				}
			}
		}

}
