package board.start.util;

import java.security.MessageDigest;

public class SHA256 {

	public static String getHash(String input) {
		StringBuffer result = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] salt = "draibPSJ".getBytes();
			md.reset();
			md.update(salt);
			byte[] chars = md.digest(input.getBytes("UTF-8"));
			for(int i=0; i<chars.length; i++) {
				String hex = Integer.toHexString(0xff & chars[i]);
				if(hex.length() == 1) result.append('0');
				result.append(hex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result.toString();
		
	}

}
