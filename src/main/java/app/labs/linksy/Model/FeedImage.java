package app.labs.linksy.Model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Entity
@Table(name = "FEED_IMAGE")
public class FeedImage {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feed_image_seq_generator")
	@SequenceGenerator(name = "feed_image_seq_generator", sequenceName = "FEED_IMAGE_SEQ", allocationSize = 1)
	@Column(name = "IMAGE_ID")
	private int imageId;

	@ManyToOne
	@JoinColumn(name = "FEED_ID", nullable = false)
	private Feed feed;

	@Column(name = "IMG_NAME")
	private String imgName;
}
