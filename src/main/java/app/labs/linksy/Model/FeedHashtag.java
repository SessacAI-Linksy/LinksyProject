package app.labs.linksy.Model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FEED_HASHTAG")
@IdClass(FeedHashtagId.class) // 복합 키 설정
public class FeedHashtag implements Serializable {

	@Id
	@Column(name = "FEED_ID", nullable = false)
	private int feedId;

	@Id
	@Column(name = "HASHTAG_ID", nullable = false)
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

	// Getters and Setters
	public int getFeedId() {
		return feedId;
	}

	public void setFeedId(int feedId) {
		this.feedId = feedId;
	}

	public int getHashtagId() {
		return hashtagId;
	}

	public void setHashtagId(int hashtagId) {
		this.hashtagId = hashtagId;
	}

	public Feed getFeed() {
		return feed;
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	public Hashtag getHashtag() {
		return hashtag;
	}

	public void setHashtag(Hashtag hashtag) {
		this.hashtag = hashtag;
	}
}
