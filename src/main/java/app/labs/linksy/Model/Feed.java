package app.labs.linksy.Model;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "FEED")
public class Feed {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_seq_generator")
	@SequenceGenerator(name = "feed_seq_generator", sequenceName = "FEED_SEQ", allocationSize = 1)
	@Column(name = "FEED_ID")
	int feedId;

	@Column(name = "USER_ID", insertable=false, updatable=false)
	String userId;

	@Column(name = "FEED_CONTENT")
	String feedContent;

	@Column(name = "FEED_TIME")
	Timestamp feedTime;

	@Column(name = "LIKE_AMOUNT")
	int likeAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonIgnore
	Member member;
	
	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
	List<FeedImage> feedimages;

	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Comment> comments;

	@Column(name = "COMMENT_COUNT")
	@Transient
	int commentCount;
}


