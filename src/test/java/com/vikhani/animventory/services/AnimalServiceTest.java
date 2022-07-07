package com.vikhani.animventory.services;

import com.vikhani.animventory.enums.Genders;
import com.vikhani.animventory.enums.AppUserRole;
import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.models.Species;
import com.vikhani.animventory.dtos.AnimalDto;
import com.vikhani.animventory.exceptions.NotFoundException;
import com.vikhani.animventory.repositories.AnimalRepository;
import com.vikhani.animventory.repositories.AppUserRepository;
import com.vikhani.animventory.repositories.SpeciesRepository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    @Mock
    private AppUserRepository userRepo;
    @Mock
    private AnimalRepository animalRepo;
    @Mock
    private SpeciesRepository speciesRepo;

    private AnimalService animalService;

    @BeforeEach
    void SetUp() {
        animalService = new AnimalService(userRepo, speciesRepo, animalRepo);
    }

    private static final Date TEST_BIRTHDAY = new Date();

    @Test
    // TODO
    @Disabled("Not working since animal is not added to the passed User now")
    void addAnimalToUser() {
        AnimalDto animalDto = new AnimalDto();
        animalDto.setNickname("Test Nickname");
        animalDto.setBirthday(TEST_BIRTHDAY);
        animalDto.setGender(Genders.FEMALE);

        animalDto.setSpecies("Cat");

        AppUser user = new AppUser();
        user.setUsername("test user");
        user.setPassword("test");
        user.setRole(AppUserRole.USER);
        user.setAnimals(new ArrayList<>());

        Species species = new Species();
        species.setSpeciesName("Cat");
        species.setId(1L);

        when(speciesRepo.getBySpeciesName("Cat")).thenReturn(species);

        animalService.addAnimalToUser(animalDto, user);

        assertEquals(1, user.getAnimals().size());
    }

    @Test
    void getNonexistentAnimalThrows() {
        assertThatThrownBy(() -> animalService.getAnimal(1L))
                .isInstanceOf(NotFoundException.class);

    }
}