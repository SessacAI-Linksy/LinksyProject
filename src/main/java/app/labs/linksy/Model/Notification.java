package app.labs.linksy.Model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "NOTIFICATION")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
	@SequenceGenerator(name = "notification_seq", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
	@Column(name = "NOTIFICATION_ID")
	int notificationId;

	@Column(name = "USER_ID", nullable = false)
	String userId;

	@Column(name = "NOTI_TYPE", nullable = false)
	String notiType;

	@Column(name = "TARGET_URL")
	String targetUrl;

	@Column(name = "CONTENT", nullable = false)
	String content;
}
