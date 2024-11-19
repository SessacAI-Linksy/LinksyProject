package app.labs.linksy.Model;

import java.io.Serializable;
import java.util.Objects;

public class FeedHashtagId implements Serializable {
    private int feedId;
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

    // Getter와 Setter
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
