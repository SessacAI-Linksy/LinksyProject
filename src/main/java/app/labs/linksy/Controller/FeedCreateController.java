package app.labs.linksy.Controller;

import app.labs.linksy.Model.Feed;
import app.labs.linksy.Service.FeedCreateService;
import app.labs.linksy.Service.UserService;
import app.labs.linksy.Util.HashtagExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@Controller
public class FeedCreateController {

    @Autowired
    private FeedCreateService feedCreateService;

    @Autowired
    private UserService userService;

    private static final Logger logger = Logger.getLogger(FeedCreateController.class.getName());

    // 변경된 업로드 경로를 가져옴
    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    // 게시물 생성 페이지 매핑
    @GetMapping("/feed/create")
    public String createFeedPage() {
        return "feed-create"; // feed-create.html 파일과 매핑
    }

    // 게시물 수정 페이지 매핑
    @GetMapping("/feed/edit/{feedId}")
    public String editFeedPage(@PathVariable("feedId") int feedId, Model model) {
        Feed feed = feedCreateService.getFeedById(feedId);
        if (feed == null) {
            logger.warning("Feed with ID " + feedId + " not found.");
            return "redirect:/error";
        }

        // 해당 피드에 연결된 이미지 목록과 해시태그 목록을 가져오기
        List<String> imageNames = feedCreateService.getFeedImages(feedId);

        // Model에 데이터 추가
        model.addAttribute("feed", feed);
        model.addAttribute("imageNames", imageNames);

        return "feed-modify"; // feed-modify.html 파일과 매핑
    }

    @GetMapping("/feed/view/{feedId}")
    @ResponseBody
    public Map<String, Object> getFeedByIdWithImages(@PathVariable("feedId") int feedId) {
        Feed feed = feedCreateService.getFeedById(feedId);
        if (feed == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feed not found");
        }

        List<String> feedImages = feedCreateService.getFeedImages(feedId);

        Map<String, Object> response = new HashMap<>();
        response.put("feedId", feed.getFeedId());
        response.put("userId", feed.getUserId());
        response.put("feedContent", feed.getFeedContent());
        response.put("feedTime", feed.getFeedTime());
        response.put("likeAmount", feed.getLikeAmount());
        response.put("feedImages", feedImages);

        return response;
    }



    // 게시물 수정 처리
    @PutMapping("/feed/edit/{feedId}")
    @ResponseBody
    public String updateFeedById(@PathVariable("feedId") int feedId,
                                 @RequestBody Map<String, String> payload) {
        String feedContent = payload.get("feedContent");
        logger.info("Received request to update feed with ID: " + feedId);
        try {
            // 피드 업데이트 및 데이터 저장
            Feed feed = new Feed();
            feed.setFeedId(feedId);
            feed.setFeedContent(feedContent);

            feedCreateService.updateFeedContent(feed); // 피드 내용만 업데이트
            logger.info("Feed successfully updated with ID: " + feedId);

            return "redirect:/modifyFeedSuccess";
        } catch (Exception e) {
            logger.severe("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating feed");
        }
    }

    // 게시물 삭제 처리
    @DeleteMapping("/feed/delete/{feedId}")
    public String deleteFeed(@PathVariable("feedId") int feedId) {
        logger.info("Received request to delete feed with ID: " + feedId);
        try {
            feedCreateService.deleteFeed(feedId);
            logger.info("Feed successfully deleted with ID: " + feedId);
            return "redirect:/deleteFeedSuccess";
        } catch (Exception e) {
            logger.severe("Error occurred while deleting feed: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    // 게시물 삭제 성공 페이지 매핑
    @GetMapping("/deleteFeedSuccess")
    public String deleteFeedSuccessPage() {
        return "feed-delete-success"; // feed-delete-success.html 파일과 매핑
    }

    @PostMapping("/feed/create")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createFeed(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam("feedContent") String feedContent,
            @RequestParam("images") List<MultipartFile> imageFiles) {
        logger.info("Received request to create feed for user: " + (userId != null ? userId : "anonymous"));

        if (userId == null || !userService.existsByUserId(userId)) {
            userId = "anonymous";
            userService.createAnonymousUserIfNotExist();
        }

        try {
            List<String> hashtags = HashtagExtractor.extractHashtags(feedContent);
            List<String> imageNames = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                    String filePath = uploadDir + File.separator + uniqueFileName;
                    File destFile = new File(filePath);
                    if (!destFile.getParentFile().exists()) {
                        destFile.getParentFile().mkdirs();
                    }
                    imageFile.transferTo(destFile);
                    imageNames.add(uniqueFileName);
                }
            }

            Feed feed = new Feed();
            feed.setUserId(userId);
            feed.setFeedContent(feedContent);
            feed.setLikeAmount(0);
            feedCreateService.createFeed(feed, hashtags, imageNames);

            // **수정된 JSON 응답**
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("redirectUrl", "/feed/createSuccess"); // 리다이렉트 경로를 프론트엔드에 전달
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.severe("Error saving files: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "File save error"));
        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Unexpected error"));
        }
    }


    @PostMapping("/feed/edit/{feedId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editFeed(
            @PathVariable("feedId") int feedId,
            @RequestBody Map<String, String> payload) {
        String feedContent = payload.get("feedContent");
        boolean success = feedCreateService.updateFeedContent(feedId, feedContent);
        if (success) {
            return ResponseEntity.ok(Map.of("status", "success", "redirectUrl", "/modifyFeedSuccess"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Failed to update feed"));
        }
    }

    // 게시물 수정 성공 페이지 매핑
    @GetMapping("/modifyFeedSuccess")
    public String modifyFeedSuccessPage() {
        return "feed-modify-success"; // feed-modify-success.html 파일과 매핑
    }

    @GetMapping("/feed/createSuccess")
    public String showCreateSuccessPage() {
        return "feed-create-success"; // 파일 이름만 반환
    }

    // 게시물 생성 페이지 매핑
    @GetMapping("/feed/likeslisst")
    public String likeslistPage() {
        return "likeslist"; // feed-create.html 파일과 매핑
    }

}
