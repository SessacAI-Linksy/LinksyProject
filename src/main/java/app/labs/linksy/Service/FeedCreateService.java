package app.labs.linksy.Service;

import app.labs.linksy.Model.Feed;
import app.labs.linksy.Model.FeedHashtag;
import app.labs.linksy.Model.FeedImage;
import app.labs.linksy.Model.Hashtag;
import app.labs.linksy.Repository.FeedHashtagRepository;
import app.labs.linksy.Repository.FeedImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.labs.linksy.Repository.feed_create_repository;
import app.labs.linksy.Repository.FeedHashtagRepository;
import app.labs.linksy.Repository.hashtag_repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class FeedCreateService {

    @Autowired
    private feed_create_repository feedCreateRepository; // Repository를 자동 주입하여 DB와의 상호작용을 처리

    @Autowired
    private FeedHashtagRepository feedHashtagRepository;

    @Autowired
    private hashtag_repository hashtagRepository;

    @Autowired
    private FeedImageRepository feedImageRepository; // 새로운 필드 추가하여 의존성 주입

    // Create Feed
    public Feed createFeed(String content, MultipartFile image) throws IOException {
        // 새로운 Feed 객체를 생성
        Feed newFeed = new Feed();
        newFeed.setFeedContent(content); // Setter 사용하여 feedContent 설정
        newFeed.setFeedTime(new Timestamp(System.currentTimeMillis())); // 현재 시간 설정

        // Feed를 DB에 저장
        Feed savedFeed = feedCreateRepository.save(newFeed);

        // 이미지가 제공되었는지 체크
        if (image != null && !image.isEmpty()) {
            // 이미지 파일명을 UUID로 설정하여 충돌 방지
            String originalFilename = image.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            if (originalFilename != null) {
                fileName += "_" + originalFilename;
            } else {
                fileName += "_unknown.png"; // 기본 파일명 설정 (원래 파일명이 없는 경우)
            }

            String uploadDir = "uploads/"; // 이미지를 저장할 디렉토리
            File dir = new File(uploadDir);

            // 저장 디렉토리 없으면 생성
            if (!dir.exists()) {
                boolean isCreated = dir.mkdirs();
                if (!isCreated) {
                    throw new IOException("Failed to create directory: " + uploadDir);
                }
            }

            // 이미지 파일을 저장
            File uploadedFile = new File(Paths.get(uploadDir, fileName).toString());
            image.transferTo(uploadedFile);

            // FeedImage 객체를 생성하여 이미지 정보를 저장
            FeedImage feedImage = new FeedImage();
            feedImage.setFeed(savedFeed); // Setter를 사용하여 feedId 설정
            feedImage.setImgName(uploadDir + fileName); // 이미지 이름 설정
            feedImageRepository.save(feedImage); // 이미지 정보를 DB에 저장

            // 이미지 정보를 DB에 저장
            feedImageRepository.save(feedImage);
        }

        // 해시태그 저장
        List<String> hashtags = extractHashtags(content);
        for (String tag : hashtags) {
            // 'HASHTAG' 테이블에 해시태그 저장
            Hashtag hashtag = hashtagRepository.findByHashtag(tag);
            if (hashtag == null) {
                hashtag = new Hashtag();
                hashtag.setHashtag(tag);
                hashtag = hashtagRepository.save(hashtag);
            }

            // 'FEED_HASHTAG' 테이블에 Feed와 Hashtag 관계 저장
            saveFeedHashtag(savedFeed.getFeedId(), hashtag.getHashtagId());
        }

        // 생성된 Feed 반환
        return savedFeed;
    }

    // 해시태그와 피드 관계 저장
    public void saveFeedHashtag(Integer feedId, Integer hashtagId) {
        FeedHashtag feedHashtag = new FeedHashtag();
        feedHashtag.setFeedId(feedId);
        feedHashtag.setHashtagId(hashtagId);
        feedHashtagRepository.save(feedHashtag);
    }

    // 해시태그 추출 메서드 (간단한 예시)
    private List<String> extractHashtags(String content) {
        // 내용에서 해시태그를 추출하는 로직 구현
        // 예를 들어, '#'으로 시작하는 단어들을 추출할 수 있습니다.
        return List.of(); // 추출된 해시태그 리스트 반환 (구현 필요)
    }
}
