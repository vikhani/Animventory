package com.vikhani.animventory.controllers;

import com.vikhani.animventory.dtos.AppUserDto;
import com.vikhani.animventory.models.AppUser;
import com.vikhani.animventory.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/registration")
@AllArgsConstructor
public class RegistrationController {
    AppUserService service;

    @PostMapping
    public AppUser registerUserAccount(@RequestBody AppUserDto userDto) {
        return service.registerNewUserAccount(userDto);
    }
}
