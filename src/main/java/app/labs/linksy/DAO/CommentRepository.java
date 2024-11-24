package app.labs.linksy.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.labs.linksy.Model.Comment;

@Mapper
public interface CommentRepository {
    List<Comment> getCommentsByFeedId(@Param("feedId") int feedId);
    void insertComment(Comment comment);
}
