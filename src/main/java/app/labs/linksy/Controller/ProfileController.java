package app.labs.linksy.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import app.labs.linksy.Service.ProfileSettingService;
import app.labs.linksy.Service.HttpSessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/settings")
public class ProfileController {

    @Autowired
    private ProfileSettingService profileSettingService;

    @Autowired
    private HttpSessionService httpSessionService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    @PostMapping("/save-nickname")
    public ResponseEntity<String> saveNickname(@RequestParam("nickname") String nickname,
                                                HttpSession session) {
        
        if (nickname == null || nickname.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("닉네임이 유효하지 않습니다.");
        }

        System.out.println("닉네임 변경 요청: " + nickname);
        
        String userId = httpSessionService.sessionConfirm(session);
        profileSettingService.updateNickname(userId, nickname);

        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }

    @PostMapping("/save-password")
    public ResponseEntity<String> savePassword(@RequestBody Map<String, String> request,
                                                HttpSession session) {
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        if (currentPassword == null || newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("비밀번호가 유효하지 않습니다.");
        }

        System.out.println("비밀번호 변경 요청: " + newPassword);

        String userId = httpSessionService.sessionConfirm(session);
        profileSettingService.updatePassword(userId, currentPassword, newPassword);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @PostMapping("/save-profile")
    public ResponseEntity<String> saveProfile(
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("userIntroduce") String userIntroduce,
            HttpSession session) {

        String profileImagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String fileName = profileImage.getOriginalFilename();
                File destination = new File(uploadDir + "/" + fileName);
                profileImage.transferTo(destination);
                profileImagePath = fileName;  // DB에는 파일명만 저장
                System.out.println("저장된 프로필 이미지 경로: " + profileImagePath);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("프로필 이미지 저장 중 오류 발생");
            }
        }

        String userId = httpSessionService.sessionConfirm(session);
        profileSettingService.updateProfileImage(userId, profileImagePath);
        profileSettingService.updateUserIntroduce(userId, userIntroduce);

        return ResponseEntity.ok("프로필이 성공적으로 저장되었습니다.");
    }
}
