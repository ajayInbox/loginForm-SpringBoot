package com.self.login.user.registration;

import com.self.login.user.entity.ApplicationUser;
import com.self.login.user.events.RegistrationCompleteEvent;
import com.self.login.user.registration.token.VerificationToken;
import com.self.login.user.registration.token.VerificationTokenRepository;
import com.self.login.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @PostMapping
    public String registerUser(@RequestBody RegistrationForm req, HttpServletRequest httpReq){
        ApplicationUser user = userService.register(req);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrls(httpReq)));
        return "check email for confirmation";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if(theToken.getUser().isEnabled()){
            return "user is already verified pls login!";
        }
        String isValid = userService.validateToken(token);
        if(isValid.equalsIgnoreCase("valid")){
            return "email verified successfully";
        }
        return "Invalid token";
    }

    private String applicationUrls(HttpServletRequest httpReq) {
        return "http://"+httpReq.getServerName()+":"+httpReq.getServerPort()+httpReq.getContextPath();
    }
}
