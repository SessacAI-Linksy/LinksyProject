package app.labs.linksy.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "FEED_HASHTAG")
@IdClass(FeedHashtagId.class)
public class FeedHashtag {

	@Id
	int feedId;

	@Id
	int hashtagId;

	// 생성자 추가
	public FeedHashtag(int feedId, int hashtagId) {
		this.feedId = feedId;
		this.hashtagId = hashtagId;
	}

	// 기본 생성자 (JPA를 위해 필요합니다)
	public FeedHashtag() {
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
}
