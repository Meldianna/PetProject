package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Edge;
import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.dto.responses.MSTEdgeResponse;
import com.fotos.redsocial.entity.dto.responses.MSTResponse;
import com.fotos.redsocial.entity.relationship.ConnectConnection;
import com.fotos.redsocial.repository.LocationRepository;

/**
 * ALGORITMO: Prim
 * 
 * CASO DE USO: "Un camión de alimentos / veterinarios / voluntarios necesita
 * visitar todos los shelters de nuestra red
 * y encotrar la red mínima que conecta a todos ellos, con el menor recorrdio
 * posible"
 *
 * FUNCIONAMIENTO:
 * El algoritmo de Prim es un algoritmo voraz (greedy) que encuentra un Árbol de
 * Expansión Mínima (MST) para un grafo conectado no dirigido y ponderado. Un
 * MST
 * es un subgrafo que conecta todos los vértices con la suma mínima de pesos
 * de las aristas, sin formar ciclos.
 *
 * Comienza desde un vértice arbitrario y "hace crecer" el MST añadiendo
 * iterativamente la arista de menor peso que conecta un vértice en el MST
 * con un vértice fuera del MST. Una cola de prioridad es ideal para
 * seleccionar eficientemente la siguiente arista más barata.
 *
 * Aquí, se usa para encontrar una red de conexiones que enlace todas las
 * ubicaciones con la mínima distancia total posible.
 */
@Service
public class PrimService {

    @Autowired
    private LocationRepository locationRepository;

    public MSTResponse getMinimumSpanningTree() {
        // "mstEdges": Lista para guardar las aristas que formarán el MST final.
        List<MSTEdgeResponse> mstEdges = new ArrayList<>();
        // "visited": Conjunto para registrar los nodos que ya forman parte del MST.
        Set<String> visited = new HashSet<>();
        // "pq": Cola de prioridad para almacenar las aristas candidatas, ordenadas por
        // peso (distancia).
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));

        List<Location> allLocations = locationRepository.findAll();
        if (allLocations.isEmpty()) {
            return new MSTResponse(mstEdges);
        }

        // Crear mapa para acceso rápido O(1)
        Map<String, Location> locationMap = allLocations.stream()
                .collect(Collectors.toMap(Location::getId, Function.identity()));

        // Elegir una ubicación de inicio arbitraria para comenzar a construir el MST.
        Location start = allLocations.get(0);
        visited.add(start.getId());

        for (ConnectConnection conn : start.getConnections()) {
            pq.offer(new Edge(start.getId(), conn.getDestination().getId(), conn.getDistance()));
        }

        // El bucle se ejecuta hasta que hayamos incluido todos los nodos en el MST.
        while (!pq.isEmpty() && visited.size() < allLocations.size()) {
            // Extraer la arista con el menor peso.
            Edge edge = pq.poll();
            String destinationId = edge.getDestination();

            // si el nodo destino ya está en el MST, ignorar.
            if (visited.contains(destinationId)) {
                continue;
            }

            // La arista es válida: la añadimos al MST.
            mstEdges.add(new MSTEdgeResponse(edge.getSource(), destinationId, edge.getWeight()));

            visited.add(destinationId);

            // Obtener el nodo que acabamos de añadir desde el mapa en memoria.
            Location newVertex = locationMap.get(destinationId);
            if (newVertex == null) {
                throw new RuntimeException("Location not found in map for ID: " + destinationId);
            }

            for (ConnectConnection conn : newVertex.getConnections()) {
                if (!visited.contains(conn.getDestination().getId())) {
                    pq.offer(new Edge(newVertex.getId(), conn.getDestination().getId(), conn.getDistance()));
                }
            }
        }

        // Retornar la lista de aristas que componen el MST.
        return new MSTResponse(mstEdges);
    }
}