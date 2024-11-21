package app.labs.linksy.Repository;

import app.labs.linksy.Model.FeedImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedImageRepository extends CrudRepository<FeedImage, Integer> {
    // 기본적인 CRUD 메서드를 사용하기 위해 CrudRepository 상속
}
