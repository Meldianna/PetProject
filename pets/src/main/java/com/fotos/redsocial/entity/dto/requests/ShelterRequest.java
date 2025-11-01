package com.fotos.redsocial.entity.dto.requests;

import com.fotos.redsocial.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShelterRequest {
    private String name;
    private String address;
    private Location location;
}
