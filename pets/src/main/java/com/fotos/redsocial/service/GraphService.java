package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.dto.responses.MSTEdgeResponse;
import com.fotos.redsocial.entity.dto.responses.MSTResponse;
import com.fotos.redsocial.repository.LocationRepository;
import com.fotos.redsocial.repository.ShelterRepository;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class GraphService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    public MSTResponse calculateShelterMSTWithPrim(String startShelterId) {
    List<MSTEdgeData> allEdges = shelterRepository.getShelterConnections();
    
    System.out.println("DEBUG: Total de conexiones cargadas: " + allEdges.size());
    
    // Crear conjunto de nodos únicos
    Set<String> allShelterIds = allEdges.stream()
        .flatMap(e -> Stream.of(e.getSourceId(), e.getDestinationId()))
        .collect(Collectors.toSet());
        
    System.out.println("DEBUG: Total de shelters únicos: " + allShelterIds.size());

    if (!allShelterIds.contains(startShelterId)) {
        System.out.println("DEBUG: shelter inicial no encontrado: " + startShelterId);
        System.out.println("DEBUG: shelters disponibles: " + allShelterIds);
        throw new IllegalArgumentException("Shelter inicial no encontrado en el grafo");
    }

    Set<String> visited = new HashSet<>();
    List<MSTEdgeResponse> mstEdges = new ArrayList<>();
    double totalDistance = 0.0;

    PriorityQueue<MSTEdgeData> queue = new PriorityQueue<>(Comparator.comparingDouble(MSTEdgeData::getDistance));

    visited.add(startShelterId);
    addShelterEdgesToQueue(startShelterId, allEdges, visited, queue);

    while (!queue.isEmpty() && visited.size() < allShelterIds.size()) {
        MSTEdgeData minEdge = queue.poll();
        System.out.println("DEBUG: minEdge: " + minEdge.getSourceId());
        if (visited.contains(minEdge.getDestinationId())) continue;

        mstEdges.add(minEdge.toResponse());
        totalDistance += minEdge.getDistance();
        visited.add(minEdge.getDestinationId());

        addShelterEdgesToQueue(minEdge.getDestinationId(), allEdges, visited, queue);
    }

    if (visited.size() != allShelterIds.size()) {
        System.out.println("DEBUG: El grafo no está completamente conectado");
        System.out.println("DEBUG: visitados: " + visited.size() + " de " + allShelterIds.size());
        throw new IllegalArgumentException("El grafo de shelters no está completamente conectado");
    }

    return new MSTResponse(
        startShelterId,
        mstEdges.isEmpty() ? null : mstEdges.get(0).getFromLocationName(),
        mstEdges,
        allShelterIds.size(),
        totalDistance
    );
}

    private void addShelterEdgesToQueue(String currentId, List<MSTEdgeData> allEdges,
                                        Set<String> visited, PriorityQueue<MSTEdgeData> queue) {
        for (MSTEdgeData e : allEdges) {
            // Verificar conexiones donde el currentId es origen
            if (e.getSourceId().equals(currentId) && !visited.contains(e.getDestinationId())) {
                queue.offer(e);
            }
            // Verificar conexiones donde el currentId es destino (crear arista inversa)
            if (e.getDestinationId().equals(currentId) && !visited.contains(e.getSourceId())) {
                queue.offer(new MSTEdgeData(
                    e.getDestinationId(),
                    e.getDestinationName(),
                    e.getSourceId(),
                    e.getSourceName(),
                    e.getDistance()
                ));
            }
        }
    }    /**
     * Clase interna para manejar datos de aristas en la cola de prioridad
     */
    @QueryResult
    public static class MSTEdgeData {
        private String sourceId;
        private String sourceName;
        private String destinationId;
        private String destinationName;
        private double distance;

        public MSTEdgeData() {}

        public MSTEdgeData(String sourceId, String sourceName, String destinationId, 
                            String destinationName, double distance) {
            this.sourceId = sourceId;
            this.sourceName = sourceName;
            this.destinationId = destinationId;
            this.destinationName = destinationName;
            this.distance = distance;
        }

        public String getDestinationId() {
            return destinationId;
        }
        public String getDestinationName() {
            return destinationName;
        }

        public double getDistance() {
            return distance;
        }

        public String getSourceId() {
            return sourceId;
        }

        public String getSourceName() {
            return sourceName;
        }

        public MSTEdgeResponse toResponse() {
            return new MSTEdgeResponse(sourceId, sourceName, destinationId, destinationName, distance);
        }
    }
    
}
