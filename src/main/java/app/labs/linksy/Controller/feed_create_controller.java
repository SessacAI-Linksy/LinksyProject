package app.labs.linksy.Controller;

import app.labs.linksy.Model.Feed;
import app.labs.linksy.Service.FeedCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.IOException;

@Controller // @Controller 어노테이션 추가
@RequestMapping("/feed")
public class feed_create_controller {

    @Autowired
    private FeedCreateService feedService; // FeedService를 자동 주입하여 게시물 생성 관련 비즈니스 로직을 처리

    // Create Feed - 게시물 생성 메서드
    @PostMapping("/create")
    public String createFeed(@RequestParam("content") String content, // 게시물 내용 파라미터
                             @RequestParam(value = "image", required = false) MultipartFile image, // 이미지 파일 파라미터 (선택사항)
                             Model model) { // Model 객체를 통해 뷰에 데이터를 전달
        try {
            Feed createdFeed = feedService.createFeed(content, image); // FeedService를 호출해 게시물을 생성하고 반환
            model.addAttribute("feed", createdFeed); // 생성된 게시물을 모델에 추가하여 뷰로 전달
            return "feed-create-success";  // 리다이렉트를 하지 않고, 바로 뷰 이름을 반환
        } catch (IOException e) { // 이미지 업로드 시 발생할 수 있는 예외 처리
            throw new RuntimeException("Error uploading image", e); // 예외 발생 시 런타임 예외로 처리
        }
    }

    // Success Page - 게시물 생성 성공 시 보여주는 페이지
    @GetMapping("/success")
    public String successPage() {
        return "feed-create-success"; // 성공 페이지를 보여주는 뷰 이름 반환 (feed-create-success로 수정하여 templates와 일치)
    }

    // 게시물 수정 페이지로 이동하는 메서드
    @GetMapping("/edit/{id}")
    public String editFeedPage(@PathVariable("id") int id, Model model) {
        Feed feed = feedService.getFeedById(id); // 서비스로부터 피드를 가져옴
        model.addAttribute("feed", feed);
        return "feed-edit";  // 수정 페이지로 이동 (feed-edit.html)
    }

    // 게시물 수정 요청을 처리하는 메서드
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editFeed(@PathVariable("id") int id, @RequestBody Feed updatedFeed) {
        try {
            // 게시물 내용만 수정합니다 (이미지는 수정하지 않음)
            feedService.updateFeed(id, updatedFeed);
            return ResponseEntity.ok("게시물이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 수정 중 오류가 발생했습니다.");
        }
    }

    // 게시물 삭제 요청을 처리하는 메서드
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFeed(@PathVariable("id") int id) {
        try {
            feedService.deleteFeedById(id); // 피드 삭제
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 삭제 중 오류가 발생했습니다.");
        }
    }

}
