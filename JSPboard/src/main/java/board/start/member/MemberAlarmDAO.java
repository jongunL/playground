package board.start.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import board.start.util.DBUtil;

public class MemberAlarmDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public MemberAlarmDAO() {
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

	public List<MemberAlarmDTO> getAlarmList(String memberSeq) {
		List<MemberAlarmDTO> result = null;
		try {
			String sql = "select "
					+ "    m.nickname as member_nickname, m.profile as member_profile, "
					+ "    n.created as alaram_created, n.message as message, "
					+ "    n.board_seq as board_seq, n.comment_seq as comment_seq, "
					+ "	   n.checked as alarm_checked, n.seq as memberAlarmSeq, "
					+ "    b.board_title_seq as board_title_seq "
					+ "from notification n "
					+ "inner join board_comment bc on n.comment_seq = bc.seq "
					+ "inner join board b on n.board_seq = b.seq "
					+ "inner join member m on n.member_sender_seq = m.seq "
					+ "where "
					+ "    n.member_receiver_seq = ? "
					+ "    and bc.active = 'y' "
					+ "	   and n.checked = 'n' ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberSeq);
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while(rs.next()) {
				MemberAlarmDTO memberAlarmDTO = new MemberAlarmDTO();
				memberAlarmDTO.setMemberAlarmSeq(rs.getString("memberAlarmSeq"));
				memberAlarmDTO.setMemberNickname(rs.getString("member_nickname"));
				memberAlarmDTO.setMemberProfile(rs.getString("member_profile"));
				memberAlarmDTO.setCreated(rs.getString("alaram_created"));
				memberAlarmDTO.setMessage(rs.getString("message"));
				memberAlarmDTO.setBoardSeq(rs.getString("board_seq"));
				memberAlarmDTO.setCommentSeq(rs.getString("comment_seq"));
				memberAlarmDTO.setChecked(rs.getString("alarm_checked"));
				memberAlarmDTO.setBoardTitleSeq(rs.getString("board_title_seq"));
				result.add(memberAlarmDTO);
			}
			
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getAlarmCount(String memberSeq) {
		int result = 0;
		
		try {
			String sql = "select count(*) as count "
					+ "from notification n "
					+ "inner join board_comment bc on n.comment_seq = bc.seq "
					+ "where "
					+ "    n.member_receiver_seq = ? "
					+ "    and bc.active = 'y' "
					+ "    and n.checked = 'n' ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberSeq);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean checkAlarm(MemberAlarmDTO memberAlarmDTO) {
		boolean result = false;
		
		try {
			String sql = "update notification set checked = 'y' "
					+ "where member_receiver_seq = ? and seq = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberAlarmDTO.getMemberReceiverSeq());
			pstmt.setString(2, memberAlarmDTO.getMemberAlarmSeq());
			if(pstmt.executeUpdate() < 1) {
				result = false;
				throw new SQLException("알람전송실패");
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean checkAllAlarm(MemberAlarmDTO memberAlarmDTO) {
		boolean result = false;
		
		try {
			String sql = "update notification set checked = 'y' "
					+ "where member_receiver_seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberAlarmDTO.getMemberReceiverSeq());
			if(pstmt.executeUpdate() < 1) {
				result = false;
				throw new SQLException("알람전송실패");
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}



















