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
    private static final long LOCK_TIME_DURATION = 60 * 60 * 1000L;

    private CustomPasswordEncoder passwordEncoder;
    private AppUserRepository repository;

    public AppUser registerNewUserAccount(AppUserDto accountDto) {
        if (repository.findByUsername(accountDto.getUsername()) != null) {
            throw new NameExistsException("This username is taken:" + accountDto.getUsername());
        }

        AppUser user = new AppUser();
        user.setAccountNonLocked(true);
        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encoder().encode(accountDto.getPassword()));
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);

        repository.save(user);

        UserDetails principal = loadUserByUsername(user.getUsername());
        Authentication auth = new UsernamePasswordAuthenticationToken(principal.getUsername(), principal.getPassword(), principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return user;
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

    public void increaseFailedAttempts(AppUser user) {
        int newFailAttempts = user.getFailedAttempts() + 1;
        repository.updateFailedAttemptsCount(newFailAttempts, user.getUsername());
    }

    public void resetFailedAttempts(String username) {
        repository.updateFailedAttemptsCount(0, username);
    }

    public void lock(AppUser user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());

        repository.save(user);
    }

    public boolean unlockWhenTimeExpired(AppUser user) {
        Date lockDate = user.getLockTime();
        if (lockDate == null)
            return true;

        long lockTime = lockDate.getTime();
        long currentTime = System.currentTimeMillis();

        if (lockTime + LOCK_TIME_DURATION > currentTime)
            return false;

        user.setAccountNonLocked(true);
        user.setLockTime(null);
        user.setFailedAttempts(0);

        repository.save(user);

        return true;
    }
}
