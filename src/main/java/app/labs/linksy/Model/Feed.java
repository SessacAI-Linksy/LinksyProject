package app.labs.linksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity // Feed를 JPA 엔티티로 표시
@Table(name = "FEED") // 테이블 이름을 FEED로 매핑
public class Feed {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_seq_generator")
	@SequenceGenerator(name = "feed_seq_generator", sequenceName = "FEED_SEQ", allocationSize = 1)
	@Column(name = "FEED_ID")
	int feedId;

	@Column(name = "USER_ID")
	String userId;

	@Column(name = "FEED_CONTENT")
	String feedContent;

	@Column(name = "FEED_TIME")
	Timestamp feedTime;

	@Column(name = "LIKE_AMOUNT")
	int likeAmount;

	// Feed와 FeedImage의 관계 설정
	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference // 순환 참조 방지
	private List<FeedImage> feedImages;

	// Getters and Setters
	public int getFeedId() {
		return feedId;
	}

	public void setFeedId(int feedId) {
		this.feedId = feedId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFeedContent() {
		return feedContent;
	}

	public void setFeedContent(String feedContent) {
		this.feedContent = feedContent;
	}

	public Timestamp getFeedTime() {
		return feedTime;
	}

	public void setFeedTime(Timestamp feedTime) {
		this.feedTime = feedTime;
	}

	public int getLikeAmount() {
		return likeAmount;
	}

	public void setLikeAmount(int likeAmount) {
		this.likeAmount = likeAmount;
	}

	public List<FeedImage> getFeedImages() {
		return feedImages;
	}

	public void setFeedImages(List<FeedImage> feedImages) {
		this.feedImages = feedImages;
	}
}
