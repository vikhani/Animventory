package com.vikhani.animventory.repositories;

import com.vikhani.animventory.models.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {
    Species getBySpeciesName(String speciesName);
}
