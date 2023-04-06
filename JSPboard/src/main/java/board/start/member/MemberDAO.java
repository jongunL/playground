package board.start.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import board.start.util.DBUtil;

public class MemberDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public MemberDAO() {
		conn = DBUtil.open();
	}
	
	public int signUp(MemberDTO memberDTO) {
		
		int result = -1;
		
		try {
			String sql = "insert into member(seq, id, pwd, nickname, email) values(member_seq.nextval, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getPwd());
			pstmt.setString(3, memberDTO.getNickname());
			pstmt.setString(4, memberDTO.getEmail());
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			try { conn.rollback(); } catch (SQLException e1) { e.printStackTrace(); }
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(rs != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		return result;
	}
	
	//TODO 나중에 쿠키 암호화 또는 jwt 인증방식을 통해 로그인 방식 바꿔보기
	//select * from member where id='admin' and id='admin' and active = 'y'; 
	public MemberDTO signIn(MemberDTO memberDTO) {
		MemberDTO result = null;
		
		try {
			String sql = "select * from member where id=? and pwd=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getPwd());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = new MemberDTO();
				result.setSeq(rs.getString("seq"));
				result.setId(rs.getString("id"));
				result.setNickname(rs.getString("nickname"));
				result.setProfile(rs.getString("profile"));
				result.setRegdate(rs.getString("regdate"));
				result.setActive(rs.getString("active"));
				result.setGrade(rs.getString("grade"));
				result.setEmail(rs.getString("email"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(rs != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		return result;
	}//signIn
	
	public MemberDTO getIdByEmail(String auth) {
		MemberDTO result = null;
		
		try {
			String sql = "select * from member where email = ?";
			if(auth != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, auth);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = new MemberDTO();
					result.setSeq(rs.getString("seq"));
					result.setId(rs.getString("id"));
					result.setNickname(rs.getString("nickname"));
					result.setProfile(rs.getString("profile"));
					result.setRegdate(rs.getString("regdate"));
					result.setActive(rs.getString("active"));
					result.setGrade(rs.getString("grade"));
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(rs != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		return result;
	}//getIdByEmail

	//TODO 중복체크 id, email, nickname은 조건문 사용해서 하나로 통합할 수 있으나 어떤게 나은 방법인지 아직 모르겠음
	public int duplicationCheckById(MemberDTO memberDTO) {
		
		String id = null;
		int result = -1;
		
		try {
			String sql = "select count(id) from member where id = ?";
			id = memberDTO.getId(); 
			if(id != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				if(rs.next()) result = rs.getInt("count(id)");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(rs != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		return result;
	}//duplicationCheckById
	
	public int duplicationCheckByEmail(MemberDTO memberDTO) {
		String email = null;
		int result = -1;
		
		try {
			String sql = "select count(email) from member where email = ?";
			email = memberDTO.getEmail(); 
			if(email != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, email);
				rs = pstmt.executeQuery();
				
				if(rs.next()) result = rs.getInt("count(email)");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(rs != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		return result;
	}//duplicationCheckByEmail
	
	public int duplicationCheckByNickname(MemberDTO memberDTO) {
		String nickname = null;
		int result = -1;
		
		try {
			String sql = "select count(nickname) from member where nickname = ?";
			nickname = memberDTO.getNickname(); 
			if(nickname != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, nickname);
				rs = pstmt.executeQuery();
				
				if(rs.next()) result = rs.getInt("count(nickname)");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(stmt != null) stmt.close(); } catch (Exception e) { e.printStackTrace(); }
			try { if(rs != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
		}
		
		return result;
	}//duplicationCheckByNickname

}
