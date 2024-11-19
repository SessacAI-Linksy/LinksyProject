package app.labs.linksy.Controller;

import app.labs.linksy.Model.Notification;
import app.labs.linksy.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket을 통해 알림을 전송하기 위한 SimpMessagingTemplate

    @Autowired
    private NotificationService notificationService; // 알림 저장 서비스

    // 알림 생성 및 실시간 전송
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        try {
            // 알림을 데이터베이스에 저장
            Notification savedNotification = notificationService.createNotification(notification);

            // WebSocket을 통해 알림 전송
            messagingTemplate.convertAndSend("/topic/notifications", savedNotification);

            // 성공적인 응답 반환
            return ResponseEntity.ok(savedNotification);
        } catch (Exception e) {
            // 오류 발생 시 적절한 오류 상태 반환fv
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/sendTestNotification")
    public ResponseEntity<String> sendTestNotification() {
        Notification notification = new Notification();
        notification.setUserId("user123");
        notification.setNotiType("LIKE");
        notification.setContent("사용자가 게시물에 좋아요를 눌렀습니다.");
        notification.setTargetUrl("http://example.com/post/1");

        // WebSocket을 통해 알림 전송
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return ResponseEntity.ok("Test notification sent successfully");
    }

    // 특정 사용자에게 실시간 알림 전송
    @PostMapping("/send/{userId}")
    @ResponseBody
    public ResponseEntity<String> sendNotificationToUser(@PathVariable String userId, @RequestBody Notification notification) {
        try {
            // 특정 사용자에게 WebSocket을 통해 알림 전송
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
            return ResponseEntity.ok("알림이 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("알림 전송에 실패했습니다.");
        }
    }

    @GetMapping("/notification")
    public String notification() {
        return "notification"; // 'notification.html'을 찾기 위해 이름만 반환
    }

}

