package com.fotos.redsocial.entity.dto.responses;

import java.util.List;

public class PathResponse {
    private final List<LocationDTO> locations;
    private final double distance;

    public PathResponse(List<LocationDTO> locations, double distance) {
        this.locations = locations;
        this.distance = distance;
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public double getDistance() {
        return distance;
    }
}
