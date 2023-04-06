package board.start.util;

import java.util.Random;

public class CreateCode {
	private static int size = 6;
	private static boolean lowerCheck = false; 
	
	//기본 6자리
	public static String getCode() {
		return getCode(size);
	}
	//길이 설정
	public static String getCode(int codeLength) {	
		return getCode(codeLength, lowerCheck);
	}
	//true = 대, 소문자, false = 소문자
	public static String getCode(boolean lower) {
		return getCode(size, lower);
	}
	
	public static String getCode(int codeLength, boolean lower) {	
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		int num = 0;
		do {
			num = random.nextInt(75)+48;
			if((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
				sb.append((char)num);
			} else {
				continue;
			}
		} while(sb.length() < size);
		
		if(lowerCheck == true) {
			return sb.toString().toLowerCase();
		}
		
		return sb.toString();
	}
	
}
