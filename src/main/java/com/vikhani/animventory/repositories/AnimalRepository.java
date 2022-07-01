package com.vikhani.animventory.repositories;

import com.vikhani.animventory.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
