package com.vikhani.animventory.components;

import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.services.AppUserService;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private AppUserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        AppUser user = userService.getByUsername(username);

        LockedException handleException = handleLoginFailure(user);

        super.onAuthenticationFailure(request, response, handleException == null ? exception : handleException);
    }

    private LockedException handleLoginFailure(AppUser user) {
        if (user == null)
            return null;

        if (user.isAccountNonLocked()) {
            if (user.getFailedAttempts() < AppUserService.MAX_FAILED_ATTEMPTS - 1) {
                userService.increaseFailedAttempts(user);
            } else {
                userService.lock(user);
                return new LockedException("Your account has been locked due to 10 failed attempts. It will be unlocked after 1 hour.");
            }
        } else if (!userService.unlockWhenTimeExpired(user)) {
            return new LockedException("Your account has been locked. Please try to login again later.");
        }

        return null;
    }
}