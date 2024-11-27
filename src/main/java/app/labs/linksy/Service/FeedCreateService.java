package app.labs.linksy.Service;

import app.labs.linksy.DAO.FeedCreateMapper;
import app.labs.linksy.DAO.HashtagMapper;
import app.labs.linksy.Model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public void createFeed(Feed feed, List<String> hashtags, List<String> imageNames) {
        try {
            // 피드 삽입
            feedCreateMapper.insertFeed(feed);
            int feedId = feed.getFeedId(); // 자동 증가된 피드 ID 가져오기
            System.out.println("Feed inserted with ID: " + feedId);

            // 해시태그 처리 (정제 및 저장)
            saveHashtags(feedId, hashtags);

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
        // '#' 제거 및 소문자로 변환
        String refined = hashtag.replaceFirst("^#", "").trim().toLowerCase();

        // 길이 제한 (2-20자)
        if (refined.length() < 2) {
            return null;
        }
        if (refined.length() > 20) {
            refined = refined.substring(0, 20);
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
        Pattern pattern = Pattern.compile("#([a-zA-Z가-힣0-9_]+)"); // 수정된 정규식: 해시태그는 영문, 숫자, 한글, 밑줄만 허용
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

    @Transactional
    public void deleteFeedRelatedData(int feedId) {
        // 1. 피드의 댓글 삭제
        feedCreateMapper.deleteCommentsByFeedId(feedId);
        
        // 2. 피드의 이미지 삭제
        feedCreateMapper.deleteFeedImagesByFeedId(feedId);
        
        // 3. 피드의 해시태그 관계 삭제
        feedCreateMapper.deleteFeedHashtagsByFeedId(feedId);
        
        // 4. 피드 좋아요 삭제
        feedCreateMapper.deleteFeedLikesByFeedId(feedId);
        
        // 5. 마지막으로 피드 삭제
        feedCreateMapper.deleteFeed(feedId);
    }

}
