package com.vikhani.animventory.repositories;

import com.vikhani.animventory.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
