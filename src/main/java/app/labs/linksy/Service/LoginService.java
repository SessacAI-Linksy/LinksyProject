package app.labs.linksy.Service;

import app.labs.linksy.DAO.LoginRepository;
import app.labs.linksy.Model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    public void login(Member member) {
        loginRepository.login(member);
    }

    public boolean validateLogin(String userId, String password) {
        Member member = loginRepository.findByUserId(userId);
        return member != null && member.getUserPwd().equals(password);
    }
}
