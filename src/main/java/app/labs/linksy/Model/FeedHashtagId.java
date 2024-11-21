package app.labs.linksy.Model;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;

@Getter @Setter @ToString
@Entity
@Table(name = "FEED_HASHTAG")
public class FeedHashtagId implements Serializable {
	@Id
	@Column(name = "FEED_ID")
    private int feedId;

	@Column(name = "HASHTAG_ID")
    private int hashtagId;

    // 기본 생성자
    public FeedHashtagId() {}

    // 생성자
    public FeedHashtagId(int feedId, int hashtagId) {
        this.feedId = feedId;
        this.hashtagId = hashtagId;
    }

    // equals 메서드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedHashtagId that = (FeedHashtagId) o;
        return feedId == that.feedId && hashtagId == that.hashtagId;
    }

    // hashCode 메서드
    @Override
    public int hashCode() {
        return Objects.hash(feedId, hashtagId);
    }

}
