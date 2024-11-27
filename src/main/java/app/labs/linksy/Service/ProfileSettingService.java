package app.labs.linksy.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.labs.linksy.DAO.ProfileSettingRepository;
import app.labs.linksy.Model.Member;
import org.apache.ibatis.annotations.Param;

@Service
public class ProfileSettingService {

    @Autowired
    private ProfileSettingRepository profileSettingRepository;

    public void updateProfileImage(String userId, String userImg) {
        profileSettingRepository.updateProfileImage(userId, userImg);
    }

    public void updateUserIntroduce(String userId, String userIntroduce) {
        profileSettingRepository.updateUserIntroduce(userId, userIntroduce);
    }

    public void updateNickname(@Param("userId") String userId, @Param("nickname") String nickname) {
        profileSettingRepository.updateNickname(userId, nickname);
    }

    public int updatePassword(String userId, String currentPassword, String newPassword) {
        return profileSettingRepository.updatePassword(userId, currentPassword, newPassword);
    }

    public Member getMemberProfile(String userId) {
        return profileSettingRepository.getMemberProfile(userId);
    }
}
