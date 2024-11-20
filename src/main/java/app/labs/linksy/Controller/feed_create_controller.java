package app.labs.linksy.Controller;

import app.labs.linksy.Model.Feed;
import app.labs.linksy.Service.FeedCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/feed")
public class feed_create_controller {

    @Autowired
    private FeedCreateService feedService;

    // Create Feed Page - 게시물 작성 페이지를 열기 위한 메서드
    @GetMapping("/create")
    public String createFeedPage() {
        return "feed-create"; // 게시물 작성 페이지 (feed-create.html)를 반환
    }

    // Create Feed - JSON 데이터를 받는 게시물 생성 메서드
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createFeed(@RequestParam("content") String content,
                                        @RequestParam(value = "hashtags", required = false) List<String> hashtags,
                                        @RequestParam(value = "images", required = false) MultipartFile[] images) {
        try {
            // 로그로 받은 데이터 확인
            System.out.println("받은 콘텐츠: " + content);
            if (hashtags != null) {
                System.out.println("받은 해시태그들: " + hashtags);
            }
            if (images != null) {
                System.out.println("받은 이미지 개수: " + images.length);
                for (MultipartFile image : images) {
                    System.out.println("받은 이미지 이름: " + image.getOriginalFilename());
                }
            }

            // 피드 생성 로직 호출 (이미지 업로드 로직 포함)
            Feed createdFeed = feedService.createFeed(content, images, hashtags);

            return ResponseEntity.ok(createdFeed);  // 성공적으로 생성된 피드를 반환합니다.
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 중 오류 발생");
        }
    }

    // Success Page - 게시물 생성 성공 시 보여주는 페이지
    @GetMapping("/success")
    public String successPage() {
        return "feed-create-success"; // 성공 페이지 (feed-create-success.html)를 반환
    }

    // 게시물 수정 페이지로 이동하는 메서드
    @GetMapping("/edit/{id}")
    public String editFeedPage(@PathVariable("id") int id, Model model) {
        Feed feed = feedService.getFeedById(id);
        model.addAttribute("feed", feed);
        return "feed-modify";
    }

    // 게시물 ID로 게시물을 조회하는 메서드
    @GetMapping("/{id}")
    public ResponseEntity<Feed> getFeedById(@PathVariable("id") int id) {
        try {
            Feed feed = feedService.getFeedById(id); // FeedService를 통해 데이터베이스에서 피드를 가져옴
            return ResponseEntity.ok(feed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 피드를 찾을 수 없으면 404 응답을 반환
        }
    }

    // 게시물 수정 요청을 처리하는 메서드
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editFeed(@PathVariable("id") int id, @RequestBody Feed updatedFeed) {
        try {
            // 게시물 내용 및 해시태그 수정
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
            feedService.deleteFeedById(id);
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 삭제 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testExtractHashtags() {
        feedService.testExtractHashtags();
        return ResponseEntity.ok("Hashtags test completed, check logs.");
    }


    // FeedRequest 클래스 정의
    public static class FeedRequest {
        private String content;
        private List<String> hashtags;
        private String imageUrl;

        // Getters and Setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<String> hashtags) {
            this.hashtags = hashtags;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
