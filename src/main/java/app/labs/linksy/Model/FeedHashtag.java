package app.labs.linksy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FEED_HASHTAG")
@IdClass(FeedHashtagId.class)
public class FeedHashtag implements Serializable {

	@Id
	@Column(name = "FEED_ID", nullable = false)
	private int feedId;

	@Id
	@Column(name = "HASHTAG_ID", nullable = false)
	private int hashtagId;

	@ManyToOne
	@JoinColumn(name = "FEED_ID", insertable = false, updatable = false)
	private Feed feed;

	@ManyToOne
	@JoinColumn(name = "HASHTAG_ID", insertable = false, updatable = false)
	private Hashtag hashtag;

	// feedId와 hashtagId만 받는 생성자 추가
	public FeedHashtag(int feedId, int hashtagId) {
		this.feedId = feedId;
		this.hashtagId = hashtagId;
	}
}
