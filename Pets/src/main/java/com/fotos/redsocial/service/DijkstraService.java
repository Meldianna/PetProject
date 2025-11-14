package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.Node;
import com.fotos.redsocial.entity.dto.responses.LocationDTO;
import com.fotos.redsocial.entity.dto.responses.PathResponse;
import com.fotos.redsocial.entity.relationship.ConnectConnection;
import com.fotos.redsocial.repository.LocationRepository;

/**
 * ALGORITMO: Dijkstra
 * 
 * CASO DE USO: "Encontrar la menor distancia entre dos ubicaciones"
 *
 * FUNCIONAMIENTO:
 * El algoritmo de Dijkstra encuentra el camino de menor coste (distancia, en
 * este caso) desde un nodo de origen único hacia todos los demás nodos en un
 * grafo con pesos no negativos en las aristas.
 * Utiliza una cola de prioridad (PriorityQueue) para explorar siempre el nodo
 * no visitado más cercano al origen. Mantiene un registro de las distancias
 * más cortas conocidas desde el origen a cada nodo y las va actualizando a
 * medida que encuentra caminos más cortos.
 *
 * Aquí se usa para calcular la ruta más corta en distancia entre dos ubicaciones.
 */
@Service
public class DijkstraService {

    @Autowired
    private LocationRepository locationRepository;

    public PathResponse findShortestPath(String startId, String endId) {
        // --- FASE 1: INICIALIZACIÓN ---

        // 1. `distances`: Almacena la distancia más corta conocida desde `startId` hasta cada nodo.
        Map<String, Double> distances = new HashMap<>();
        // 2. `previous`: Almacena el nodo predecesor en el camino más corto. Sirve para reconstruir la ruta.
        Map<String, String> previous = new HashMap<>();
        // 3. `pq`: Cola de prioridad que ordena los nodos por su distancia al nodo de inicio.
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // 4. Inicializar todas las distancias como infinito, ya que no conocemos ninguna ruta.
        locationRepository.findAll().forEach(loc -> distances.put(loc.getId(), Double.MAX_VALUE));
        
        // 5. La distancia del nodo de inicio a sí mismo es 0.
        distances.put(startId, 0.0);
        Location startLocation = locationRepository.findById(startId)
                .orElseThrow(() -> new RuntimeException("Start location not found"));
        
        // 6. Añadir el nodo de inicio a la cola de prioridad.
        pq.offer(new Node(startLocation, 0.0));


        // --- FASE 2: BUCLE PRINCIPAL ---
        // El algoritmo se ejecuta mientras haya nodos por procesar en la cola.
        while (!pq.isEmpty()) {
            // 7. Extraer el nodo con la distancia más pequeña de la cola de prioridad.
            Node current = pq.poll();
            String currentId = current.getLocation().getId();

            // 8. Optimización: si el nodo actual es el destino, podemos detenernos,
            // ya que Dijkstra garantiza que hemos encontrado el camino más corto hacia él.
            if (currentId.equals(endId)) break;

            // 9. Ignorar nodos si ya hemos encontrado un camino más corto hacia ellos.
            if (current.getDistance() > distances.get(currentId)) continue;


            // --- FASE 3: RELAJACIÓN DE ARISTAS ---
            // 10. Explorar los vecinos (conexiones) del nodo actual.
            for (ConnectConnection conn : current.getLocation().getConnections()) {
                String nextId = conn.getDestination().getId();
                // 11. Calcular la distancia al vecino a través del nodo actual.
                double newDist = distances.get(currentId) + conn.getDistance();

                // 12. "Relajación": si este nuevo camino es más corto que el que conocíamos,
                // lo actualizamos.
                if (newDist < distances.getOrDefault(nextId, Double.MAX_VALUE)) {
                    distances.put(nextId, newDist); // Actualizar distancia.
                    previous.put(nextId, currentId);  // Actualizar predecesor.
                    pq.offer(new Node(conn.getDestination(), newDist)); // Añadir a la cola para explorar desde él.
                }
            }
        }
        
        // 13. Una vez terminado el bucle, reconstruir el camino desde el destino hacia el origen.
        return reconstructPath(previous, endId, distances);
    }

    private PathResponse reconstructPath(Map<String, String> previous, String endId, Map<String, Double> distances) {
        // 1. Iniciar la reconstrucción desde el nodo destino.
        List<LocationDTO> pathLocations = new ArrayList<>();
        String currentId = endId;
        
        // 2. Retroceder a través del mapa de predecesores hasta llegar al nodo de inicio (cuyo predecesor es null).
        while (currentId != null) {
            Location current = locationRepository.findById(currentId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
            // 3. Añadir la ubicación al principio de la lista para mantener el orden correcto (inicio -> fin).
            pathLocations.add(0, new LocationDTO(current.getId(), current.getName(), current.getAddress()));
            currentId = previous.get(currentId);
        }

        // 4. La distancia total es la distancia calculada por Dijkstra para el nodo final.
        double totalDistance = distances.get(endId);
        
        // 5. Retornar la respuesta con el camino y la distancia total.
        return new PathResponse(pathLocations, totalDistance);
    }
}