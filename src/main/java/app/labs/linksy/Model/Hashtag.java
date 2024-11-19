package app.labs.linksy.Model;

import jakarta.persistence.*;

@Entity // Hashtag를 JPA 엔티티로 표시
@Table(name = "HASHTAG") // 테이블 이름을 HASHTAG로 매핑
public class Hashtag {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hashtag_seq")
	@SequenceGenerator(name = "hashtag_seq", sequenceName = "HASHTAG_SEQ", allocationSize = 1)
	@Column(name = "HASHTAG_ID")
	Integer hashtagId;

	@Column(name = "HASHTAG", nullable = false, unique = true)
	String hashtag;

	// 기본 생성자
	public Hashtag() {
	}

	// 생성자
	public Hashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	// Getter and Setter for hashtagId
	public int getHashtagId() {
		return hashtagId;
	}

	public void setHashtagId(int hashtagId) {
		this.hashtagId = hashtagId;
	}

	// Getter and Setter for hashtag
	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
}
