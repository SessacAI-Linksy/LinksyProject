package app.labs.linksy.DAO;

import app.labs.linksy.Model.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileDAO {
    Profile getProfile(@Param("id") String id);
    void saveProfile(Profile profile);
    void updateNickname(@Param("userId") String userId, @Param("nickname") String nickname);
}
