package app.labs.linksy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Hashtag를 JPA 엔티티로 표시
@Table(name = "HASHTAG") // 테이블 이름을 HASHTAG로 매핑
@Data // Getter, Setter, equals, hashCode, toString 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 자동 생성
public class Hashtag {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hashtag_seq")
	@SequenceGenerator(name = "hashtag_seq", sequenceName = "HASHTAG_SEQ", allocationSize = 1)
	@Column(name = "HASHTAG_ID")
	private Integer hashtagId;

	@Column(name = "HASHTAG", nullable = false, unique = true)
	private String hashtag;
}