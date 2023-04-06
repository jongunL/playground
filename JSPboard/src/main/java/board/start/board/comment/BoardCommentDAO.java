package board.start.board.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import board.start.util.DBUtil;

public class BoardCommentDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public BoardCommentDAO() {
		conn = DBUtil.open();
	}
	
	public ArrayList<BoardCommentDTO> getCommentListByboardSeq(BoardCommentDTO boardCommentDTO) {
		ArrayList<BoardCommentDTO> result = null;
		try {
			String boardTitleSeq = boardCommentDTO.getBoardTitleSeq();
			String boardSeq = boardCommentDTO.getBoardSeq();
			
			String sql = "select "
					+ "    m.seq as member_seq, m.nickname as member_nickname, m.profile as member_profile, "
					+ "    m.grade as member_grade, bc.board_auth_seq as board_auth_seq, "
					+ "    (select seq from board_manager where member_seq = m.seq and board_title_seq = ?) as member_manager, "
					+ "    bc.seq as board_comment_seq, bc.comment_group_seq as board_comment_group_seq, "
					+ "    bc.group_order_seq as board_comment_group_order_seq, bc.board_comment as board_comment, "
					+ "    bc.regdate as board_comment_regdate, bc.last_modified as board_comment_last_modified, "
					+ "    bc.thumbs_up as board_comment_thumbs_up, bc.thumbs_down as board_comment_thumbs_down "
					+ "from "
					+ "    ( "
					+ "        select * "
					+ "        from board_comment "
					+ "        where board_seq = ? "
					+ "    ) bc "
					+ "inner join member m on bc.comment_auth_seq = m.seq "
					+ "order by board_comment_group_seq, board_comment_group_order_seq, board_comment_regdate ";
			if(boardTitleSeq != null && boardSeq != null) {
				result = new ArrayList<>();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, boardTitleSeq);
				pstmt.setString(2, boardSeq);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					BoardCommentDTO comment = new BoardCommentDTO();
					comment.setBoardSeq(boardSeq);
					comment.setBoardTitleSeq(boardTitleSeq);
					comment.setMemberSeq(rs.getString("member_seq"));
					comment.setMemberNickname(rs.getString("member_nickname"));
					comment.setMemberProfile(rs.getString("member_profile"));
					comment.setMemberGrade(rs.getString("member_grade"));
					comment.setMemberManager(rs.getString("member_manager"));
					comment.setBoardAuthSeq(rs.getString("board_auth_seq"));
					comment.setBoardCommentSeq(rs.getString("board_comment_seq"));
					comment.setBoardComment(rs.getString("board_comment"));
					comment.setBoardCommentGroupSeq(rs.getString("board_comment_group_seq"));
					comment.setBoardCommentGroupOrderSeq(rs.getString("board_comment_group_order_seq"));
					comment.setBoardCommentRegdate(rs.getString("board_comment_regdate"));
					comment.setBoardCommentLastModified(rs.getString("board_comment_last_modified"));
					comment.setBoardCommentThumbsUp(rs.getString("board_comment_thumbs_up"));
					comment.setBoardCommentThumbsDown(rs.getString("board_comment_thumbs_down"));
					result.add(comment);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
