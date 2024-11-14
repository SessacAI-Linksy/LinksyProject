package app.labs.linksy.Model;

import java.sql.Timestamp;

public class Comment {
	int commentId;
	String userId;
	int feedId;
	String commentContent;
	int parentId;
	Timestamp commentTime;
}
