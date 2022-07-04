package com.vikhani.animventory.services;

import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.enums.AppUserRole;
import com.vikhani.animventory.repositories.AppUserRepository;
import com.vikhani.animventory.configurations.CustomPasswordEncoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    @Mock
    private AppUserRepository userRepo;

    private AppUserService userService;

    private static Date OLD_DATE;

    static {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        try {
            OLD_DATE = parser.parse("10/10/2020");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void SetUp() {
        userService = new AppUserService(new CustomPasswordEncoder(), userRepo);
    }

    @Test
    void changeFailedAttemptsAccordingToFailTimeWindowResetsAttempts() {
        AppUser user = new AppUser();
        user.setUsername("name");
        user.setPassword("pass");
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);
        user.setFirstFail(OLD_DATE);
        user.setFailedAttempts(5);
        user.setAccountNonLocked(true);

        userService.changeFailedAttemptsAccordingToFailTimeWindow(user);

        assertEquals(0, user.getFailedAttempts());
    }

    @Test
    void changeFailedAttemptsAccordingToFailTimeWindowUpdatesAttempts() {
        AppUser user = new AppUser();
        user.setUsername("name");
        user.setPassword("pass");
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);
        user.setFirstFail(new Date()); // now
        user.setFailedAttempts(5);
        user.setAccountNonLocked(true);

        userService.changeFailedAttemptsAccordingToFailTimeWindow(user);

        verify(userRepo, times(1)).updateFailedAttemptsCount(6, user.getUsername());
    }

    @Test
    void attemptLockSuccess() {
        AppUser user = new AppUser();
        user.setUsername("name");
        user.setPassword("pass");
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);
        user.setFirstFail(new Date()); // now
        user.setFailedAttempts(5);
        user.setAccountNonLocked(true);

        userService.attemptLock(user);

        assertFalse(user.isAccountNonLocked());
    }

    @Test
    void notAttemptLock() {
        AppUser user = new AppUser();
        user.setUsername("name");
        user.setPassword("pass");
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);
        user.setFirstFail(OLD_DATE);
        user.setFailedAttempts(5);
        user.setAccountNonLocked(true);

        userService.attemptLock(user);

        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void unlockWhenTimeExpiredSuccess() {
        AppUser user = new AppUser();
        user.setUsername("name");
        user.setPassword("pass");
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);
        user.setFirstFail(OLD_DATE);
        user.setLockTime(OLD_DATE);
        user.setFailedAttempts(5);
        user.setAccountNonLocked(false);

        userService.unlockWhenTimeExpired(user);

        assertNull(user.getLockTime());
        assertNull(user.getFirstFail());
        assertEquals(0, user.getFailedAttempts());
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void notUnlockWhenTimeExpired() {
        Date curDate = new Date();
        AppUser user = new AppUser();
        user.setUsername("name");
        user.setPassword("pass");
        user.setAnimals(new ArrayList<>());
        user.setRole(AppUserRole.USER);
        user.setFirstFail(OLD_DATE); // some old date
        user.setLockTime(curDate);
        user.setFailedAttempts(5);
        user.setAccountNonLocked(false);

        userService.unlockWhenTimeExpired(user);

        assertFalse(user.isAccountNonLocked());
        assertEquals(curDate, user.getLockTime());
        assertEquals(OLD_DATE, user.getFirstFail());
        assertEquals(5, user.getFailedAttempts());
    }
}