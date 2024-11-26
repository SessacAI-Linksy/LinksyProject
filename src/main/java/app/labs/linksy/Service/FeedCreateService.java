package app.labs.linksy.Service;

import app.labs.linksy.DAO.FeedCreateMapper;
import app.labs.linksy.DAO.HashtagMapper;
import app.labs.linksy.Model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

@Service
public class FeedCreateService {



    @Autowired
    private FeedCreateMapper feedCreateMapper;

    @Autowired
    private HashtagMapper hashtagMapper;

    /**
     * Create a new feed, including saving hashtags and images.
     */
    @Transactional
    public void createFeed(Feed feed, List<String> imageNames) {
        try {
            // 피드 삽입
            feedCreateMapper.insertFeed(feed);
            int feedId = feed.getFeedId(); // 자동 증가된 피드 ID 가져오기
            System.out.println("Feed inserted with ID: " + feedId);

            // 해시태그 추출 및 저장
            String feedContent = feed.getFeedContent(); // Feed 객체에서 내용 가져오기
            Set<String> hashtags = extractHashtags(feedContent);
            saveHashtags(feedId, new ArrayList<>(hashtags));

            // 이미지 삽입
            for (String imgName : imageNames) {
                feedCreateMapper.insertFeedImage(feedId, imgName);
                System.out.println("Inserted image with name: " + imgName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // 트랜잭션 롤백을 위해 예외 재던짐
        }
    }




    /**
     * 해시태그를 정제하는 메서드.
     */
    private String refineHashtag(String hashtag) {
        if (hashtag == null || hashtag.trim().isEmpty()) {
            return null;
        }
        // '#' 제거 및 소문자로 변환
        String refined = hashtag.replaceFirst("^#", "").trim().toLowerCase();

        // 길이 제한 (2-20자)
        if (refined.length() < 2 || refined.length() > 20) {
            return null; // 조건에 맞지 않으면 null 반환
        }

        return refined;
    }



    /**
     * Update an existing feed, including updating its hashtags and images.
     */
    @Transactional
    public void updateFeed(Feed feed, List<String> hashtags, List<String> images) {
        // Update the feed content
        feedCreateMapper.updateFeed(feed);

        int feedId = feed.getFeedId();

        // Remove existing hashtags and add new ones
        feedCreateMapper.deleteFeedHashtags(feedId);
        for (String hashtag : hashtags) {
            int hashtagId = feedCreateMapper.insertHashtagAndReturnId(hashtag);
            feedCreateMapper.insertFeedHashtag(feedId, hashtagId);
        }

        // Remove existing images and add new ones
        feedCreateMapper.deleteFeedImages(feedId);
        for (String imgName : images) {
            feedCreateMapper.insertFeedImage(feedId, imgName);
        }
    }



    /**
     * Delete a feed by its ID, including associated hashtags and images.
     */
    @Transactional
    public void deleteFeed(int feedId) {
        // Delete all associations first, then delete the feed
        feedCreateMapper.deleteFeedHashtags(feedId);
        feedCreateMapper.deleteFeedImages(feedId);
        feedCreateMapper.deleteFeed(feedId);
    }

    /**
     * Get a feed by its ID, including associated hashtags and images.
     */
    public Feed getFeedById(int feedId) {
        return feedCreateMapper.getFeedById(feedId);
    }

    @Transactional
    public void saveHashtags(int feedId, List<String> hashtags) {
        for (String tag : hashtags) {
            // 해시태그 정제
            String refinedTag = refineHashtag(tag);
            if (refinedTag == null) {
                System.out.println("Invalid or empty hashtag skipped: " + tag);
                continue; // 잘못된 해시태그는 건너뜀
            }

            // 해시태그 ID 찾기 또는 생성
            Integer tagId = hashtagMapper.findHashtagIdByName(refinedTag);
            if (tagId == null) {
                hashtagMapper.insertHashtag(refinedTag);
                tagId = hashtagMapper.findHashtagIdByName(refinedTag);
            }

            // 피드-해시태그 관계 저장
            hashtagMapper.insertFeedHashtag(feedId, tagId);
            System.out.println("Inserted hashtag with ID: " + tagId + " for feed ID: " + feedId);
        }
    }


    public boolean updateFeedContent(int feedId, String feedContent) {
        int updatedRows = feedCreateMapper.updateFeedContent(feedId, feedContent);
        return updatedRows > 0; // 업데이트된 행이 1개 이상인지 확인
    }

    public Set<String> extractHashtags(String content) {
        Set<String> hashtags = new HashSet<>();
        if (content == null || content.isEmpty()) {
            return hashtags;
        }

        Pattern pattern = Pattern.compile("#([a-zA-Z가-힣0-9_]{2,20})"); // 길이 제한 추가
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            hashtags.add(matcher.group(1)); // "#" 기호를 제외한 해시태그 부분만 추가
        }

        return hashtags;
    }


    public List<String> getFeedImages(int feedId) {
        return feedCreateMapper.getImagesByFeedId(feedId);
    }

    public void updateFeedContent(Feed feed) {
        feedCreateMapper.updateFeed(feed);
    }


}
