package app.labs.linksy.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity // Hashtag를 JPA 엔티티로 표시
@Table(name = "HASHTAG") // 테이블 이름을 HASHTAG로 매핑
public class Hashtag {

	@Id // 기본 키 필드를 지정합니다.
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정 (필요시 변경 가능)
	@Column(name = "HASHTAG_ID")
	int hashtagId;

	@Column(name = "HASHTAG", nullable = false)
	String hashtag;

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
