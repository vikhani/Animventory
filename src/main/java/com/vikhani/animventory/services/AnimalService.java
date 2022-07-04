package com.vikhani.animventory.services;

import com.vikhani.animventory.models.Animal;
import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.dtos.AnimalDto;
import com.vikhani.animventory.models.Species;

import com.vikhani.animventory.exceptions.NotFoundException;
import com.vikhani.animventory.exceptions.NameExistsException;

import com.vikhani.animventory.repositories.AnimalRepository;
import com.vikhani.animventory.repositories.AppUserRepository;
import com.vikhani.animventory.repositories.SpeciesRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.List;
import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class AnimalService {
    private AppUserRepository userRepo;
    private SpeciesRepository speciesRepo;
    private AnimalRepository animalRepo;

    @Transactional
    public Animal addAnimalToUser(AnimalDto passedAnimal, AppUser user) {
        if (animalRepo.findByNickname(passedAnimal.getNickname()) != null)
            throw new NameExistsException("Animal name " + passedAnimal.getNickname() + " is already taken.");

        Animal animal = new Animal();
        animal.setNickname(passedAnimal.getNickname());
        animal.setBirthday(passedAnimal.getBirthday());
        animal.setGender(passedAnimal.getGender());

        Species species = speciesRepo.getBySpeciesName(passedAnimal.getSpecies());
        if (species != null)
            animal.setSpecies(species);
        else
            animal.setSpecies(speciesRepo.getBySpeciesName("Unknown"));

        user.getAnimals().add(animal);
        animal.setAppUser(user);

        animalRepo.save(animal);
        userRepo.save(user);

        return animal;
    }

    public List<Animal> getAllAnimals(AppUser user) {
        return user.getAnimals();
    }

    public AnimalDto getAnimal(Long animalId) {
        Animal animal;

        animal = animalRepo.getById(animalId);

        if (animal == null)
            throw new NotFoundException("Animal with id=" + animalId + " not found.");

        AnimalDto dto = new AnimalDto();
        dto.setNickname(animal.getNickname());
        dto.setBirthday(animal.getBirthday());
        dto.setGender(animal.getGender());
        dto.setSpecies(animal.getSpecies().getSpeciesName());

        return dto;
    }

    public boolean checkIfNameTaken(String nickname) {
        return animalRepo.findByNickname(nickname) != null;
    }
}
