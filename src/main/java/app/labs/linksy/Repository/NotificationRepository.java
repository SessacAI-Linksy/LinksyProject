package app.labs.linksy.Repository;

import app.labs.linksy.Model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    List<Notification> findByUserId(String userId);

}
