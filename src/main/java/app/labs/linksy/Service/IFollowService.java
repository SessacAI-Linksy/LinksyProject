package app.labs.linksy.Service;

import java.util.List;

import app.labs.linksy.Model.Member;

public interface IFollowService {
	List<Member> getFollowings(String followerId);
}
