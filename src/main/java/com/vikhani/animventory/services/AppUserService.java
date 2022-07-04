package com.vikhani.animventory.services;

import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.dtos.AppUserDto;
import com.vikhani.animventory.enums.AppUserRole;
import com.vikhani.animventory.models.CustomUserDetails;
import com.vikhani.animventory.exceptions.NameExistsException;
import com.vikhani.animventory.repositories.AppUserRepository;
import com.vikhani.animventory.configurations.CustomPasswordEncoder;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    public static final int MAX_FAILED_ATTEMPTS = 10;
    private static final long LOCK_TIME_DURATION = 60 * 60 * 1000L; // 1hr
    public static final long FAIL_TIME_WINDOW = 60 * 60 * 1000L; // 1hr

    private CustomPasswordEncoder passwordEncoder;
    private AppUserRepository repository;

    public AppUser registerNewUserAccount(AppUserDto accountDto) {
        if (repository.findByUsername(accountDto.getUsername()) != null) {
            throw new NameExistsException("This username is taken: " + accountDto.getUsername());
        }

        AppUser user = new AppUser();
        user.setAccountNonLocked(true);
        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encoder().encode(accountDto.getPassword()));
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);

        repository.save(user);

        manualLogin(user);

        return user;
    }

    public void manualLogin(AppUser user) {
        UserDetails principal = loadUserByUsername(user.getUsername());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal.getUsername(), principal.getPassword(), principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user with" + username + "username");
        }

        return new CustomUserDetails(user);
    }

    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            return repository.findByUsername(currentUserName);
        } else {
            throw new AccessDeniedException("User is not authenticated.");
        }
    }

    public AppUser getByUsername(String username) {
        return repository.findByUsername(username);
    }

    public void changeFailedAttemptsAccordingToFailTimeWindow(AppUser user) {
        if (!isInFailTimeWindow(user)) {
            resetFailedAttempts(user.getUsername());
            unlockUser(user);
            return;
        }

        increaseFailedAttempts(user);
    }

    private void increaseFailedAttempts(AppUser user) {
        int attemptsCount = user.getFailedAttempts();
        if (attemptsCount == 0 && user.getFirstFail() == null) {
            user.setFirstFail(new Date());
            repository.save(user);
        }

        int newFailAttempts = attemptsCount + 1;
        repository.updateFailedAttemptsCount(newFailAttempts, user.getUsername());
    }

    public void resetFailedAttempts(String username) {
        repository.updateFailedAttemptsCount(0, username);
    }

    public boolean attemptLock(AppUser user) {
        if (!isInFailTimeWindow(user)) {
            user.setFirstFail(null);
            repository.save(user);

            return false;
        }

        lockUser(user);

        return true;
    }

    private void lockUser(AppUser user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        repository.save(user);
    }

    public boolean unlockWhenTimeExpired(AppUser user) {
        if (user.getLockTime() == null)
            return true;

        if (isInLockTimeDuration(user))
            return false;

        unlockUser(user);

        return true;
    }

    private boolean isInFailTimeWindow(AppUser user) {
        Date firstFailDate = user.getFirstFail();
        if (firstFailDate == null) {
            return true;
        }

        long firstFailTime = firstFailDate.getTime();
        long currentTime = System.currentTimeMillis();

        return firstFailTime + FAIL_TIME_WINDOW > currentTime;
    }

    private boolean isInLockTimeDuration(AppUser user) {
        long lockTime = user.getLockTime().getTime();
        long currentTime = System.currentTimeMillis();
        return lockTime + LOCK_TIME_DURATION > currentTime;
    }

    private void unlockUser(AppUser user) {
        user.setAccountNonLocked(true);
        user.setLockTime(null);
        user.setFirstFail(null);
        user.setFailedAttempts(0);

        repository.save(user);
    }

    public static boolean hasFailedLoginAttemptsLeft(AppUser user) {
        return user.getFailedAttempts() < AppUserService.MAX_FAILED_ATTEMPTS - 1;
    }
}
