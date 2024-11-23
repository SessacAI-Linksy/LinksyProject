package app.labs.linksy.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import app.labs.linksy.Model.Member;

@Mapper
public interface FollowRepository {
	 // 팔로우한 사용자의 정보를 가져오기
    List<Member> getFollowings(@Param("followerId") String followerId);
}
