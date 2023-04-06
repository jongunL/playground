package board.start.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.start.board.comment.BoardCommentDTO;
import board.start.util.DBUtil;
import oracle.jdbc.OracleTypes;

public class BoardDAO {
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	//main화면에 출력하는 최대 카테고리 개수
	int mainBoardAmount = 8;
	//main화면의 카테고리별 최대 출력 글
	int maxMainBoardColumn = 8;
	//best게시물로 등록될 좋아요 개수
	int minBestBoardThumbs = 0;
	
	public BoardDAO() {
		conn = DBUtil.open();
	}

	public BoardDTO getBoard(BoardDTO boardDTO) {
		BoardDTO result = null;
		
		try {
			String boardSeq = boardDTO.getBoardSeq();
			String boardTitleSeq = boardDTO.getBoardTitleSeq();
			
			//게시글 가져오기 전에 조회수 증가
			String updateBoardViews = "update board set views = views + 1 where seq = ?";
			pstmt = conn.prepareStatement(updateBoardViews);
			pstmt.setString(1, boardSeq);
			pstmt.executeUpdate();
			
			//게시글 가져오기
			String sql = "select "
					+ "		    bst.sub_title as board_sub_title,m.nickname as member_nickname, bm.seq as member_manager, "
					+ "		    b.subject as board_subject, b.contents as board_contents, b.regdate as board_regdate, "
					+ "		    b.thumbs_up as board_thumbs_up, b.thumbs_down as board_thumbs_down, b.views as board_views, "
					+ "		    (select count(*) from board_comment where board_seq = ?) as board_comment_count, "
					+ "			m.profile as member_profile, b.seq as board_seq, m.seq as member_seq,"
					+ "			b.board_title_seq as board_title_seq "
					+ "		from board b "
					+ "		inner join member m on b.member_seq = m.seq "
					+ "		inner join board_sub_title bst on b.board_sub_title_seq = bst.seq "
					+ "		left join board_manager bm on (b.member_seq = bm.member_seq and bm.board_title_seq = ?) "
					+ "		where b.seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardSeq);
			pstmt.setString(2, boardTitleSeq);
			pstmt.setString(3, boardSeq);
			rs= pstmt.executeQuery();
			if(rs.next()) {
				result = new BoardDTO();
				result.setBoardSubTitle(rs.getString("board_sub_title"));
				result.setMemberNickname(rs.getString("member_nickname"));
				result.setBoardManagerSeq(rs.getString("member_manager"));
				result.setBoardSubject(rs.getString("board_subject"));
				result.setBoardContent(rs.getString("board_contents"));
				result.setBoardRegdate(rs.getString("board_regdate"));
				result.setBoardThumbsUp(rs.getString("board_thumbs_up"));
				result.setBoardThumbsDown(rs.getString("board_thumbs_down"));
				result.setBoardViews(rs.getString("board_views"));
				result.setBoardCommentCount(rs.getString("board_comment_count"));
				result.setBoardTitleSeq(rs.getString("board_title_seq"));
				result.setMemberProfile(rs.getString("member_profile"));
				result.setBoardSeq(rs.getString("board_seq"));
				result.setMemberSeq(rs.getString("member_seq"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
		
	public ArrayList<BoardDTO> getBoardList(HashMap<String, String> board_option) {
		ArrayList<BoardDTO> result = new ArrayList<>();
		try {
			//board의 전체 개수를 구하기 위한 sql
			String count = "select max(rn) as boardCount ";
			//board의 결과를 얻기위한 sql, getBoardNum과 함께 사용
			String getBoard = "select board_seq, board_title_seq, board_sub_title_seq, board_sub_title, member_seq, member_nickname, member_grade, board_subject, board_regdate, board_views, board_thumbs_up, comment_count, board_manager_seq ";
			String sql =  "from ( "
					+ "    select rownum rn, board_seq, board_title_seq, board_sub_title_seq, board_sub_title, member_seq, member_nickname, member_grade, board_subject, board_regdate, board_views, board_thumbs_up, comment_count, board_manager_seq "
					+ "    from ( "
					+ "        select b.seq as board_seq, b.board_title_seq as board_title_seq, b.board_sub_title_seq as board_sub_title_seq, "
					+ "        bst.sub_title as board_sub_title , m.seq as member_seq, m.nickname as member_nickname, b.subject as board_subject, "
					+ "        b.regdate as board_regdate, b.views as board_views, b.thumbs_up as board_thumbs_up, count(bc.seq) as comment_count, "
					+ "		   m.grade as member_grade, bm.seq as board_manager_seq "
					+ "        from board b "
					+ "        inner join member m on b.member_seq = m.seq "
					+ "        inner join board_sub_title bst on b.board_sub_title_seq = bst.seq "
					+ "		   left join board_comment bc on b.seq = bc.board_seq "	
					+ "		   left join board_manager bm on (m.seq = bm.member_seq and bm.board_title_seq = ? and bm.active = 'y') "
					+ "	   where 1 = 1"
					+ "			 and b.board_title_seq = ? ";
			//글 유형
			if(board_option.get("listType").equals("recommend")) {
				sql += String.format("and b.thumbs_up >= %d ", minBestBoardThumbs);
			} 
			//글 소제목
			if(!board_option.get("subTitle").equals("all")) {
				sql += String.format("and b.board_sub_title_seq = %s ", board_option.get("subTitle"));
			}
			//검색
			String searchType = board_option.get("search");
			String keyword = board_option.get("keyword");
			if(searchType != null && keyword != null) {
				//제목 + 내용 검색
				if(searchType.equals("subject_contents")) {
					sql += String.format("and (LOWER(b.subject) like LOWER('%%%s%%') or LOWER(b.contents) like LOWER('%%%s%%')) ", keyword, keyword);
				}
				//제목 검색
				if(searchType.equals("subject")) {
					sql += String.format("and LOWER(b.subject) like LOWER('%%%s%%') ", keyword);
				}
				//내용 검색
				if(searchType.equals("contents")) {
					sql += String.format("and LOWER(b.contents) like LOWER('%%%s%%') ", keyword);
				}
				//작성자 검색
				if(searchType.equals("author")) {
					sql += String.format("and (m.active = 'y' and m.nickname like('%%%s%%')) ", keyword);			
				}
			}
			sql	+="group by b.seq, b.board_title_seq, b.board_sub_title_seq, bst.sub_title, m.seq, m.nickname, m.grade, b.subject, b.regdate, b.views, b.thumbs_up, bm.seq "
				+ "        order by b.regdate desc "
				+ "    ) "
				+ ") ";
			String getBoardNum = "where rn between ? and ?";
			
			//해당 검색조건으로 최대 몇개의 게시물이 존재하는지 확인
			String totalBoardCount = null;
			pstmt = conn.prepareStatement(count + sql);
			pstmt.setString(1, board_option.get("category"));
			pstmt.setString(2, board_option.get("category"));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				totalBoardCount = rs.getString("boardCount");
			}
			
			pstmt = conn.prepareStatement(getBoard + sql + getBoardNum);
			pstmt.setString(1, board_option.get("category"));
			pstmt.setString(2, board_option.get("category"));
			pstmt.setString(3, board_option.get("begin"));
			pstmt.setString(4, board_option.get("end"));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setBoardSeq(rs.getString("board_seq"));
				boardDTO.setBoardTitleSeq(rs.getString("board_title_seq"));
				boardDTO.setBoardSubTitleSeq(rs.getString("board_sub_title_seq"));
				boardDTO.setBoardSubTitle(rs.getString("board_sub_title"));
				boardDTO.setMemberSeq(rs.getString("member_seq"));
				boardDTO.setMemberGrade(rs.getString("member_grade"));
				boardDTO.setMemberNickname(rs.getString("member_nickname"));
				boardDTO.setBoardSubject(rs.getString("board_subject"));
				boardDTO.setBoardRegdate(rs.getString("board_regdate"));
				boardDTO.setBoardViews(rs.getString("board_views"));
				boardDTO.setBoardThumbsUp(rs.getString("board_thumbs_up"));
				boardDTO.setBoardCommentCount(rs.getString("comment_count"));
				boardDTO.setBoardManagerSeq(rs.getString("board_manager_seq"));
				boardDTO.setBoardCount(totalBoardCount);
				result.add(boardDTO);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean saveBoard(BoardDTO boardDTO, List<Map<String, String>> files) {
		boolean result = false;
		
		try {
			//글을 등록한 뒤에
			String insertBoard = "INSERT INTO board(seq, board_title_seq, member_seq, subject, contents, board_sub_title_seq) "
					+ "SELECT board_seq.nextval, ?, ?, ?, ?, ? "
					+ "FROM dual "
					+ "WHERE ? != 1";
			pstmt = conn.prepareStatement(insertBoard);
			pstmt = conn.prepareCall(insertBoard);
			pstmt.setString(1, boardDTO.getBoardTitleSeq());
			pstmt.setString(2, boardDTO.getMemberSeq());
			pstmt.setString(3, boardDTO.getBoardSubject());
			pstmt.setString(4, boardDTO.getBoardContent());
			pstmt.setString(5, boardDTO.getBoardSubTitleSeq());
			pstmt.setString(6, boardDTO.getBoardTitleSeq());
			//쿼리가 실패한경우
			if(pstmt.executeUpdate() != 1) {
				throw new SQLException("saveBoard board Insert Failed");
			}
			
			//파일이 등록되어 추가적인 DB작업을 해야하는 경우
			if(files != null && files.size() > 0) {
				//최근에 생성된 board Seq를 가져온다.
				long boardSeq;
				long fileSeq; 
				String selectBoardSeq = "select board_seq.currval from dual";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(selectBoardSeq);
				if(rs.next()) {
					boardSeq = rs.getLong(1);
				} else {
					throw new SQLException("saveBoard Baord Seq get Failed");
				}
				//이후 수행해야하는 추가적인 쿼리를 등록한다.
				String insertFile = 
						"insert into tbl_file(seq, original_filename, save_filename) "
									+ "values(tbl_file_seq.nextval, ?, ?)";
				String insertContentFile = 
					"insert into content_file(board_seq, file_seq) "
									+ "values(?, ?)";
				pstmt = conn.prepareStatement(insertFile);
				PreparedStatement pstmt2 = conn.prepareStatement(insertContentFile);
				for(int i=0; i<files.size(); i++) {
					pstmt.setString(1, files.get(i).get("originalFileName"));
					pstmt.setString(2, files.get(i).get("saveFileName"));
					if(pstmt.executeUpdate() != 1) {
						throw new SQLException("saveBoard tbl_file Insert Failed");
					}
					//tbl_seq의 insert결과로 반환되는 seq를 반환받는다.
					String selectTblFileSeq = "select tbl_file_seq.currval from dual";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(selectTblFileSeq);
					if(rs.next()) {
						fileSeq = rs.getLong(1);
					} else {
						throw new SQLException("saveBoard tlb_file Seq get Failed");
					}
					//이후
					pstmt2.setLong(1, boardSeq);
					pstmt2.setLong(2, fileSeq);
					if(pstmt2.executeUpdate() != 1) {
						throw new SQLException("saveBoard content_file Insert Failed");
					}
				}//for문 
			}//파일등록을 위한 if문
			
			//예외발생 없이 해당위치까지 도달하였다면 true 리턴
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<BoardSubTitleDTO> getBoardSubTitle(BoardSubTitleDTO boardSubTitleDTO) {
		List<BoardSubTitleDTO> result = null;
		
		try {
			String sql = 
					"select seq, board_title_seq, sub_title "
					+ "from board_sub_title "
					+ "where board_title_seq = ?";
			if(boardSubTitleDTO.getBoardTitleSeq() != null) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, boardSubTitleDTO.getBoardTitleSeq());
				rs = pstmt.executeQuery();

				result = new ArrayList<>();
				while(rs.next()) {
					BoardSubTitleDTO temp = new BoardSubTitleDTO();
					temp.setBoardTitleSeq(rs.getString("board_title_seq"));
					temp.setBoardSubTitleSeq(rs.getString("seq"));
					temp.setBoardSubTitle(rs.getString("sub_title"));
					result.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return result;
	}
	
	
	public int checkSubScribe(String boardTitleSeq, String memberSeq) {
		int result = 0;
		try {
			String sql = 
					"select count(*) as subscribeCheck "
				  + "from board_subscriber "
				  + "where board_title_seq = ? and member_seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardTitleSeq);
			pstmt.setString(2, memberSeq);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("subscribeCheck");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	public int changeSubScribe(String boardTitleSeq, String memberSeq) {
		int result = 0;
		try {
			String sql = 
					"select count(*) as subscribeCheck "
				  + "from board_subscriber "
				  + "where board_title_seq = ? and member_seq = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardTitleSeq);
			pstmt.setString(2, memberSeq);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int count = rs.getInt("subscribeCheck");
				//구독하지 않은경우
				if(count < 1) {
					sql = "insert into board_subscriber(seq, board_title_seq, member_seq) "
						+ "values(board_subscriber_seq.nextval, ?, ?)";
				//구독한경우
				} else {
					sql = "delete from board_subscriber "
							+ "where exists ( "
							+ "    select * "
							+ "    from board_subscriber "
							+ "    where board_title_seq = ? and member_seq = ? "
							+ ")";
				}
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, boardTitleSeq);
				pstmt.setString(2, memberSeq);
				result = pstmt.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	public BoardTitleDTO getBoardTitle(BoardTitleDTO boardTitleDTO) {
		BoardTitleDTO result = null;
		try {
			String sql = 
					"SELECT bt.seq as board_title_seq,bt.board_img as board_title_img, bt.board_title as board_title, bt.discription as board_discription, COUNT(bs.seq) as board_subscriber_count "
					+ "FROM board_title bt "
					+ "LEFT JOIN board_subscriber bs on bt.seq = bs.board_title_seq "
					+ "WHERE bt.seq = ? "
					+ "GROUP BY bt.seq, bt.board_title, bt.discription, bt.board_img";
			pstmt = conn.prepareStatement(sql);
			if(boardTitleDTO.getBoardTitle() != null) {
				pstmt.setString(1, boardTitleDTO.getBoardTitle());
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = new BoardTitleDTO();
					result.setBoardTitleSeq(rs.getString("board_title_seq"));
					result.setBoardTitle(rs.getString("board_title"));
					result.setBoardTitleImg(rs.getString("board_title_img"));
					result.setBoardTitleDescription(rs.getString("board_discription"));
					result.setBoardTitleSubscriberCount(rs.getInt("board_subscriber_count"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	//main화면에 출력하는 카테고리 보드리스트
	//TODO 짜다보니 보기 복잡해졌는데, 나중에 한번 수정하기
	public ArrayList<MainBoardDTO> getMainBoardList() {
		ArrayList<MainBoardDTO> result = null;
		try {
			String sql = 
				    "WITH best_board_list  AS ( "
				    + "    SELECT 1 as board_title_seq, best.seq as board_seq, best.subject as subject, best.regdate as regdate, COUNT(bc.seq) as board_comment_num "
				    + "    FROM "
				    + "        ( "
				    + "        SELECT seq, subject, regdate "
				    + "        FROM board "
				    + "        WHERE thumbs_up >= ? "
				    + "        ) best "
				    + "    LEFT JOIN board_comment bc ON best.seq = bc.board_seq "
				    + "    GROUP BY 1, best.seq, best.subject, best.regdate "
				    + ") "
				    + "SELECT title_seq, title, board_seq, subject, regdate, board_comment_num "
				    + "FROM ( "
				    + "    SELECT tlist.seq as title_seq, tlist.board_title as title, b.seq as board_seq, b.subject as subject, b.regdate as regdate, COUNT(bc.seq) as board_comment_num,\r\n"
				    + "    ROW_NUMBER() OVER (PARTITION BY tlist.seq ORDER BY b.regdate DESC) AS rn "
				    + "    FROM "
				    + "        ( "
				    + "            SELECT rs.seq, rs.board_title "
				    + "            FROM ( "
				    + "            SELECT bt.seq AS seq, bt.board_title AS board_title, COUNT(b.seq) AS board_count "
				    + "            FROM board_title bt "
				    + "            LEFT JOIN board b ON bt.seq = b.board_title_seq "
				    + "            WHERE bt.seq != 1 AND active = 'y' "
				    + "            GROUP BY bt.seq, bt.board_title "
				    + "            ORDER BY COUNT(b.seq) DESC "
				    + "        ) rs "
				    + "        WHERE ROWNUM < ? "
				    + "    ) tlist "
				    + "    LEFT JOIN board b ON tlist.seq = b.board_title_seq "
				    + "    LEFT JOIN board_comment bc ON b.seq = bc.board_seq "
				    + "    GROUP BY tlist.seq, tlist.board_title, b.seq, b.subject, b.regdate "
				    + "    UNION ALL "
				    + "    SELECT bt.seq as title_seq, bt.board_title as title, bbl.board_seq as board_seq, bbl.subject as subject, bbl.regdate as regdate, bbl.board_comment_num as board_comment_num, "
				    + "    ROW_NUMBER() OVER (PARTITION BY bt.seq ORDER BY bbl.regdate DESC) AS rn "
				    + "    FROM board_title bt "
				    + "    LEFT JOIN best_board_list bbl ON bt.seq = bbl.board_title_seq "
				    + "    WHERE bt.seq = 1 AND bt.active = 'y' "
				    + ") "
				    + "WHERE rn <= ? "
				    + "ORDER BY title_seq ASC, regdate DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, minBestBoardThumbs);
			pstmt.setInt(2, mainBoardAmount);
			pstmt.setInt(3, maxMainBoardColumn);
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while(rs.next()) {
				MainBoardDTO mainboardDTO = new MainBoardDTO();
				mainboardDTO.setBoardTitleSeq(rs.getString("title_seq"));
				mainboardDTO.setBoardTitle(rs.getString("title"));
				mainboardDTO.setBoardSeq(rs.getString("board_seq"));
				mainboardDTO.setBoardSubject(rs.getString("subject"));
				mainboardDTO.setBoardRegdate(rs.getString("regdate"));
				mainboardDTO.setBoardCommentCount(rs.getString("board_comment_num"));
				result.add(mainboardDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}

	public Map<String, Boolean> updateRecommend(BoardRecommendDTO boardRecommendDTO) {
		Map<String, Boolean> result = null;
		
		try {
			String memberSeq = boardRecommendDTO.getMemberSeq();
			String boardSeq = boardRecommendDTO.getBoardSeq();
			int recommend = boardRecommendDTO.getRecommend();
			
			String alreadyCk = "select count(*) as count "
					+ "from board_like "
					+ "where member_seq = ? and board_seq = ?";
			pstmt = conn.prepareStatement(alreadyCk);
			pstmt.setString(1, memberSeq);
			pstmt.setString(2, boardSeq);
			rs = pstmt.executeQuery();
			result = new HashMap<>();
			if(rs.next()) {
				result.put("auth", true);
				result.put("already", rs.getInt("count") > 0 ? true : false);
			} else {
				new SQLException("추천여부 조회 실패");
			}
			
			//조회 여부가 없는경우 상태 업데이트
			if(!result.get("already")) {
				String insertRecommend = "insert into board_like(member_seq, board_seq, board_like) values(?, ?, ?)";
				pstmt = conn.prepareStatement(insertRecommend);
				pstmt.setString(1, memberSeq);
				pstmt.setString(2, boardSeq);
				pstmt.setInt(3, recommend);
				int insertResult = pstmt.executeUpdate();
				
				if(insertResult > 0) {
					String changeBoardStatus = "update board set ";
					if(recommend == 1) {
						changeBoardStatus += " thumbs_up = thumbs_up + 1 ";
					} else if(recommend == -1) {
						changeBoardStatus += " thumbs_down = thumbs_down + 1 ";
					}
					changeBoardStatus += "where seq = ?";
					pstmt = conn.prepareStatement(changeBoardStatus);
					pstmt.setString(1, boardSeq);
					int boardUpdateResult = pstmt.executeUpdate();
					if(boardUpdateResult > 0) {
						result.put("success", true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
		}
		
		return result;
	}
	
}
