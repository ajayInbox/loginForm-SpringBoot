package com.self.login.user.registration;

public record RegistrationForm(
        String firstname,
        String lastname,
        String email,
        String password,
        String role
) {
}
