package com.fotos.redsocial.entity.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailedUserResponse {
    private String name;
    private String email;
    private String phone;
    private LocationResponse location;

    private List<SimpleAnimalResponse> adopted;
    private List<SimpleAnimalResponse> fostering;
    private List<SimpleAnimalResponse> takingCareOf;
}
