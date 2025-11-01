package com.fotos.redsocial.entity.dto.requests;

import java.util.List;

import com.fotos.redsocial.entity.Specie;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnimalRequest {
    private String name;
    private String gender;
    private int age;
    private Specie specie;
    private Long sheltedId;
    private List<TraitRequest> traits;

}
