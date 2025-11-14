package com.fotos.redsocial.entity.dto.responses;

import com.fotos.redsocial.entity.Animal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnimalScore {
    private Animal animal;
    private int score;
    
}
