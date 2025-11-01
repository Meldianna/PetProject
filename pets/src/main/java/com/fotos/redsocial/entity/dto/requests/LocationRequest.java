package com.fotos.redsocial.entity.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationRequest {
    private String locationName;
    private String stateName;
}
