package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.dto.responses.LocationDTO;
import com.fotos.redsocial.entity.relationship.ConnectConnection;
import com.fotos.redsocial.repository.LocationRepository;

/**
 * ALGORITMO: Búsqueda en Profundidad (DFS - Depth-First Search)
 *
 * CASO DE USO: "Plan B para generación de rutas: necesitas una ruta viable para trasladar animales desde el Refugio A al Refugio B,
 * y no importa que sea la más corta, solo que exista una ruta"
 *
 * FUNCIONAMIENTO:
 * DFS es un algoritmo de recorrido de grafos que explora una rama tan lejos como
 * sea posible antes de retroceder (backtracking). Generalmente se implementa
 * de forma recursiva o utilizando una pila (Stack).
 * A diferencia de BFS, DFS no garantiza encontrar el camino más corto, pero es
 * muy útil para tareas como la detección de ciclos o simplemente para encontrar
 * cualquier camino entre dos nodos.
 *
 * En este caso, se usa para encontrar una ruta (no necesariamente la más corta)
 * entre una ubicación de inicio y una de fin.
 */
@Service
public class DFSService {

    @Autowired
    private LocationRepository locationRepository;

    public List<LocationDTO> findPath(String startId, String endId) {
        // Inicializar un conjunto para registrar los IDs de las ubicaciones visitadas
        // y evitar ciclos y reprocesamiento.
        Set<String> visited = new HashSet<>();

        // Inicializar una lista para almacenar las ubicaciones en el camino encontrado.
        List<Location> path = new ArrayList<>();

        // Iniciar la búsqueda recursiva DFS desde el nodo de partida.
        dfsRecursive(startId, endId, visited, path);
        
        // Convertir la lista de entidades `Location` a una lista de `LocationDTO`
        // para la respuesta final.
        return path.stream()
                   .map(loc -> new LocationDTO(loc.getId(), loc.getName(), loc.getAddress()))
                   .collect(Collectors.toList());
    }

    private boolean dfsRecursive(String currentId, String endId, Set<String> visited, List<Location> path) {
        // Marcar el nodo actual como visitado.
        visited.add(currentId);
        
        // Obtener la entidad `Location` actual de la base de datos.
        Location current = locationRepository.findById(currentId)
            .orElseThrow(() -> new RuntimeException("Location not found"));
        
        // Añadir la ubicación actual al camino que se está construyendo.
        path.add(current);

        // Si el nodo actual es el destino, hemos encontrado un camino.
        // Se retorna true para indicar a las llamadas recursivas anteriores que dejen de buscar.
        if (currentId.equals(endId)) {
            return true;
        }

        // Expansión: Recorrer todas las conexiones (vecinos) de la ubicación actual.
        for (ConnectConnection conn : current.getConnections()) {
            String nextId = conn.getDestination().getId();
            
            //Si el vecino no ha sido visitado, se realiza una llamada recursiva.
            if (!visited.contains(nextId)) {
                // Propagamos `true` para terminar la búsqueda.
                if (dfsRecursive(nextId, endId, visited, path)) {
                    return true;
                }
            }
        }
        
        // Backtracking: Si ninguna de las ramas a partir del nodo actual lleva al destino,
        // este nodo no forma parte del camino final. Por lo tanto, se elimina del camino
        // y se retorna `false`.
        path.remove(path.size() - 1);
        return false;
    }
}