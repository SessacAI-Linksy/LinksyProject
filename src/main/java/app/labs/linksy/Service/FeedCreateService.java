package app.labs.linksy.Service;

import app.labs.linksy.Model.Feed;
import app.labs.linksy.Model.FeedHashtag;
import app.labs.linksy.Model.FeedImage;
import app.labs.linksy.Model.Hashtag;
import app.labs.linksy.Repository.FeedHashtagRepository;
import app.labs.linksy.Repository.feed_create_repository;
import app.labs.linksy.Repository.FeedImageRepository;
import app.labs.linksy.Repository.hashtag_repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FeedCreateService {

    private static final Logger logger = LoggerFactory.getLogger(FeedCreateService.class);

    @Autowired
    private feed_create_repository feedRepository;

    @Autowired
    private FeedHashtagRepository feedHashRepository;

    @Autowired
    private hashtag_repository hashtagRepository;

    @Autowired
    private FeedImageRepository feedImageRepository;

    // 게시물 생성 메서드
    @Transactional
    public Feed createFeed(String content, MultipartFile image, List<String> hashtags) throws IOException {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        Feed newFeed = new Feed();
        newFeed.setFeedContent(content);
        newFeed.setFeedTime(new Timestamp(System.currentTimeMillis()));

        Feed savedFeed = feedRepository.save(newFeed);
        if (savedFeed == null) {
            throw new RuntimeException("Failed to save feed");
        }

        // 이미지가 제공되었는지 체크
        if (image != null && !image.isEmpty()) {
            String originalFilename = image.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            if (originalFilename != null) {
                fileName += "_" + originalFilename;
            } else {
                fileName += "_unknown.png";
            }

            String uploadDir = "uploads/";
            File dir = new File(uploadDir);

            if (!dir.exists()) {
                boolean isCreated = dir.mkdirs();
                if (!isCreated) {
                    throw new IOException("Failed to create directory: " + uploadDir);
                }
            }

            File uploadedFile = new File(Paths.get(uploadDir, fileName).toString());
            image.transferTo(uploadedFile);

            FeedImage feedImage = new FeedImage();
            feedImage.setFeed(savedFeed);
            feedImage.setImgName(uploadDir + fileName);
            feedImageRepository.save(feedImage);
        }

        // 해시태그 저장: 추출된 해시태그와 사용자가 입력한 해시태그를 모두 사용
        Set<String> allHashtags = new HashSet<>(hashtags);
        allHashtags.addAll(extractHashtags(content));

        for (String tag : allHashtags) {
            Hashtag hashtag = hashtagRepository.findByHashtag(tag);
            if (hashtag == null) {
                hashtag = new Hashtag();
                hashtag.setHashtag(tag);
                hashtag = hashtagRepository.save(hashtag);
                logger.info("새로운 해시태그 저장: {}", hashtag.getHashtag());
            } else {
                logger.info("기존 해시태그 사용: {}", hashtag.getHashtag());
            }

            saveFeedHashtag(savedFeed, hashtag);
        }

        return savedFeed;
    }

    // 해시태그와 피드 관계 저장
    public void saveFeedHashtag(Feed feed, Hashtag hashtag) {
        int feedId = feed.getFeedId();
        int hashtagId = hashtag.getHashtagId();

        // 중복 관계가 있는지 확인
        Optional<FeedHashtag> existingRelationship = feedHashRepository.findByFeedIdAndHashtagId(feedId, hashtagId);
        if (existingRelationship.isEmpty()) {
            FeedHashtag feedHashtag = new FeedHashtag(feedId, hashtagId);
            feedHashRepository.save(feedHashtag);
            logger.info("피드와 해시태그 관계 저장: Feed ID = {}, Hashtag ID = {}", feedId, hashtagId);
        } else {
            logger.info("이미 존재하는 피드와 해시태그 관계: Feed ID = {}, Hashtag ID = {}", feedId, hashtagId);
        }
    }

    // 해시태그 추출 메서드
    private List<String> extractHashtags(String content) {
        if (content == null || content.isEmpty()) {
            logger.debug("입력된 content가 비어있습니다.");
            return List.of();
        }

        logger.debug("입력된 content: {}", content);

        List<String> hashtags = new ArrayList<>();
        // 한글, 영문, 숫자, 언더스코어를 포함하는 패턴
        Pattern pattern = Pattern.compile("#[ㄱ-ㅎ가-힣a-zA-Z0-9_]+");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String hashtag = matcher.group();
            hashtags.add(hashtag);
            logger.debug("추출된 해시태그: {}", hashtag);
        }

        logger.info("전체 추출된 해시태그 목록: {}", hashtags);

        return hashtags.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    // 게시물 ID로 게시물 조회 메서드
    public Feed getFeedById(int id) {
        return feedRepository.findById(id).orElseThrow(() -> new RuntimeException("Feed not found"));
    }
    // 테스트용 메서드 추가
    public void testExtractHashtags() {
        String testContent = "테스트 #안녕하세요 #Hello #테스트123 #한글_태그 #English_Tag";
        logger.info("==== 해시태그 추출 테스트 시작 ====");
        logger.info("테스트 문자열: {}", testContent);
        List<String> hashtags = extractHashtags(testContent);
        logger.info("추출 결과: {}", hashtags);
        logger.info("==== 해시태그 추출 테스트 종료 ====");
    }


    // 게시물 수정 메서드
    @Transactional
    public void updateFeed(int id, Feed updatedFeed) {
        Feed existingFeed = getFeedById(id);
        existingFeed.setFeedContent(updatedFeed.getFeedContent());
        feedRepository.save(existingFeed);
    }

    // 게시물 삭제 메서드
    @Transactional
    public void deleteFeedById(int id) {
        feedRepository.deleteById(id);
    }
}
