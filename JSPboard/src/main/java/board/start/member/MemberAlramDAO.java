package board.start.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import board.start.util.DBUtil;

public class MemberAlramDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public MemberAlramDAO() {
		conn = DBUtil.open();
	}
	
	public boolean deleteMemberAlramByCommentSeq(String seq) {
		boolean result = false;
		
		try {
			String sql = "delete from notification where comment_seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq);
			if(pstmt.executeUpdate() > -1) result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean deleteMemberAlramByBoardSeq(String seq) {
		boolean result = false;
		
		try {
			String sql = "delete from notification where board_seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seq);
			if(pstmt.executeUpdate() > -1) result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
