package app.labs.linksy.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // JPA가 이 클래스를 엔티티로 인식하도록 설정
@Table(name = "FEED_IMAGE") // 데이터베이스의 테이블 이름을 FEED_IMAGE로 설정
@Data // Getter, Setter, equals, hashCode, toString 메서드 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 자동 생성
public class FeedImage {

	@Id // 기본 키 지정
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_image_seq_generator")
	@SequenceGenerator(name = "feed_image_seq_generator", sequenceName = "FEED_IMAGE_SEQ", allocationSize = 1) // 시퀀스를 사용해 IMAGE_ID 자동 생성
	@Column(name = "IMAGE_ID")
	private int imageId;

	@ManyToOne
	@JoinColumn(name = "FEED_ID", nullable = false)
	@JsonBackReference // 순환 참조 방지 어노테이션 추가
	private Feed feed;

	@Column(name = "IMG_NAME")
	private String imgName;
}
