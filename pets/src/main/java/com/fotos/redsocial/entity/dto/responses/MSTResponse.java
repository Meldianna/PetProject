package com.fotos.redsocial.entity.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSTResponse {
    private String startLocationId;
    private String startLocationName;
    private List<MSTEdgeResponse> edges;
    private int totalLocations;
    private double totalDistance;
}
