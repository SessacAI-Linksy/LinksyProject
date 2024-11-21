package app.labs.linksy.Model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@IdClass(FeedHashtagId.class)
@Table(name = "FEED_HASHTAG")
public class FeedHashtag {
	@Id
	@Column(name = "FEED_ID")
	private int feedId;

	@Id
	@Column(name = "HASHTAG_ID")
	private int hashtagId;

	// 관계 매핑: Feed와 Hashtag 참조
	@ManyToOne
	@JoinColumn(name = "FEED_ID", insertable = false, updatable = false)
	private Feed feed;

	@ManyToOne
	@JoinColumn(name = "HASHTAG_ID", insertable = false, updatable = false)
	private Hashtag hashtag;

	// 기본 생성자
	public FeedHashtag() {
	}

	// 생성자 추가
	public FeedHashtag(int feedId, int hashtagId) {
		this.feedId = feedId;
		this.hashtagId = hashtagId;
	}
}