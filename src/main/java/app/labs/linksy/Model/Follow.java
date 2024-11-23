package app.labs.linksy.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Follow {
	private int followId;
	private String followerId;
	private String folloingId;
	
	private Member member;
}
