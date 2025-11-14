package com.fotos.redsocial.entity.dto.responses;

import java.util.List;

public class MSTResponse {
    private List<MSTEdgeResponse> edges;

    public MSTResponse(List<MSTEdgeResponse> edges) {
        this.edges = edges;
    }

    public List<MSTEdgeResponse> getEdges() {
        return edges;
    }
}
