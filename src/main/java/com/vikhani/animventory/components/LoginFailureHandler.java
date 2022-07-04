package com.vikhani.animventory.components;

import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.services.AppUserService;
import com.vikhani.animventory.exceptions.UserLockedException;

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

    // org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
    // doFilter:223, AbstractAuthenticationProcessingFilter (org.springframework.security.web.authentication)
    // checks passed params via request.getParameter(...) which reads parameters from the query string
    // or from form data in the body. It does not try to parse JSON body content.
    // https://stackoverflow.com/questions/69362349/httpservletrequest-getparameter-of-spring-boot-handlerinterceptor-returns-null
    // https://docs.oracle.com/javaee/7/api/javax/servlet/ServletRequest.html#getParameterMap--

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
            if (AppUserService.hasFailedLoginAttemptsLeft(user)) {
                userService.changeFailedAttemptsAccordingToFailTimeWindow(user);
            } else {
                return userService.attemptLock(user)
                        ? new UserLockedException("Your account has been locked due to 10 failed attempts. It will be unlocked after 1 hour.")
                        : null;
            }
        } else if (!userService.unlockWhenTimeExpired(user)) {
            return new UserLockedException("Your account has been locked. Please try to login again later.");
        }

        return null;
    }
}