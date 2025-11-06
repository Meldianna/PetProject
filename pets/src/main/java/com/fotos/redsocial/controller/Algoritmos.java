package com.fotos.redsocial.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.responses.MSTResponse;
import com.fotos.redsocial.service.GraphService;

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
    public ResponseEntity<MSTResponse> calculatePrim(@RequestParam String shelterStartId) {
        try {
            MSTResponse mstResponse = graphService.calculateShelterMSTWithPrim(shelterStartId);
            return ResponseEntity.ok(mstResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
