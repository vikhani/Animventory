package com.vikhani.animventory.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.vikhani.animventory.enums.Genders;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "animals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Animal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "birthday", nullable = false)
    private Date birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Genders gender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "species_id", nullable = false)
    private Species species;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private AppUser appUser;

}
