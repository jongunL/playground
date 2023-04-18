package board.start.board.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardCommentDTO {
	private boolean authorCk;
	private String boardSeq;
	private String boardTitleSeq;
	private String boardAuthSeq;
	private String memberSeq;
	private String memberNickname;
	private String memberProfile;
	private String memberGrade;
	private String memberManager;
	private String boardCommentSeq;
	private String boardComment;
	private String boardCommentActive;
	private String boardCommentGroupSeq;
	private String boardCommentGroupOrderSeq;
	private String boardCommentRegdate;
	private String boardCommentLastModified;
	private String boardCommentThumbsUp;
	private String boardCommentThumbsDown;
}