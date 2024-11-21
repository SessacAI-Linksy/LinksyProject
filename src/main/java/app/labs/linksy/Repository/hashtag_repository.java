package app.labs.linksy.Repository;

import app.labs.linksy.Model.Hashtag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface hashtag_repository extends CrudRepository<Hashtag, Integer> {
    Hashtag findByHashtag(String hashtag);
}
