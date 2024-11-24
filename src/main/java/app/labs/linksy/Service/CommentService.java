package app.labs.linksy.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.labs.linksy.DAO.CommentRepository;
import app.labs.linksy.Model.Comment;

@Service
public class CommentService implements ICommentService {
	 @Autowired
	 private CommentRepository commentRepository;

	 public List<Comment> getCommentsByFeedId(int feedId) {
	     return commentRepository.getCommentsByFeedId(feedId);
	 }
	 
	 // 댓글 추가하기
	 public void addComment(Comment comment) {
	     commentRepository.insertComment(comment);
	 }

	
}
