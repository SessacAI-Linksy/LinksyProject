package app.labs.linksy.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.labs.linksy.DAO.FollowRepository;
import app.labs.linksy.Model.Member;

@Service
public class FollowService implements IFollowService {
	@Autowired
    private FollowRepository followrepository;
    
    // 팔로우한 사용자 정보 가져오기
    public List<Member> getFollowings(String followerId) {
        return followrepository.getFollowings(followerId);
    }
}
