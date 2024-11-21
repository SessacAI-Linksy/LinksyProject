package app.labs.linksy.Model;

import java.sql.Timestamp;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "COMMENT")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
	@SequenceGenerator(name = "comment_seq", sequenceName = "COMMENT_SEQ", allocationSize = 1)
	int commentId;

	@Column(name = "USER_ID", nullable = false)
	String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FEED_ID")
	Feed feed;

	@Column(name = "COMMENT_CONTENT", nullable = false)
	String commentContent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	Comment parentid;
	
	@Column(name = "COMMENT_TIME", nullable = false)
	Timestamp commentTime;
}
