package com.fotos.redsocial.entity.dto.responses;

public class MSTEdgeResponse {
    private String sourceId;
    private String destinationId;
    private double distance;

    public MSTEdgeResponse(String sourceId, String destinationId, double distance) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.distance = distance;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public double getDistance() {
        return distance;
    }
}
