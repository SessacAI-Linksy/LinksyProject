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
    private NotificationService notificationService; // 알림 저장 서비스

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket을 통한 알림 전송

    // 알림 생성 및 실시간 전송
    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(@RequestParam String userId,
                                                           @RequestParam String notiType,
                                                           @RequestParam String targetUrl) {
        try {
            // 알림을 데이터베이스에 저장 (string 매개변수들을 사용하여 호출)
            Notification savedNotification = notificationService.createNotification(userId, notiType, targetUrl);

            // WebSocket을 통해 알림 전송
            messagingTemplate.convertAndSend("/topic/notifications", savedNotification);

            // 성공적인 응답 반환
            return ResponseEntity.ok(savedNotification);
        } catch (Exception e) {
            // 오류 발생 시 적절한 오류 상태 반환
            e.printStackTrace(); // 에러 로그 출력 추가
            return ResponseEntity.status(500).body(null);
        }
    }

    // 테스트 알림 생성 및 실시간 전송
    @GetMapping("/sendTestNotification")
    public ResponseEntity<String> sendTestNotification() {
        try {
            Notification savedNotification = notificationService.createNotification("user123", "LIKE", "http://example.com/post/1");

            // WebSocket 전송
            messagingTemplate.convertAndSend("/topic/notifications", savedNotification);
            return ResponseEntity.ok("Test notification sent successfully");
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력 추가
            return ResponseEntity.status(500).body("Test notification failed");
        }
    }

    // 특정 사용자에게 실시간 알림 전송
    @PostMapping("/send/{userId}")
    public ResponseEntity<String> sendNotificationToUser(@PathVariable String userId, @RequestParam String notiType, @RequestParam String targetUrl) {
        try {
            Notification savedNotification = notificationService.createNotification(userId, notiType, targetUrl);
            // 특정 사용자에게 WebSocket을 통해 알림 전송
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, savedNotification);
            return ResponseEntity.ok("알림이 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력 추가
            return ResponseEntity.status(500).body("알림 전송에 실패했습니다.");
        }
    }

    // 알림 페이지를 반환하는 엔드포인트
    @GetMapping("/notification")
    public String notification() {
        return "notification"; // ViewResolver를 통해 'notification.html' 파일을 찾도록 변경
    }
}