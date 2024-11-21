package app.labs.linksy.Repository;

import app.labs.linksy.Model.Feed;
import app.labs.linksy.Model.FeedHashtag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface feed_create_repository extends CrudRepository<Feed, Integer> {
    // 기본적인 CRUD 메서드를 사용하기 위해 CrudRepository 상속
}


