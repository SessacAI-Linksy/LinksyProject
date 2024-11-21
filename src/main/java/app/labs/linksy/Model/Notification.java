package app.labs.linksy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "NOTIFICATION")
@Data // Getter, Setter, equals, hashCode, toString 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 자동 생성
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
	@SequenceGenerator(name = "notification_seq", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
	@Column(name = "NOTIFICATION_ID")
	private int notificationId;

	@Column(name = "USER_ID", nullable = false)
	private String userId;

	@Column(name = "NOTI_TYPE", nullable = false)
	private String notiType; // 좋아요, 댓글, 대댓글 등

	@Column(name = "TARGET_URL")
	private String targetUrl;

	@Column(name = "CONTENT", nullable = false)
	private String content;

	// 생성 시간 필드 추가
	//@Column(name = "CREATED_AT", nullable = false)
	//private Timestamp createdAt;
}
