package app.labs.linksy.Model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
@Table(name = "HASHTAG")
public class Hashtag {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hashtag_seq")
	@SequenceGenerator(name = "hashtag_seq", sequenceName = "HASHTAG_SEQ", allocationSize = 1)
	@Column(name = "HASHTAG_ID")
	int hashtagId;

	@Column(name = "HASHTAG", nullable = false, unique = true)
	String hashtag;

	// 기본 생성자
	public Hashtag() {
	}

	// 생성자
	public Hashtag(String hashtag) {
		this.hashtag = hashtag;
	}
}

