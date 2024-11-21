package app.labs.linksy.Model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CommentLike {
	int userCommentLikeId;
	String userId;
	int commentId;
}
