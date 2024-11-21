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

    public Notification createNotification(String userId, String notiType, String targetUrl) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotiType(notiType);
        notification.setTargetUrl(targetUrl);

        // 알림 유형에 따른 내용 설정
        String content = switch (notiType) {
            case "LIKE" -> "사용자 " + userId + "님이 당신의 게시물을 좋아합니다.";
            case "COMMENT" -> "사용자 " + userId + "님이 게시물에 댓글을 남겼습니다.";
            case "FOLLOW" -> "사용자 " + userId + "님이 당신을 팔로우하기 시작했습니다.";
            default -> "알 수 없는 알림 유형입니다.";
        };
        notification.setContent(content);

        // 데이터베이스에 알림 저장
        return notificationRepository.save(notification);
    }

    // 사용자별 알림 조회
    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId);
    }
}
