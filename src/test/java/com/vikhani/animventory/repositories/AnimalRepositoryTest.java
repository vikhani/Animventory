package com.vikhani.animventory.repositories;

import com.vikhani.animventory.enums.Genders;
import com.vikhani.animventory.models.Animal;
import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.AnimventoryApplication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AnimventoryApplication.class)
class AnimalRepositoryTest {
    @Autowired
    private AnimalRepository animalRepo;
    @Autowired
    private SpeciesRepository speciesRepo;
    @Autowired
    private AppUserRepository userRepo;

    private static final Date TEST_BIRTHDAY = new Date();

    @BeforeEach
    void setUp() {
        Animal animal = new Animal();

        animal.setNickname("TestNick");
        animal.setGender(Genders.FEMALE);
        animal.setSpecies(speciesRepo.getBySpeciesName("Unknown"));
        animal.setBirthday(TEST_BIRTHDAY);

        AppUser user = new AppUser();
        user.setUsername("test user");
        user.setPassword("test");

        animal.setAppUser(user);

        userRepo.save(user);

        animalRepo.save(animal);
    }

    @AfterEach
    void tearDown() {
        animalRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void getBySpeciesName() {
        Animal resAnimal = animalRepo.findByNickname("TestNick");

        assertNotNull(resAnimal);
    }

}