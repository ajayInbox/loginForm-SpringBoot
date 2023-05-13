package com.self.login.user.service;

import com.self.login.user.entity.ApplicationUser;
import com.self.login.user.registration.RegistrationForm;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<ApplicationUser> getAllUsers();

    ApplicationUser register(RegistrationForm request);

    Optional<ApplicationUser> findByEmail(String email);

    void saveVerificationToken(ApplicationUser user, String token);

    String validateToken(String token);
}
