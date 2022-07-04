package com.vikhani.animventory.controllers;

import com.vikhani.animventory.dtos.AnimalDto;
import com.vikhani.animventory.models.Animal;
import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.services.AnimalService;
import com.vikhani.animventory.services.AppUserService;
import com.vikhani.animventory.exceptions.InvalidInputException;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/animals")
@AllArgsConstructor
public class AnimalController {
    private AnimalService animalService;
    private AppUserService userService;

    @PostMapping
    public Animal addAnimal(@RequestBody AnimalDto animal) {
        AppUser user = userService.getCurrentUser();
        return animalService.addAnimalToUser(animal, user);
    }

    @GetMapping
    public List<Animal> getAnimals() {
        AppUser user = userService.getCurrentUser();
        return animalService.getAllAnimals(user);
    }

    @GetMapping("/{animalId}")
    public AnimalDto getAnimal(@PathVariable Long animalId) {
        return animalService.getAnimal(animalId);
    }

    @GetMapping("/is_nickname_taken")
    public boolean isNameAvailable(@RequestBody Map<String, String> input) {
        String nickname = input.get("nickname");
        if (nickname == null)
            throw new InvalidInputException("No animal nickname passed for checking.");

        return animalService.checkIfNameTaken(nickname);
    }
}
