package com.fotos.redsocial.entity.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShelterRequest {
    private String name;
    private String address;
    private String location;

}
