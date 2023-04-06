package board.start.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
	private String boardSeq;
	private String boardTitleSeq;
	private String boardTitle;
	private String boardSubTitleSeq;
	private String boardSubTitle;
	private String memberSeq;
	private String memberNickname;
	private String memberGrade;
	private String memberProfile;
	private String boardSubject;
	private String boardContent;
	private String boardRegdate;
	private String boardViews;
	private String boardThumbsUp;
	private String boardThumbsDown;
	private String boardCount;
	private String boardCommentCount;
	private String boardManagerSeq; 
}
