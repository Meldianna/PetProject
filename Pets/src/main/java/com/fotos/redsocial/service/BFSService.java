package com.fotos.redsocial.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.User;
import com.fotos.redsocial.entity.dto.responses.BfsState;
import com.fotos.redsocial.entity.dto.responses.FriendPairDTO;
import com.fotos.redsocial.entity.dto.responses.SocialConnectionResponse;
import com.fotos.redsocial.repository.UserRepository;

/**
 * ALGORITMO: Búsqueda en Anchura (BFS - Breadth-First Search)
 *
 * CASO DE USO: "A fin de generar una red de confianza, se puede mostrar qué tan conectado está un usuario con un animal.
 * Se traduce a casos donde los usuario quieren adoptar o apadrinar un animal, pero prefiere tener referencias de aquel animal"
 * 
 * FUNCIONAMIENTO:
 * BFS es un algoritmo de recorrido de grafos que explora todos los nodos vecinos a
 * una distancia determinada antes de moverse a los nodos de la siguiente distancia.
 * Utiliza una cola (Queue) para llevar un registro de los nodos a visitar.
 *
 * En este caso, lo usamos para encontrar la conexión de "amigos" más cercana
 * (hasta 3 grados de separación) entre un usuario inicial y cualquier usuario
 * que haya adoptado o esté apadrinando a un animal específico. 
 */
@Service
public class BFSService {

    @Autowired
    private UserRepository userRepository;

    public Optional<SocialConnectionResponse> findSocialConnection(String startUserEmail, String targetAnimalId) {
        // --- FASE 1: PREPARACIÓN DEL GRAFO Y OBJETIVOS ---

        // 1. Validar que el usuario de inicio exista.
        User user = userRepository.findByEmail(startUserEmail);
        if (user == null) {
            throw new RuntimeException("El usuario no fue encontrado para el BFS");
        }
        
        // 2. Cargar todos los pares de amigos de la BD para construir el grafo.
        List<FriendPairDTO> pairs = userRepository.getAllFriendshipPairs();
        
        // 3. Construir el grafo social en memoria (un Mapa de Adyacencia).
        // La clave es el email de un usuario y el valor es un conjunto de emails de sus amigos.
        Map<String, Set<String>> socialGraph = new HashMap<>();
        for (FriendPairDTO pair : pairs) {
            // Se añade la conexión en ambas direcciones para representar una amistad mutua.
            socialGraph.computeIfAbsent(pair.getUserEmail(), k -> new HashSet<>()).add(pair.getFriendEmail());
            socialGraph.computeIfAbsent(pair.getFriendEmail(), k -> new HashSet<>()).add(pair.getUserEmail());
        }

        // 4. Obtener los usuarios "objetivo" (los que tienen conexión con el animal).
        // Hacemos una única consulta para optimizar.
        Set<SocialConnectionResponse> connections = userRepository.findAllUserConnectedToAnimal(targetAnimalId);
        
        // Si no hay nadie conectado a ese animal, no es necesario buscar.
        if (connections.isEmpty()) {
            return Optional.empty();
        }
        
        // 5. Convertir el conjunto de conexiones en un mapa para búsquedas rápidas (O(1)).
        Map<String, String> goalUsers = connections.stream()
            .collect(Collectors.toMap(
                SocialConnectionResponse::getFriendEmail, 
                SocialConnectionResponse::getConnectionType
            ));


        // --- FASE 2: INICIALIZACIÓN DE BFS ---

        // 6. Inicializar la cola con el estado inicial (usuario de partida, distancia 0).
        Queue<BfsState> queue = new LinkedList<>();
        queue.offer(new BfsState(startUserEmail, 0));
        
        // 7. Inicializar un conjunto de 'visitados' para no procesar al mismo usuario dos veces y evitar ciclos.
        Set<String> visited = new HashSet<>();
        visited.add(startUserEmail);

        // --- FASE 3: BUCLE PRINCIPAL DE BFS ---
        while (!queue.isEmpty()) {
            // 8. Sacar el siguiente estado de la cola para procesarlo.
            BfsState currentState = queue.poll();
            String currentUserEmail = currentState.userEmail();
            int currentDistance = currentState.distance();

            // 9. LÓGICA DE "OBJETIVO": Verificar si el usuario actual es uno de los objetivos.
            if (goalUsers.containsKey(currentUserEmail)) {
                // ¡Éxito! Hemos encontrado la conexión más cercana.
                String connectionType = goalUsers.get(currentUserEmail);
                User userNode = userRepository.findByEmail(currentUserEmail); 
                return Optional.of(new SocialConnectionResponse(userNode.getEmail(), connectionType));
            }

            // 10. LÓGICA DE EXPANSIÓN: Añadir los vecinos a la cola.
            // Detener la búsqueda si la distancia excede el límite (3 saltos).
            if (currentDistance >= 3) {
                continue; 
            }
            
            // 11. Obtener los vecinos del usuario actual desde el grafo en memoria (muy eficiente).
            Set<String> neighbors = socialGraph.getOrDefault(currentUserEmail, Collections.emptySet());
            
            // 12. Recorrer los vecinos.
            for (String friendEmail : neighbors) {
                // Si el amigo no ha sido visitado, se añade a la cola y al conjunto de visitados.
                if (!visited.contains(friendEmail)) {
                    visited.add(friendEmail);
                    queue.offer(new BfsState(friendEmail, currentDistance + 1));
                }
            }
        }
        
        // 13. Si el bucle termina y no se encontró conexión, se retorna vacío.
        return Optional.empty(); 
    }
}