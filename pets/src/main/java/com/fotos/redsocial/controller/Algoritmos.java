package com.fotos.redsocial.controller;
import com.fotos.redsocial.service.GraphService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import com.fotos.redsocial.entity.dto.responses.MSTResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/algoritmos")
public class Algoritmos {

    @Autowired
    private final GraphService graphService;

    public Algoritmos(GraphService graphService){
        this.graphService = graphService;
    }

    /**
     * Calcula el MST (Minimum Spanning Tree) desde una ubicación inicial
     * @body startLocationId ID de la ubicación inicial para el algoritmo de Prim
     * @return MSTResponse con las aristas del árbol y costo total
     */
    @GetMapping("/prim")
    public ResponseEntity<MSTResponse> calculatePrim(@RequestBody String startLocationId) {
        try {
            MSTResponse mstResponse = graphService.calculateMSTWithPrim(startLocationId);
            return ResponseEntity.ok(mstResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
