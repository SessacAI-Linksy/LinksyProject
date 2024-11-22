package app.labs.linksy.DAO;

import org.apache.ibatis.annotations.Mapper;

import app.labs.linksy.Model.Member;

@Mapper
public interface LoginRepository {
    void login(Member member);
    Member findByUserId(String userId);
}
