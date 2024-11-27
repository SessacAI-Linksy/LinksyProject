package app.labs.linksy.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.labs.linksy.Model.Member;
import app.labs.linksy.Service.FollowService;
import app.labs.linksy.Service.HttpSessionService;
import app.labs.linksy.Service.MemberService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private MemberService memberService;
      
    @Autowired
    private HttpSessionService httpSessionService;

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        String userId = httpSessionService.sessionConfirm(session);
        Member member = memberService.getMemberByUserId(userId);

        model.addAttribute("member", member);
       
        return "settings/profile";
    }

    @GetMapping("/notifications")
    public String notifications() {
        return "settings/notifications";
    }
}
