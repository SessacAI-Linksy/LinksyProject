package app.labs.linksy.Controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.labs.linksy.Model.Comment;
import app.labs.linksy.Model.Feed;
import app.labs.linksy.Model.Member;
import app.labs.linksy.Service.CommentService;
import app.labs.linksy.Service.FeedService;
import app.labs.linksy.Service.FollowService;
import app.labs.linksy.Service.MemberService;

@Controller
@RequestMapping("/linksy")
public class MainController {
	
	@Autowired
    private MemberService memberService;
	
	@Autowired
	private FeedService feedService;	
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private FollowService followService;
	
	// 메인 페이지 호출
	@GetMapping(value="")
	public String main(Model model) {
		
		 String userId = "testUser"; // DB에서 가져올 사용자 ID (하드코딩된 값)
		 
	     // DB에서 사용자 정보 가져오기
	     Member member = memberService.getMemberByUserId(userId);
	     
	     // 피드 데이터 가져오기
	     List<Feed> feeds = feedService.getFeedsWithDetails();
	     
	     // 팔로우한 사용자 정보 가져오기
	     List<Member> followings = followService.getFollowings(userId);
	     
	     // 랜덤으로 7명 선택
		 Collections.shuffle(followings);
		 List<Member> limitedFollowings = followings.stream().limit(7).collect(Collectors.toList());
	     
	     // 각 피드에 댓글 데이터 추가
	     for (Feed feed : feeds) {
	         List<Comment> comments = commentService.getCommentsByFeedId(feed.getFeedId());
	         feed.setComments(comments); // Feed 모델에 comments 필드를 추가해야 합니다.
	     }
	     
	     model.addAttribute("feeds", feeds);
	     model.addAttribute("member", member);
	     model.addAttribute("followings", limitedFollowings);
	     
		 return "main";
	}
	
	// 댓글 가져오기 API
    @GetMapping("/comments")
    @ResponseBody
    public List<Comment> getComments(@RequestParam("feedId") int feedId) {
    	List<Comment> comments = commentService.getCommentsByFeedId(feedId);
        return comments;
        // return commentService.getCommentsByFeedId(feedId);
    }
    
    

}
