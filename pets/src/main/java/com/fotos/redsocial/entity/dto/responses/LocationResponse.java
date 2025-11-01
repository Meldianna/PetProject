package com.fotos.redsocial.entity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LocationResponse {
    private String locationName;
    private String state;
}
