package board.start.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberAlarmDTO {
	private String memberAlarmSeq;
	private String boardSeq;
	private String CommentSeq;
	private String memberReceiverSeq;
	private String memberSenderSeq;
	private String checked;
	private String created;
	private String message;
}
