package com.vikhani.animventory.repositories;

import com.vikhani.animventory.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Animal findByNickname(String nickname);
}
