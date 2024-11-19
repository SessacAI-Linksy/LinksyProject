package app.labs.linksy.Service;

import app.labs.linksy.Model.Notification;
import app.labs.linksy.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 알림 생성
    public Notification createNotification(Notification notification) {
        // 알림 생성 시간 필드가 필요 없다면 이 부분을 제거
        return notificationRepository.save(notification);
    }

    // 사용자별 알림 조회
    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId);
    }
}
