package com.vikhani.animventory.repositories;

import com.vikhani.animventory.models.Species;
import com.vikhani.animventory.AnimventoryApplication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AnimventoryApplication.class)
class SpeciesRepositoryTest {
    @Autowired
    private SpeciesRepository repo;

    @Test
    void getBySpeciesName() {
        Species species = repo.getBySpeciesName("Unknown");

        assertNotNull(species);
    }
}