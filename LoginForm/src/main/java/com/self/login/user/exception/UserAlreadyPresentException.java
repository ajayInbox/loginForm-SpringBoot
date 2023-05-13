package com.self.login.user.exception;

public class UserAlreadyPresentException extends RuntimeException {

    public UserAlreadyPresentException(String message){
        super(message);
    }
}
