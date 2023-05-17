package board.start.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
	private String seq;
	private String id;
	private String pwd;
	private String nickname;
	private String email;
	private String profile;
	private String regdate;
	private String active;
	private String grade;
}
