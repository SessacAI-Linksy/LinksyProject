package app.labs.linksy.Repository;

// CRUD 기능을 제공, 각각의 엔티티에 대응하는 Repository를 생성하여 데이터베이스 연산을 처리

import app.labs.linksy.Model.FeedHashtag;
import app.labs.linksy.Model.FeedHashtagId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedHashtagRepository extends CrudRepository<FeedHashtag, FeedHashtagId> {
    // Feed ID를 기반으로 해시태그 가져오기 등 추가 메서드가 필요할 수 있음
    // feedId와 hashtagId를 기반으로 피드-해시태그 관계를 찾는 메서드 정의
    Optional<FeedHashtag> findByFeedIdAndHashtagId(int feedId, int hashtagId);
}