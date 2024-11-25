package app.labs.linksy.Model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
	private int feedId;
	private String userId;
	private String feedContent;
	private Timestamp feedTime;
	private int likeAmount;
	
	private Member member;  // Member 객체를 참조
	private FeedImage feedImages;  // FeedImage 객체를 참조
    private List<Comment> comments; // 댓글 리스트 추가
	private int commentCount;
	private List<Hashtag> hashtags;
}
