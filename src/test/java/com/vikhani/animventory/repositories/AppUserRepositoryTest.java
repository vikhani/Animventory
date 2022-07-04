package com.vikhani.animventory.repositories;

import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.enums.AppUserRole;
import com.vikhani.animventory.AnimventoryApplication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AnimventoryApplication.class)
class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository userRepo;

    @BeforeEach
    void setUp() {
        AppUser user = new AppUser();
        user.setUsername("test user");
        user.setPassword("test");
        user.setRole(AppUserRole.USER);
        user.setAnimals(new ArrayList<>());

        userRepo.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void findByUsername() {
        AppUser user = userRepo.findByUsername("test user");

        assertNotNull(user);
    }

    @Test
    void updateFailedAttemptsCount() {
        userRepo.updateFailedAttemptsCount(5, "test user");
        AppUser user = userRepo.findByUsername("test user");

        assertNotNull(user);
        assertEquals(5, user.getFailedAttempts());
    }
}