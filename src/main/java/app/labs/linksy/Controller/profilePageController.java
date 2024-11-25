package app.labs.linksy.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import app.labs.linksy.Model.Feed;
import app.labs.linksy.Model.Member;
import app.labs.linksy.Service.MemberService;
import app.labs.linksy.Service.SearchService;


@Controller 
public class profilePageController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private SearchService searchService;

    // 사용중인 사용자의 프로필 페이지
    @GetMapping("/profile")
    public String myProfile(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        return showProfile(userId, userId, model);
    }

    // 다른 사용자의 프로필 페이지
    @GetMapping("/profile/{userId}")
    public String userProfile(@PathVariable String userId, 
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
        boolean isOwnProfile = currentUserId != null && currentUserId.equals(targetUserId);
        
        model.addAttribute("member", member);
        model.addAttribute("feeds", feeds);
        model.addAttribute("isOwnProfile", isOwnProfile);
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
        Feed feed = searchService.getFeedById((feedId));
		model.addAttribute("feed", feed);
        return "searchPage/searchFeedPopup";
    }
}
