package app.labs.linksy.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import app.labs.linksy.Service.LoginService;

@Controller
public class LoginPageController {
	
	@Autowired
	private LoginService loginService;
	
    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam("userId") String userId, 
                        @RequestParam("password") String password,
                        HttpSession session) {
        try {
            boolean isValid = loginService.validateLogin(userId, password);
            if (isValid) {
                session.setAttribute("userId", userId);
                return "redirect:/linksy";
            } else {
                return "redirect:/login?error=true";
            }
        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }

}
