package app.labs.linksy.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data // Lombok의 @Data 어노테이션을 사용하여 모든 Getter, Setter, toString, equals, hashCode 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 생성
@Builder // 빌더 패턴 사용 가능
@Entity // Feed를 JPA 엔티티로 표시
@Table(name = "FEED") // 테이블 이름을 FEED로 매핑
public class Feed {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_seq_generator")
	@SequenceGenerator(name = "feed_seq_generator", sequenceName = "FEED_SEQ", allocationSize = 1)
	@Column(name = "FEED_ID")
	private int feedId;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "FEED_CONTENT")
	private String feedContent;

	@Column(name = "FEED_TIME")
	private Timestamp feedTime;

	@Column(name = "LIKE_AMOUNT")
	private int likeAmount;

	// Feed와 FeedImage의 관계 설정
	@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference // 순환 참조 방지
	private List<FeedImage> feedImages;
}
