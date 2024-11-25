package app.labs.linksy.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/settings")
public class ProfileController {


    @PostMapping("/save-nickname")
    public ResponseEntity<String> saveNickname(@RequestBody Map<String, String> request) {
        String newNickname = request.get("nickname");

        if (newNickname == null || newNickname.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("닉네임이 유효하지 않습니다.");
        }

        System.out.println("닉네임 변경 요청: " + newNickname);
        // 추가: 실제 닉네임 저장 로직 (DB 혹은 서비스 호출)
        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }

    @PostMapping("/save-password")
    public ResponseEntity<String> savePassword(@RequestBody Map<String, String> request) {
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        if (currentPassword == null || newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("비밀번호가 유효하지 않습니다.");
        }

        System.out.println("비밀번호 변경 요청: " + newPassword);
        // 추가: 실제 비밀번호 변경 로직 (DB 혹은 서비스 호출)
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @PostMapping("/save-profile")
    public ResponseEntity<String> saveProfile(
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("bio") String bio) {

        String uploadDir = "C:/upload/profileImages/";
        File uploadFolder = new File(uploadDir);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String profileImagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String fileName = profileImage.getOriginalFilename();
                File destination = new File(uploadDir + fileName);
                profileImage.transferTo(destination);
                profileImagePath = "/profileImages/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body("프로필 이미지 저장 중 오류 발생");
            }
        }

        System.out.println("저장된 프로필 이미지 경로: " + profileImagePath);
        System.out.println("저장된 소개글: " + bio);

        // 추가: 실제 프로필 저장 로직 (DB 혹은 서비스 호출)

        return ResponseEntity.ok("프로필이 성공적으로 저장되었습니다.");
    }
}
