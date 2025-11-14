package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

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
 * CASO DE USO: "Un camión de alimentos / veterinarios / voluntarios necesita visitar todos los shelters de nuestra red
 * y encotrar la red mínima que conecta a todos ellos, con el menor recorrdio posible"
 *
 * FUNCIONAMIENTO:
 * El algoritmo de Prim es un algoritmo voraz (greedy) que encuentra un Árbol de
 * Expansión Mínima (MST) para un grafo conectado no dirigido y ponderado. Un MST
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
        // --- FASE 1: INICIALIZACIÓN ---

        // 1. `mstEdges`: Lista para guardar las aristas que formarán el MST final.
        List<MSTEdgeResponse> mstEdges = new ArrayList<>();
        // 2. `visited`: Conjunto para registrar los nodos que ya forman parte del MST.
        Set<String> visited = new HashSet<>();
        // 3. `pq`: Cola de prioridad para almacenar las aristas candidatas, ordenadas por peso (distancia).
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));

        List<Location> allLocations = locationRepository.findAll();
        if (allLocations.isEmpty()) {
            return new MSTResponse(mstEdges);
        }

        // 4. Elegir una ubicación de inicio arbitraria para comenzar a construir el MST.
        Location start = allLocations.get(0);
        visited.add(start.getId());


        // --- FASE 2: AÑADIR ARISTAS DEL NODO INICIAL ---
        // 5. Añadir todas las aristas del nodo inicial a la cola de prioridad.
        // Estas son las primeras aristas candidatas para formar el MST.
        for (ConnectConnection conn : start.getConnections()) {
            pq.offer(new Edge(start.getId(), conn.getDestination().getId(), conn.getDistance()));
        }


        // --- FASE 3: BUCLE PRINCIPAL (CRECIMIENTO DEL MST) ---
        // El bucle se ejecuta hasta que hayamos incluido todos los nodos en el MST.
        while (!pq.isEmpty() && visited.size() < allLocations.size()) {
            // 6. Extraer la arista con el menor peso (la más prometedora).
            Edge edge = pq.poll();
            String destinationId = edge.getDestination();

            // 7. Evitar ciclos: Si el nodo destino de la arista ya está en el MST, la ignoramos.
            if (visited.contains(destinationId)) {
                continue;
            }

            // --- FASE 4: EXPANSIÓN DEL MST ---
            // 8. La arista es válida: la añadimos al MST.
            mstEdges.add(new MSTEdgeResponse(edge.getSource(), destinationId, edge.getWeight()));
            
            // 9. Marcar el nodo destino como visitado, añadiéndolo al conjunto del MST.
            visited.add(destinationId);

            // 10. Obtener el nodo que acabamos de añadir.
            Location newVertex = locationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

            // 11. Añadir las aristas del nuevo nodo a la cola de prioridad, siempre que
            // no conecten a un nodo que ya está en el MST.
            for (ConnectConnection conn : newVertex.getConnections()) {
                if (!visited.contains(conn.getDestination().getId())) {
                    pq.offer(new Edge(newVertex.getId(), conn.getDestination().getId(), conn.getDistance()));
                }
            }
        }
        
        // 12. Retornar la lista de aristas que componen el MST.
        return new MSTResponse(mstEdges);
    }
}