package app.labs.linksy.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "NOTIFICATION")
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

	//@Column(name = "CREATED_AT", nullable = false)
	//private Timestamp createdAt; // 알림 생성 시간 필드 추가

	// 기본 생성자
	public Notification() {
	}

	// Getters and Setters
	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNotiType() {
		return notiType;
	}

	public void setNotiType(String notiType) {
		this.notiType = notiType;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
