package board.start.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardTitleDTO {
	private String regdate;
	private String boardTitleSeq;
	private String boardTitle;
	private String boardTitleImg;
	private String boardTitleDescription;
	private int boardTitleSubscriberCount;
}
