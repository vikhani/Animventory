package com.vikhani.animventory.models;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "species")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Species implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "species_name", unique = true, nullable = false)
    private String speciesName;
}
