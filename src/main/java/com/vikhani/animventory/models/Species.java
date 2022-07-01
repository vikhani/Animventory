package com.vikhani.animventory.models;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "species")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "species_name", unique = true, nullable = false)
    String speciesName;
}
