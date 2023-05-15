package board.start.board.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.start.util.DBUtil;

public class BoardCommentDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public BoardCommentDAO() {
		conn = DBUtil.open();
	}
	
	public ArrayList<BoardCommentDTO> getCommentList(Map<String, String> commentOption) {
		ArrayList<BoardCommentDTO> result = null;
		try {
			String boardTitleSeq = commentOption.get("category");
			String boardSeq = commentOption.get("boardSeq");
			String begin = commentOption.get("begin");
			String end = commentOption.get("end");
			String sort = commentOption.get("sort");
			String sortOption = null;
			
			if(sort.equals("registrationDate")) {
				sortOption = "asc";
			} else if(sort.equals("newest")) {
				sortOption = "desc";
			} else {
				sortOption = "asc";
			}

			String sql = "select * "
					+ "from( "
					+ "    select rownum as rn, rs.* "
					+ "    from( "
					+ "        select "
					+ "            m.seq as member_seq, m.nickname as member_nickname, m.profile as member_profile, "
					+ "            m.grade as member_grade, bc.board_auth_seq as board_auth_seq, \r\n"
					+ "            ( "
					+ "                select seq "
					+ "                from board_manager "
					+ "                where member_seq = m.seq and board_title_seq = ?"
					+ "            ) as member_manager, "
					+ "            bc.seq as board_comment_seq, bc.comment_group_seq as board_comment_group_seq, "
					+ "            bc.group_order_seq as board_comment_group_order_seq, bc.board_comment as board_comment, "
					+ "            bc.regdate as board_comment_regdate, bc.last_modified as board_comment_last_modified, "
					+ "            bc.thumbs_up as board_comment_thumbs_up, bc.thumbs_down as board_comment_thumbs_down, "
					+ "            bc.active as board_comment_active "
					+ "        from ( "
					+ "            select * "
					+ "            from board_comment "
					+ "            where board_seq = ? "
					+ "            and ( "
					+ "                     board_comment.active = 'y' "
					+ "                     or board_comment.seq = board_comment.comment_group_seq "
					+ "                     and board_comment.seq in ( "
					+ "                        select comment_group_seq "
					+ "                        from board_comment "
					+ "                        where active = 'y' "
					+ "                        group by comment_group_seq having count(comment_group_seq) > 0 "
					+ "                      ) "
					+ "                  ) "
					+ "        ) bc "
					+ "        inner join member m on bc.comment_auth_seq = m.seq "
					+ "        order by board_comment_group_seq "+sortOption+", board_comment_group_order_seq "
					+ "    ) rs "
					+ ")"
					+ "where rn between ? and ?";
			
			if(boardTitleSeq != null && boardSeq != null) {
				result = new ArrayList<>();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, boardTitleSeq);
				pstmt.setString(2, boardSeq);
				pstmt.setString(3, begin);
				pstmt.setString(4, end);
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
					comment.setBoardCommentActive(rs.getString("board_comment_active"));
					result.add(comment);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public long getCommentRownum(BoardCommentDTO boardCommentDTO) {
		long result = 0;
		
		try {
			String boardTitleSeq = boardCommentDTO.getBoardTitleSeq();
			String boardSeq = boardCommentDTO.getBoardSeq();
			String cmt = boardCommentDTO.getBoardCommentSeq();
			String sortOption = "asc";
			
			String sql = "select rn "
					+ "from( "
					+ "    select rownum as rn, rs.* "
					+ "    from( "
					+ "        select "
					+ "            m.seq as member_seq, m.nickname as member_nickname, m.profile as member_profile, "
					+ "            m.grade as member_grade, bc.board_auth_seq as board_auth_seq, \r\n"
					+ "            ( "
					+ "                select seq "
					+ "                from board_manager "
					+ "                where member_seq = m.seq and board_title_seq = ?"
					+ "            ) as member_manager, "
					+ "            bc.seq as board_comment_seq, bc.comment_group_seq as board_comment_group_seq, "
					+ "            bc.group_order_seq as board_comment_group_order_seq, bc.board_comment as board_comment, "
					+ "            bc.regdate as board_comment_regdate, bc.last_modified as board_comment_last_modified, "
					+ "            bc.thumbs_up as board_comment_thumbs_up, bc.thumbs_down as board_comment_thumbs_down, "
					+ "            bc.active as board_comment_active "
					+ "        from ( "
					+ "            select * "
					+ "            from board_comment "
					+ "            where board_seq = ? "
					+ "            and ( "
					+ "                     board_comment.active = 'y' "
					+ "                     or board_comment.seq = board_comment.comment_group_seq "
					+ "                     and board_comment.seq in ( "
					+ "                        select comment_group_seq "
					+ "                        from board_comment "
					+ "                        where active = 'y' "
					+ "                        group by comment_group_seq having count(comment_group_seq) > 0 "
					+ "                      ) "
					+ "                  ) "
					+ "        ) bc "
					+ "        inner join member m on bc.comment_auth_seq = m.seq "
					+ "        order by board_comment_group_seq "+sortOption+", board_comment_group_order_seq "
					+ "    ) rs "
					+ ")"
					+ "where board_comment_seq = ?";
			
			if(boardTitleSeq != null && boardSeq != null && cmt != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, boardTitleSeq);
				pstmt.setString(2, boardSeq);
				pstmt.setString(3, cmt);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					result = rs.getLong("rn");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String saveBoardComment(BoardCommentDTO boardCommentDTO) {
		String result = null;
		try {
			String sql = "insert into board_comment(seq, board_seq, board_auth_seq, board_comment, comment_auth_seq";
			if(boardCommentDTO.getBoardCommentGroupSeq() != null && boardCommentDTO.getBoardCommentGroupOrderSeq() != null) {
				sql += ", comment_group_seq, group_order_seq";
			}
			sql +=  ") values(board_comment_seq.nextval, ?, ?, ?, ?";
			if(boardCommentDTO.getBoardCommentGroupSeq() != null && boardCommentDTO.getBoardCommentGroupOrderSeq() != null) {
				sql += ", ?, (select sum(max(group_order_seq) + 1) "
						+ "from board_comment "
						+ "where comment_group_seq = ? "
						+ "group by comment_group_seq)";
			}
			sql += ")";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardCommentDTO.getBoardSeq());
			pstmt.setString(2, boardCommentDTO.getBoardAuthSeq());
			pstmt.setString(3, boardCommentDTO.getBoardComment());
			pstmt.setString(4, boardCommentDTO.getMemberSeq());
			
			if(boardCommentDTO.getBoardCommentGroupSeq() != null && boardCommentDTO.getBoardCommentGroupOrderSeq() != null) {				
				pstmt.setString(5, boardCommentDTO.getBoardCommentGroupSeq());
				pstmt.setString(6, boardCommentDTO.getBoardCommentGroupSeq());
			}
			
			if(pstmt.executeUpdate() > 0) {
				String getCommentSeq = "select board_comment_seq.currval from dual";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(getCommentSeq);
				if(rs.next()) {
					result = rs.getString(1);
				}
			} else {
				throw new SQLException("board comment insert failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public BoardCommentDTO getComment(BoardCommentDTO boardCommentDTO) {
		BoardCommentDTO result = null;
		try {
			String sql = "select "
					+ "    m.seq as member_seq, m.nickname as member_nickname, bc.seq as board_comment_seq, "
					+ "    m.grade as member_grade, m.profile as member_profile, bc.active as board_comment_active, "
					+ "    (select seq from board_manager where member_seq = m.seq and board_title_seq = ?) as member_manager, "
					+ "    bc.board_comment as board_comment, bc.comment_group_seq as comment_group_seq, "
					+ "    bc.group_order_seq as group_order_seq, bc.regdate as board_comment_regdate, "
					+ "    bc.thumbs_up as board_comment_thumbs_up, bc.thumbs_down as board_comment_thumbs_down,"
					+ "	   bc.board_auth_seq as board_auth_seq "
					+ "from "
					+ "    ( "
					+ "        select * "
					+ "        from board_comment "
					+ "        where seq = ? "
					+ "    ) bc "
					+ "inner join member m on bc.comment_auth_seq = m.seq";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardCommentDTO.getBoardTitleSeq());
			pstmt.setString(2, boardCommentDTO.getBoardCommentSeq());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = new BoardCommentDTO();
				result.setBoardCommentSeq(rs.getString("board_comment_seq"));
				result.setMemberSeq(rs.getString("member_seq"));
				result.setMemberNickname(rs.getString("member_nickname"));
				result.setMemberGrade(rs.getString("member_grade"));
				result.setMemberProfile(rs.getString("member_profile"));
				result.setMemberManager(rs.getString("member_manager"));
				result.setBoardComment(rs.getString("board_comment"));
				result.setBoardCommentGroupSeq(rs.getString("comment_group_seq"));
				result.setBoardCommentGroupOrderSeq(rs.getString("group_order_seq"));
				result.setBoardCommentRegdate(rs.getString("board_comment_regdate"));
				result.setBoardCommentThumbsUp(rs.getString("board_comment_thumbs_up"));
				result.setBoardCommentThumbsDown(rs.getString("board_comment_thumbs_down"));
				result.setBoardCommentActive(rs.getString("board_comment_active"));
				result.setBoardAuthSeq(rs.getString("board_auth_seq"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean deleteComment(BoardCommentDTO boardCommentDTO) {
		boolean result = false;
		
		try {
			String sql = "update board_comment set active = 'n' "
					+ " where comment_auth_seq = ? and ";
			sql += String.format(" seq in(%s) ", boardCommentDTO.getBoardCommentSeq());
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardCommentDTO.getMemberSeq());
			if(pstmt.executeUpdate() > 0) result = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public boolean updateComment(BoardCommentDTO boardCommentDTO) {
		boolean result = false;
		try {
			String sql = "update board_comment set board_comment= ?, last_modified = sysdate where seq = ? "
					+ "and comment_auth_seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardCommentDTO.getBoardComment());
			pstmt.setString(2, boardCommentDTO.getBoardCommentSeq());
			pstmt.setString(3, boardCommentDTO.getMemberSeq());
			if(pstmt.executeUpdate() > 0) result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Map<String, Boolean> updateRecommend(BoardCommentRecommendDTO boardCommentRecommendDTO) {
		Map<String, Boolean> result = null;
		
		try {
			String memberSeq = boardCommentRecommendDTO.getMemberSeq();
			String commentSeq = boardCommentRecommendDTO.getCommentSeq();
			int recommend = boardCommentRecommendDTO.getRecommend();
			
			String alreadyCk = "select count(*) as count "
					+ "from comment_like "
					+ "where member_seq = ? and comment_seq = ?";
			pstmt = conn.prepareStatement(alreadyCk);
			pstmt.setString(1, memberSeq);
			pstmt.setString(2, commentSeq);
			rs = pstmt.executeQuery();
			result = new HashMap<>();
			if(rs.next()) {
				result.put("auth", true);
				result.put("already", rs.getInt("count") > 0 ? true : false);
			} else {
				new SQLException("댓글 추천여부 조회 실패");
			}
			
			//추천을 하지 않았을 경우 상태 업데이트
			if(!result.get("already")) {
				String insertRecommend = "insert into comment_like(member_seq, comment_seq, comment_like) "
						+ "values(?, ?, ?)";
				pstmt = conn.prepareStatement(insertRecommend);
				pstmt.setString(1, memberSeq);
				pstmt.setString(2, commentSeq);
				pstmt.setLong(3, recommend);
				int insertResult = pstmt.executeUpdate();
				if(insertResult > 0) {
					String changeCommentStatus = "update board_comment set ";
					if(recommend == 1) {
						changeCommentStatus += "thumbs_up = thumbs_up + 1 ";
					} else if(recommend == -1) {
						changeCommentStatus += "thumbs_down = thumbs_down + 1 ";
					}
					changeCommentStatus += "where seq = ?";
					pstmt = conn.prepareStatement(changeCommentStatus);
					pstmt.setString(1, commentSeq);
					int commentUpdateResult = pstmt.executeUpdate();
					if(commentUpdateResult > 0) {
						result.put("success", true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("auth", true);
			result.put("success", false);
		}
		
		return result;
	}

	public String getCommentCountByBoardSeq(String boardSeq) {
		String result = null;
		
		try {
			String sql = " select count(*) as count "
					+ "from board_comment "
					+ "where board_seq = ? "
					+ "and ( "
					+ "         board_comment.active = 'y' "
					+ "         or board_comment.seq = board_comment.comment_group_seq "
					+ "         and board_comment.seq in ( "
					+ "            select comment_group_seq "
					+ "            from board_comment "
					+ "            where active = 'y' "
					+ "            group by comment_group_seq having count(comment_group_seq) > 0 "
					+ "          ) "
					+ "    )";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardSeq);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getString("count");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public List<BoardCommentDTO> getMemberAuthorCommentList(Map<String, String> option) {
		List<BoardCommentDTO> result = null;
		
		try {
			String sql = "select * "
					+ "from ( "
					+ "    select "
					+ "        rownum as rn, "
					+ "        bt.seq as board_title_seq, "
					+ "        bt.board_title as board_title, "
					+ "        bt.board_img as board_title_img, "
					+ "        b.seq as board_seq, "
					+ "        bc.seq as board_comment_seq, "
					+ "        bc.board_comment as board_comment, "
					+ "        bc.regdate as board_comment_regdate "
					+ "    from board_comment bc "
					+ "    inner join board b on bc.board_seq = b.seq "
					+ "    inner join board_title bt on b.board_title_seq = bt.seq "
					+ "	   where bc.comment_auth_seq = ? and bc.active = 'y' and b.active = 'y' and bt.active = 'y' "
					+ ") "
					+ "where rn between ? and ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, option.get("memberSeq"));
			pstmt.setString(2, option.get("begin"));
			pstmt.setString(3, option.get("end"));
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while(rs.next()) {
				BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
				boardCommentDTO.setBoardTitleSeq(rs.getString("board_title_seq"));
				boardCommentDTO.setBoardTitle(rs.getString("board_title"));
				boardCommentDTO.setBoardTitleImg(rs.getString("board_title_img"));
				boardCommentDTO.setBoardSeq(rs.getString("board_seq"));
				boardCommentDTO.setBoardCommentSeq(rs.getString("board_comment_seq"));
				boardCommentDTO.setBoardComment(rs.getString("board_comment"));
				boardCommentDTO.setBoardCommentRegdate(rs.getString("board_comment_regdate"));
				result.add(boardCommentDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int getMemberAuthorCommentCount(String memberSeq) {
		int result = -1;
		
		try {
			String sql = "select count(*) "
					+ "from board_comment bc "
					+ "inner join board b on bc.board_seq = b.seq "
					+ "inner join board_title bt on b.board_title_seq = bt.seq "
					+ "where bc.comment_auth_seq = ? and bc.active = 'y' and b.active = 'y' and bt.active = 'y' ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberSeq);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("count(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}











