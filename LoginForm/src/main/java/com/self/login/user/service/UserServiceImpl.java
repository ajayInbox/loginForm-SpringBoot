package com.self.login.user.service;

import com.self.login.user.entity.ApplicationUser;
import com.self.login.user.registration.RegistrationForm;
import com.self.login.user.exception.UserAlreadyPresentException;
import com.self.login.user.registration.token.VerificationToken;
import com.self.login.user.registration.token.VerificationTokenRepository;
import com.self.login.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Override
    public List<ApplicationUser> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public ApplicationUser register(RegistrationForm request) {
        Optional<ApplicationUser> user = userRepo.findByEmail(request.email());
        if(user.isPresent()){
            throw new UserAlreadyPresentException("user already present.");
        }
        var newUser = new ApplicationUser();
        newUser.setFirstname(request.firstname());
        newUser.setLastname(request.lastname());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        return userRepo.save(newUser);
    }

    @Override
    public Optional<ApplicationUser> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void saveVerificationToken(ApplicationUser user, String token) {
        var verificationToken = new VerificationToken(user,token);
        tokenRepo.save(verificationToken);

    }

    @Override
    public String validateToken(String token) {
        VerificationToken theToken = tokenRepo.findByToken(token);
        if(theToken==null){
            return "Invalid token";
        }
        ApplicationUser user = theToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((theToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepo.delete(theToken);
            return "token already expired";
        }
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }
}
