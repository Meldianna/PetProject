package com.fotos.redsocial.entity.dto;

import java.util.List;

import com.fotos.redsocial.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnimalDTO {
    private String name;
    private int age;
    private String gender;
    private Status status;
    private SpecieDTO specie;

    private List<SimpleUserDTO> caretakers;
    private SimpleUserDTO adopter;
    private SimpleUserDTO fosterer;
}
