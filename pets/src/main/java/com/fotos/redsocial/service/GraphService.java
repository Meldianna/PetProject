package com.fotos.redsocial.service;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.dto.responses.MSTEdgeResponse;
import com.fotos.redsocial.entity.dto.responses.MSTResponse;
import com.fotos.redsocial.entity.relationship.RoadConnection;
import com.fotos.redsocial.repository.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GraphService {

    @Autowired
    private LocationRepository locationRepository;

    /**
     * Calcula el Árbol de Expansión Mínima (MST) usando el algoritmo de Prim
     * @param startLocationId ID de la ubicación inicial
     * @return MSTResponse con las aristas del MST y costo total
     */
    public MSTResponse calculateMSTWithPrim(String startLocationId) {
        Location startLocation = locationRepository.findByIdString(startLocationId);
        if (startLocation == null) {
            throw new IllegalArgumentException("Ubicación inicial no encontrada");
        }
        List<Location> allLocations = locationRepository.findAll();
        if (allLocations.isEmpty()) {
            throw new IllegalArgumentException("No hay ubicaciones en el grafo");
        }

        Set<String> visitedLocationIds = new HashSet<>();
        List<MSTEdgeResponse> mstEdges = new ArrayList<>();
        double totalDistance = 0.0;

        // Usar PriorityQueue para seleccionar la arista de menor peso
        PriorityQueue<MSTEdgeData> edgeQueue = new PriorityQueue<>(
                Comparator.comparingDouble(MSTEdgeData::getDistance)
        );

        // Comenzar desde la ubicación inicial
        visitedLocationIds.add(startLocationId);
        addEdgesToQueue(startLocation, visitedLocationIds, edgeQueue);

        while (!edgeQueue.isEmpty() && visitedLocationIds.size() < allLocations.size()) {
            MSTEdgeData minEdge = edgeQueue.poll();

            // Si ya visitamos el destino, saltamos (evita ciclos)
            if (visitedLocationIds.contains(minEdge.getDestinationId())) {
                continue;
            }

            // Agregar la arista al MST
            mstEdges.add(minEdge.toResponse());
            totalDistance += minEdge.getDistance();

            // Marcar el nuevo nodo como visitado
            visitedLocationIds.add(minEdge.getDestinationId());

            // Agregar las aristas del nuevo nodo a la cola de prioridad
            Location destinationLocation = locationRepository.findById(minEdge.getDestinationId())
                    .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));
            addEdgesToQueue(destinationLocation, visitedLocationIds, edgeQueue);
        }

        if (visitedLocationIds.size() != allLocations.size()) {
            throw new IllegalArgumentException(
                    "El grafo no está completamente conectado desde la ubicación inicial"
            );
        }

        // Construir y retornar la respuesta
        return new MSTResponse(
                startLocationId,
                startLocation.getName(),
                mstEdges,
                allLocations.size(),
                totalDistance
        );
    }

    /**
     * Agrega las aristas de una ubicación a la cola de prioridad
     */
    private void addEdgesToQueue(Location location, Set<String> visitedLocationIds,
                                PriorityQueue<MSTEdgeData> edgeQueue) {
        if (location.getRoadTo() == null) {
            return;
        }

        for (RoadConnection road : location.getRoadTo()) {
            String destinationId = road.getDestination().getId();
            // Solo agregar si el destino no ha sido visitado
            if (!visitedLocationIds.contains(destinationId)) {
                edgeQueue.offer(new MSTEdgeData(
                        location.getId(),
                        location.getName(),
                        destinationId,
                        road.getDestination().getName(),
                        road.getDistance()
                ));
            }
        }
    }

    /**
     * Clase interna para manejar datos de aristas en la cola de prioridad
     */
    private static class MSTEdgeData {
        private String sourceId;
        private String sourceName;
        private String destinationId;
        private String destinationName;
        private double distance;

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

        public double getDistance() {
            return distance;
        }

        public MSTEdgeResponse toResponse() {
            return new MSTEdgeResponse(sourceId, sourceName, destinationId, destinationName, distance);
        }
    }
    
}
