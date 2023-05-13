package com.self.login.user.events.listener;

import com.self.login.user.entity.ApplicationUser;
import com.self.login.user.events.RegistrationCompleteEvent;
import com.self.login.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;
    ApplicationUser theUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        // get newly register user
        ApplicationUser user = event.getUser();
        // create verification token;
        String token = UUID.randomUUID().toString();
        // save verification token
        userService.saveVerificationToken(user, token);
        // build verification url to be sent to user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+token;
        // send to email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("link to verify email : {}", url);

    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ theUser.getFirstname()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("dailycodework@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
