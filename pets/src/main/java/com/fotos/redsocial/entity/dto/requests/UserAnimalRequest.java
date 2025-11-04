package com.fotos.redsocial.entity.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAnimalRequest {
    private String userEmail;
    private Long animalId;
    
}
