package com.fotos.redsocial.entity.dto.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSimpleDTO {
    private String id;
    private String name;
    private LocalDate date;
    private int animalEachShelter;
    private int volunteersNeeded;
}
