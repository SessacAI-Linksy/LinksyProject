package app.labs.linksy.Model;

import jakarta.persistence.*;

@Entity // JPA가 이 클래스를 엔티티로 인식하도록 설정
@Table(name = "FEED_IMAGE") // 데이터베이스의 테이블 이름을 FEED_IMAGE로 설정
public class FeedImage {

	@Id // 기본 키 지정
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_image_seq_generator")
	@SequenceGenerator(name = "feed_image_seq_generator", sequenceName = "FEED_IMAGE_SEQ", allocationSize = 1) // 시퀀스를 사용해 IMAGE_ID 자동 생성
	@Column(name = "IMAGE_ID")
	int imageId;

	@ManyToOne
	@JoinColumn(name = "FEED_ID", nullable = false)
	Feed feed;

	@Column(name = "IMG_NAME")
	String imgName;

	// Getters and Setters
	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public Feed getFeed() {
		return feed;
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
}
