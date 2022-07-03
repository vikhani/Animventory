package com.vikhani.animventory.dtos;

import com.vikhani.animventory.enums.Genders;

import lombok.Data;

import java.util.Date;

@Data
public class AnimalDto {
    private String nickname;
    private Date birthday;
    private Genders gender;
    private String species;
}
