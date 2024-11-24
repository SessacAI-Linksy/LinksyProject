package app.labs.linksy.Service;

import java.util.List;

import app.labs.linksy.Model.Comment;

public interface ICommentService {
	List<Comment> getCommentsByFeedId(int feedId);
	 public void addComment(Comment comment);
}
