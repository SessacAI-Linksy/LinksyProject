package app.labs.linksy.Controller;

import java.util.List;

import app.labs.linksy.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import app.labs.linksy.Model.Feed;
import app.labs.linksy.Model.Member;
import app.labs.linksy.Service.MemberService;
import app.labs.linksy.Service.SearchService;
import app.labs.linksy.Service.FollowService;
import app.labs.linksy.Service.CommentService;
import app.labs.linksy.Model.Comment;

@Controller 
public class profilePageController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private FollowService followService;
    @Autowired
    private CommentService commentService;

    // 사용중인 사용자의 프로필 페이지
    @GetMapping("/profile")
    public String myProfile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        return showProfile(userId, userId, model);
    }

    // 다른 사용자의 프로필 페이지
    @GetMapping("/profile/{userId}")
    public String userProfile(@PathVariable("userId") String userId, 
                            HttpSession session, 
                            Model model) {
        String currentUserId = (String) session.getAttribute("userId");
        return showProfile(currentUserId, userId, model);
    }

    // 프로필 페이지 출력
    private String showProfile(String currentUserId, String targetUserId, Model model) {
        Member member = memberService.getMemberByUserId(targetUserId);
        if (member == null) {
            return "error/404";
        }
        
        List<Feed> feeds = searchService.getFeedsByUserId(targetUserId);
        // 피드 수 계산
        int feedCount = feeds.size();
        model.addAttribute("feedCount", feedCount);
        
        // 각 피드의 좋아요 수와 댓글 수를 설정
        for (Feed feed : feeds) {
            int likeCount = searchService.getLikeCount(feed.getFeedId());
            int commentCount = searchService.getCommentCount(feed.getFeedId());
            feed.setLikeAmount(likeCount);
            feed.setCommentCount(commentCount);
        }
        
        boolean isOwnProfile = currentUserId != null && currentUserId.equals(targetUserId);
        
        // 팔로워와 팔로잉 수 가져오기
        int followerCount = followService.getFollowerCount(targetUserId);
        int followingCount = followService.getFollowingCount(targetUserId);
        
        model.addAttribute("member", member);
        model.addAttribute("feeds", feeds);
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followingCount", followingCount);
        
        return "profilePage/profile";
    }

    // @GetMapping("/profile/{userId}")
    // public String userProfile(@PathVariable("userId") String userId, Model model) {
        
    //     Member member = memberService.getMemberByUserId(userId);
    //     List<Feed> feeds = searchService.getFeedsByUserId(userId);
        
    //     model.addAttribute("member", member);
    //     model.addAttribute("feeds", feeds);
    //     return "profilePage/profile";
    // }


    // 피드 팝업 출력
    @GetMapping("/profile/feed/popup/{feedId}")
    public String feedPopup(Model model, @PathVariable("feedId") int feedId) {
        List<Comment> comments = commentService.getCommentsByFeedId(feedId);

        Feed feed = searchService.getFeedById(feedId);
		model.addAttribute("feed", feed);
        model.addAttribute("comments", comments);
        return "searchPage/searchFeedPopup";
    }
}
