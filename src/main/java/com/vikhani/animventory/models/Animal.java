package com.vikhani.animventory.models;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "animals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Animal {
    public enum Gender {
        UNKNOWN,
        MALE,
        FEMALE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "nickname", unique = true, nullable = false)
    String nickname;

    @Column(name = "birthday", nullable = false)
    Date birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    Gender gender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "species_id", nullable = false)
    Species species;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    AppUser appUser;

}
