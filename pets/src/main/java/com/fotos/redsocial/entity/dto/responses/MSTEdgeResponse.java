package com.fotos.redsocial.entity.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSTEdgeResponse {
    private String fromLocationId;
    private String fromLocationName;
    private String toLocationId;
    private String toLocationName;
    private double distance;
}
