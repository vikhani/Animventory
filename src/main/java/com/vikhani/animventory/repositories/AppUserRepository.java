package com.vikhani.animventory.repositories;

import com.vikhani.animventory.models.AppUser;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    @Query("UPDATE AppUser u SET u.failedAttempts =?1 WHERE u.username = ?2")
    @Modifying
    public void updateFailedAttemptsCount(int failedAttempts, String username);
}
