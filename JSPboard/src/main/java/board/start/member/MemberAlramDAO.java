package board.start.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	public void sendMemberAlarm(MemberAlarmDTO memberAlarmDTO) {
		try {
			String sql = "insert into notification(seq, board_seq, member_receiver_seq, member_sender_seq, comment_seq, message) "
					+ " values(notification_seq.nextval, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberAlarmDTO.getBoardSeq());
			pstmt.setString(2, memberAlarmDTO.getMemberReceiverSeq());
			pstmt.setString(3, memberAlarmDTO.getMemberSenderSeq());
			pstmt.setString(4, memberAlarmDTO.getCommentSeq());
			pstmt.setString(5, memberAlarmDTO.getMessage());
			if(pstmt.executeUpdate() < 0) throw new SQLException("알람전송실패");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
