package com.self.login.user.events;

import com.self.login.user.entity.ApplicationUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private ApplicationUser user;
    private String applicationUrl;

    public RegistrationCompleteEvent(ApplicationUser user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }


}
