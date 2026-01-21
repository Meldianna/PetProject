package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.dto.responses.LocationDTO;
import com.fotos.redsocial.entity.dto.responses.PathResponse;
import com.fotos.redsocial.entity.relationship.ConnectConnection;
import com.fotos.redsocial.repository.LocationRepository;

/**
 * ALGORITMO: Ramificación y Poda (Branch and Bound) para el Problema del Viajante (TSP)
 *
 * CASO DE USO: "Un usuario entra a la aplicación, y quiere visitar a cada uno de los animales que apadrino, los cuales se encuentran en diversos shelters en diferenres ubicaciones
 * Como usuario, quiero partir de una ubicación y desde ese lugar hacer un recorrido entre el resto de los shelters donde tengo otros animales que cuido,
 * y finalmente volver de nuevo a mi punto de partida, con el menor costo posible"
 * 
 * 
 * FUNCIONAMIENTO:
 * Branch and Bound es una técnica de optimización para problemas que buscan
 * la mejor solución en un vasto espacio de búsqueda. Es especialmente adecuado
 * para problemas NP-Hard como el del Viajante de Comercio (Traveling Salesperson Problem - TSP).
 * 
 * - PROBLEMA: En esta versión, se busca la ruta más corta que comience en una
 *   ubicación `startId`, visite un conjunto de ubicaciones objetivo
 *   (personalizadas para un `userEmail`) y regrese al punto de partida, formando
 *   un ciclo completo.
 *
 * - RAMIFICACIÓN (Branch): El algoritmo explora posibles rutas de forma incremental,
 *   construyendo un árbol de decisiones donde cada nodo representa una elección
 *   de la siguiente ubicación a visitar.
 * 
 * - PODA (Bound): La clave del algoritmo. Antes de explorar una nueva rama
 *   (añadir una nueva ubicación a la ruta), se verifica si el coste del camino
 *   actual ya es peor que la mejor solución completa encontrada hasta ahora. Si
 *   lo es, esa rama y todas sus posibles continuaciones son descartadas ("podadas"),
 *   ahorrando una cantidad masiva de cálculos.
 */
@Service
public class BranchAndBoundService {

    @Autowired
    private LocationRepository locationRepository;

    public PathResponse findOptimalShelterRoute(String startId, String userEmail) {

        //Obtener los nodos clave: inicio y objetivos.
        Location startNode = locationRepository.findById(startId)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        // Las ubicaciones objetivo ahora son personalizadas para el usuario.
        List<Location> targetLocations = locationRepository.findTargetLocationsForUser(userEmail);

        // "bestRoute" y "bestCost": Almacenarán la mejor ruta cíclica encontrada
        // y su coste total. Se usa un array para que "bestCost" sea mutable en la recursión.
        List<Location> bestRoute = new ArrayList<>();
        double[] bestCost = { Double.MAX_VALUE };

        // Crear un conjunto con los IDs de los objetivos para una verificación rápida (O(1)).
        Set<String> targetIds = targetLocations.stream()
            .map(Location::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        // El punto de partida también debe ser considerado un objetivo si no está ya en la lista.
        targetIds.add(startId);


        // Iniciar el proceso recursivo de ramificación y poda desde el nodo de inicio.
        branchAndBound(
            startNode,                             // Nodo actual
            new ArrayList<>(List.of(startNode)), // Camino actual
            new HashSet<>(List.of(startId)),     // Nodos visitados en este camino
            0.0,                               // Coste actual
            targetIds,                           // Conjunto de todos los objetivos a visitar
            bestRoute,                           // Referencia a la mejor ruta encontrada
            bestCost,                            // Referencia al mejor coste encontrado
            startNode                            // Nodo inicial (para calcular el regreso)
        );
        

        //Convertir la ruta óptima encontrada a una lista de DTOs para la respuesta.
        List<LocationDTO> locationDTOs = bestRoute.stream()
            .map(loc -> new LocationDTO(loc.getId(), loc.getName(), loc.getAddress()))
            .collect(Collectors.toList());

        return new PathResponse(locationDTOs, bestCost[0]);
    }

    private void branchAndBound(Location current, List<Location> currentPath, Set<String> visited, double currentCost,
                                Set<String> targetIds, List<Location> bestPath, double[] bestCost, Location startNode) {

        // --- PODA ---
        // Si el coste del camino que estamos construyendo ya es peor que la mejor
        // solución completa que hemos encontrado, se descarta esta rama. 
        if (currentCost >= bestCost[0]) {
            return;
        }

        // --- "CASO DE ÉXITO" ---
        if (visited.containsAll(targetIds)) {
            // Para completar el ciclo, calculamos el coste de regresar al inicio.
            Double returnCost = locationRepository.getDistanceBetween(current.getId(), startNode.getId());
            
            // Si no hay un camino de regreso, esta ruta no es un ciclo válido.
            if (returnCost == null) return;
            
            double totalCost = currentCost + returnCost;

            // Si este ciclo es mejor que el mejor encontrado hasta ahora, lo guardamos.
            if (totalCost < bestCost[0]) {
                bestCost[0] = totalCost;
                bestPath.clear();
                bestPath.addAll(currentPath);
                bestPath.add(startNode); // Añadimos el nodo inicial al final para cerrar el ciclo visualmente.
            }
            return; // Fin de esta rama de exploración.
        }

        // --- RAMIFICACIÓN ---
        // Explorar todos los vecinos del nodo actual.
        List<ConnectConnection> connections = current.getConnections();
        if (connections == null) return;

        for (ConnectConnection conn : connections) {
            Location next = conn.getDestination();
            if (next == null || next.getId() == null) continue;
            
            // Evitar ciclos dentro del camino (no visitar el mismo nodo dos veces).
            if (!visited.contains(next.getId())) {
                
                // Extender la ruta actual, añadiendo el siguiente nodo.
                visited.add(next.getId());
                currentPath.add(next);

                // Continuar la búsqueda desde el nuevo nodo.
                branchAndBound(next, currentPath, visited, currentCost + conn.getDistance(), 
                            targetIds, bestPath, bestCost, startNode);
                
                visited.remove(next.getId());
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }
}