package app.labs.linksy.Repository;

//CRUD 기능을 제공,각각의 엔티티에 대응하는 Repository를 생성하여 데이터베이스 연산을 처리

import app.labs.linksy.Model.FeedHashtag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedHashtagRepository extends CrudRepository<FeedHashtag, Long> {
    // Feed ID를 기반으로 해시태그 가져오기 등 추가 메서드가 필요할 수 있음
}
