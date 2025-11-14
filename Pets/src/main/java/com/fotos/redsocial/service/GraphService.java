/* 
package com.fotos.redsocial.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.Edge;
import com.fotos.redsocial.entity.Location;
import com.fotos.redsocial.entity.Node;
import com.fotos.redsocial.entity.Shelter;
import com.fotos.redsocial.entity.Specie;
import com.fotos.redsocial.entity.Status;
import com.fotos.redsocial.entity.Trait;
import com.fotos.redsocial.entity.User;
import com.fotos.redsocial.entity.dto.responses.AnimalScore;
import com.fotos.redsocial.entity.dto.responses.BfsState;
import com.fotos.redsocial.entity.dto.responses.EventDTO;
import com.fotos.redsocial.entity.dto.responses.FriendPairDTO;
import com.fotos.redsocial.entity.dto.responses.LocationDTO;
import com.fotos.redsocial.entity.dto.responses.MSTEdgeResponse;
import com.fotos.redsocial.entity.dto.responses.MSTResponse;
import com.fotos.redsocial.entity.dto.responses.PathResponse;
import com.fotos.redsocial.entity.dto.responses.ShelterDTO;
import com.fotos.redsocial.entity.dto.responses.SocialConnectionResponse;
import com.fotos.redsocial.entity.relationship.ConnectConnection;
import com.fotos.redsocial.repository.AnimalRepository;
import com.fotos.redsocial.repository.EventRepository;
import com.fotos.redsocial.repository.LocationRepository;
import com.fotos.redsocial.repository.ShelterRepository;
import com.fotos.redsocial.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class GraphService {
    
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private EventRepository eventRepository;


    //BFS
    public Optional<SocialConnectionResponse> findSocialConnection(String startUserEmail, String targetAnimalId) {

        // Validamos que el usuario de inicio exista
        User user = userRepository.findByEmail(startUserEmail);
        if (user == null){throw new RuntimeException("El usuario no fue encontrado para el BFS");}
        
        // 1a. Carga todos los pares de amigos de la BD
        List<FriendPairDTO> pairs = userRepository.getAllFriendshipPairs();
        
        // 1b. Construye el grafo en memoria (un Map)
        Map<String, Set<String>> socialGraph = new HashMap<>();
        for (FriendPairDTO pair : pairs) {
            // Añade la conexión en ambas direcciones
            socialGraph.computeIfAbsent(pair.getUserEmail(), k -> new HashSet<>()).add(pair.getFriendEmail());
            socialGraph.computeIfAbsent(pair.getFriendEmail(), k -> new HashSet<>()).add(pair.getUserEmail());
        }

        // Hacemos 1 consulta para saber quiénes son los "ganadores"
        Set<SocialConnectionResponse> connections = userRepository.findAllUserConnectedToAnimal(targetAnimalId);

        //parseo al mapa del grafo en memoria
       // Convierte el Set en un Map (Email -> TipoDeConexión) para búsquedas O(1)
        Map<String, String> goalUsers = connections.stream()
            .collect(Collectors.toMap(
                SocialConnectionResponse::getFriendEmail, 
                SocialConnectionResponse::getConnectionType,
                (type1, type2) -> type1 + " / " + type2 // (Maneja si un usuario tiene múltiples conexiones)
            ));

        // Si no hay nadie conectado a ese animal, no hay nada que buscar.
        if (goalUsers.isEmpty()) {
            return Optional.empty();
        }

        Queue<BfsState> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(new BfsState(startUserEmail, 0));
        visited.add(startUserEmail);

        // --- FASE 3: BUCLE BFS (¡CORREGIDO!) ---
        while (!queue.isEmpty()) {
            BfsState currentState = queue.poll();
            String currentUserEmail = currentState.userEmail();
            int currentDistance = currentState.distance();

            // 4. LÓGICA DE "OBJETIVO"
            // Comprobación instantánea contra el Set en memoria.
            if (goalUsers.containsKey(currentUserEmail)) {
                String realConnectionType = goalUsers.get(currentUserEmail);
                // ¡Encontrado!
                User userNode = userRepository.findByEmail(currentUserEmail); 
                return Optional.of(new SocialConnectionResponse(
                    userNode.getEmail(),
                    realConnectionType
                ));
            }

        // 5. LÓGICA DE EXPANSIÓN (SOBRE EL MAP, NO LA BD)
            if (currentDistance >= 3) {
                continue; // Límite de 3 saltos
            }
            
            // ¡EFICIENTE! Esto lee del Map en memoria (0 consultas)
            Set<String> neighbors = socialGraph.getOrDefault(currentUserEmail, Collections.emptySet());
            
            for (String friendEmail : neighbors) {
                if (!visited.contains(friendEmail)) {
                    visited.add(friendEmail);
                    queue.offer(new BfsState(friendEmail, currentDistance + 1));
                }
            }
        }
        
        return Optional.empty(); // No se encontró conexión
    }



    //DFS
    public List<LocationDTO> dfsPath(String startId, String endId) {
        // Utiliza un conjunto para registrar las ubicaciones visitadas y una lista para guardar el camino.
        Set<String> visited = new HashSet<>();
        List<Location> path = new ArrayList<>();
        // Inicia la búsqueda recursiva DFS.
        dfsRecursive(startId, endId, visited, path);
        // Convierte la lista de entidades Location a una lista de DTOs para la respuesta.
        return path.stream()
                .map(loc -> new LocationDTO(
                        loc.getId(),
                        loc.getName(),
                        loc.getAddress()))
                .collect(Collectors.toList());
    }

    private void dfsRecursive(String currentId, String endId, Set<String> visited, List<Location> path) {
        // Obtiene la ubicación actual y la marca como visitada, añadiéndola al camino.
        Location current = locationRepository.findById(currentId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        visited.add(currentId);
        path.add(current);

        // Si se llega al destino, la recursión para esa rama termina.
        if (currentId.equals(endId))
            return;

        // Recorre las conexiones de la ubicación actual.
        for (ConnectConnection conn : current.getConnections()) {
            String nextId = conn.getDestination().getId();
            // Si la ubicación vecina no ha sido visitada, se llama recursivamente.
            if (!visited.contains(nextId)) {
                dfsRecursive(nextId, endId, visited, path);
                // Si el último elemento del camino es el destino, detiene la exploración adicional.
                if (!path.isEmpty() && path.get(path.size() - 1).getId().equals(endId)) {
                    return;
                }
            }
        }
        // Si no se encuentra un camino al destino desde el nodo actual, se elimina del camino (backtracking).
        path.remove(path.size() - 1);
    }


    //DIJKSTRA
    public PathResponse findShortestPath(String startId, String endId) {
        // Mapas para almacenar las distancias más cortas y el predecesor de cada nodo.
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        // Cola de prioridad para seleccionar el nodo con la menor distancia.
        PriorityQueue<Node> pq = new PriorityQueue<>();

        // Inicializa todas las distancias como infinito, excepto la del nodo de inicio (0).
        locationRepository.findAll().forEach(loc -> distances.put(loc.getId(), Double.MAX_VALUE));
        distances.put(startId, 0.0);
        Location startLocation = locationRepository.findById(startId)
                .orElseThrow(() -> new RuntimeException("Start location not found"));
        pq.offer(new Node(startLocation, 0.0));

        // El algoritmo se ejecuta hasta que la cola de prioridad esté vacía o se llegue al nodo final.
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String currentId = current.getLocation().getId();

            if (currentId.equals(endId))
                break;

            // Se exploran los vecinos del nodo actual.
            for (ConnectConnection conn : current.getLocation().getConnections()) {
                String nextId = conn.getDestination().getId();
                // Se calcula la nueva distancia al vecino.
                double newDist = distances.get(currentId) + conn.getDistance();

                // Si se encuentra un camino más corto, se actualiza la distancia y el predecesor.
                if (newDist < distances.getOrDefault(nextId, Double.MAX_VALUE)) {
                    distances.put(nextId, newDist);
                    previous.put(nextId, currentId);
                    pq.offer(new Node(conn.getDestination(), newDist));
                }
            }
        }
        // Se reconstruye el camino más corto a partir del mapa de predecesores.
        return reconstructPath(previous, startId, endId);
    }

    private PathResponse reconstructPath(Map<String, String> previous, String startId, String endId) {
        // Reconstruye el camino desde el final hasta el inicio y calcula la distancia total.
        List<LocationDTO> pathLocations = new ArrayList<>();
        double totalDistance = 0.0;
        String currentId = endId;

        // Se recorre el mapa de predecesores desde el nodo destino hasta el origen.
        while (currentId != null) {
            Location current = locationRepository.findById(currentId)
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            pathLocations.add(0, new LocationDTO(
                    current.getId(),
                    current.getName(),
                    current.getAddress()));
            String prevId = previous.get(currentId);

            // Si hay un predecesor, se suma la distancia de la arista al total.
            if (prevId != null) {
                Location prev = locationRepository.findById(prevId)
                        .orElseThrow(() -> new RuntimeException("Location not found"));
                for (ConnectConnection conn : prev.getConnections()) {
                    if (conn.getDestination().getId().equals(currentId)) {
                        totalDistance += conn.getDistance();
                        break;
                    }
                }
            }
            currentId = prevId;
        }
        return new PathResponse(pathLocations, totalDistance);
    }



    //MST - PRIM'S ALGORITHM
    public MSTResponse getMinimumSpanningTree() {
        // Lista para guardar las aristas del Árbol de Expansión Mínima (MST) y un conjunto para los nodos visitados.
        List<MSTEdgeResponse> mstEdges = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        // Cola de prioridad para almacenar las aristas ordenadas por peso (distancia).
        PriorityQueue<Edge> pq = new PriorityQueue<>(
                Comparator.comparingDouble(Edge::getWeight));

        // Se elige una ubicación de inicio arbitraria.
        Location start = locationRepository.findAll()
                .iterator()
                .next();
        visited.add(start.getId());

        // Se añaden todas las aristas del nodo inicial a la cola de prioridad.
        for (ConnectConnection conn : start.getConnections()) {
            pq.offer(new Edge(
                    start.getId(),
                    conn.getDestination().getId(),
                    conn.getDistance()));
        }

        // El bucle se ejecuta hasta que la cola esté vacía o se hayan visitado todos los nodos.
        while (!pq.isEmpty() && visited.size() < locationRepository.count()) {
            Edge edge = pq.poll();

            // Si el destino de la arista ya fue visitado, se ignora para evitar ciclos.
            if (visited.contains(edge.getDestination())) {
                continue;
            }

            // La arista se añade al MST y el nodo destino se marca como visitado.
            mstEdges.add(new MSTEdgeResponse(
                    edge.getSource(),
                    edge.getDestination(),
                    edge.getWeight()));
            visited.add(edge.getDestination());

            // Se añaden las nuevas aristas desde el nodo recién visitado.
            Location current = locationRepository.findById(edge.getDestination())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            for (ConnectConnection conn : current.getConnections()) {
                if (!visited.contains(conn.getDestination().getId())) {
                    pq.offer(new Edge(
                            current.getId(),
                            conn.getDestination().getId(),
                            conn.getDistance()));
                }
            }
        }
        return new MSTResponse(mstEdges);
    }

    


    //GREEDY
    @Transactional
    public Map<String, List<String>> matchPetsToUsersGreedy() {
        // Mapa para almacenar las coincidencias finales y listas de mascotas disponibles y usuarios.
        Map<String, List<String>> matches = new LinkedHashMap<>();
        List<Animal> availablePets = new ArrayList<>(animalRepository.findByStatus(Status.AVAILABLE));
        List<User> users = new ArrayList<>(userRepository.findAll());

        // Para cada usuario, se crea una lista de mascotas candidatas ordenada por puntuación de coincidencia.
        Map<String, List<Animal>> candidates = new HashMap<>();
        for (User user : users) {
            List<Animal> cand = availablePets.stream()
                    .filter(p -> calculateMatchScore(user, p) > 0)
                    .sorted((p1, p2) -> Integer.compare(calculateMatchScore(user, p2), calculateMatchScore(user, p1)))
                    .collect(Collectors.toList());
            candidates.put(user.getId(), cand);
        }

        // Se ordenan los usuarios para priorizar aquellos con menos candidatos.
        users.sort(Comparator
                .comparingInt((User u) -> candidates.getOrDefault(u.getId(), Collections.emptyList()).size())
                .thenComparing((User u) -> -((u.getLookingFor() != null ? u.getLookingFor().size() : 0)
                        + (u.getPreferredTraits() != null ? u.getPreferredTraits().size() : 0)))
                .thenComparing(User::getId));

        // Se asigna a cada usuario la mejor mascota disponible de su lista de candidatos.
        for (User user : users) {
            List<Animal> cand = candidates.getOrDefault(user.getId(), Collections.emptyList());
            Animal chosen = null;
            int bestScore = 0;

            for (Animal pet : cand) {
                if (!availablePets.contains(pet))
                    continue;

                int score = calculateMatchScore(user, pet);
                if (score > bestScore || (score == bestScore && chosen != null && pet.getId().compareTo(chosen.getId()) < 0)) {
                    bestScore = score;
                    chosen = pet;
                }
            }

            // Si se encuentra una mascota, se registra la coincidencia y se retira la mascota de la lista de disponibles.
            if (chosen != null) {
                matches.put(user.getId(), new ArrayList<>(List.of(chosen.getId())));
                availablePets.remove(chosen);
            }
        }
        return matches;
    }

    private int calculateMatchScore(User user, Animal pet) {
        // Calcula una puntuación de compatibilidad entre un usuario y una mascota.
        if (user == null || pet == null)
            return 0;

        int score = 0;
        // Se otorgan puntos si la especie de la mascota coincide con la que busca el usuario.
        if (user.getLookingFor() != null && pet.getSpecie() != null) {
            String petSpecieId = pet.getSpecie().getId();
            for (Specie s : user.getLookingFor()) {
                if (petSpecieId.equals(s.getId())) {
                    score += 10;
                    break;
                }
            }
        }
        // Se otorgan puntos por cada rasgo preferido del usuario que la mascota posea.
        if (user.getPreferredTraits() != null && pet.getTraits() != null) {
            Set<String> userTraitIds = user.getPreferredTraits().stream()
                    .filter(Objects::nonNull)
                    .map(Trait::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            for (Trait trait : pet.getTraits()) {
                if (userTraitIds.contains(trait.getId())) {
                    score += 5;
                }
            }
        }
        return score;
    }

    


    //QUICKSORT
    @Transactional
    public List<ShelterDTO> sortSheltersByCapacity() {
        // Obtiene todos los refugios y los ordena por capacidad usando QuickSort.
        List<Shelter> shelters = shelterRepository.findAll();
        if (shelters == null || shelters.isEmpty()) {
            return Collections.emptyList();
        }
        quickSort(shelters, 0, shelters.size() - 1);
        // Convierte la lista ordenada a una lista de DTOs.
        return shelters.stream()
                .map(s -> new ShelterDTO(s.getId(), s.getName(), s.getCapacity()))
                .collect(Collectors.toList());
    }

    private void quickSort(List<Shelter> shelters, int low, int high) {
        // Implementación de QuickSort no recursiva usando una pila para gestionar los sub-arrays.
        if (shelters == null || shelters.size() <= 1 || low >= high)
            return;
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] { low, high });
        while (!stack.isEmpty()) {
            int[] range = stack.pop();
            int l = range[0], h = range[1];
            if (l >= h)
                continue;
            // Realiza la partición del sub-array.
            int pi = partition(shelters, l, h);
            // Empuja los nuevos rangos a la pila para ser ordenados.
            int leftSize = pi - 1 - l;
            int rightSize = h - (pi + 1);
            if (leftSize > rightSize) {
                if (l < pi - 1)
                    stack.push(new int[] { l, pi - 1 });
                if (pi + 1 < h)
                    stack.push(new int[] { pi + 1, h });
            } else {
                if (pi + 1 < h)
                    stack.push(new int[] { pi + 1, h });
                if (l < pi - 1)
                    stack.push(new int[] { l, pi - 1 });
            }
        }
    }

    private int partition(List<Shelter> shelters, int low, int high) {
        // Particiona la lista de refugios en base a un pivote (la capacidad del último elemento).
        int pivot = shelters.get(high).getCapacity();
        int i = low - 1;
        // Reorganiza los elementos de manera que los menores que el pivote queden a la izquierda y los mayores a la derecha.
        for (int j = low; j < high; j++) {
            if (shelters.get(j).getCapacity() <= pivot) {
                i++;
                Collections.swap(shelters, i, j);
            }
        }
        // Coloca el pivote en su posición correcta.
        Collections.swap(shelters, i + 1, high);
        return i + 1;
    }

    



    //PROGRAMACION DINAMICA
    @Transactional
    public List<EventDTO> optimizeEventEnrrollement(String shelterId) {
        try {
            // Valida y obtiene la información del refugio.
            Shelter shelter = shelterRepository.findById(shelterId)
                    .orElseThrow(() -> new RuntimeException("Refugio no encontrado con id: " + shelterId));
            // Obtiene los eventos cercanos al refugio.
            List<EventDTO> nearbyEvents = eventRepository.findNearbyEventsForShelter(shelterId)
                    .stream()
                    .map(e -> new EventDTO(
                            e.getId(),
                            e.getName(),
                            e.getDate(),
                            e.getAnimalEachShelter(),
                            e.getVolunteersNeeded()))
                    .collect(Collectors.toList());

            int n = nearbyEvents.size();
            // Obtiene la cantidad de animales y voluntarios disponibles en el refugio.
            int maxAnimals = shelterRepository.countAnimalsForShelter(shelterId);
            int maxVolunteers = shelterRepository.countVolunteersForShelter(shelterId);

            if (maxAnimals < 0 || maxVolunteers < 0) {
                return new ArrayList<>();
            }
            if (n == 0) {
                return new ArrayList<>();
            }

            // Crea la tabla de Programación Dinámica para almacenar los resultados óptimos.
            int[][][] dp = new int[n + 1][maxVolunteers + 1][maxAnimals + 1];

            // Resuelve el problema utilizando la tabla de DP y reconstruye la solución.
            List<EventDTO> resultado = solveDP(dp, nearbyEvents, n, maxAnimals, maxVolunteers);

            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Error al optimizar participación de eventos: " + e.getMessage(), e);
        }
    }


    private List<EventDTO> solveDP(int[][][] dp, List<EventDTO> eventos, int n, int maxAnimals, int maxVolunteers) {
        // Rellena la tabla de DP para determinar la máxima cantidad de eventos a los que se puede asistir.
        for (int evento = 1; evento <= n; evento++) {
            EventDTO eventoActual = eventos.get(evento - 1);
            int animalesRequeridos = eventoActual.getAnimalEachShelter();
            int voluntariosRequeridos = eventoActual.getVolunteersNeeded();
            int valor = 1; // Cada evento tiene un valor de 1 (simplemente se maximiza la cantidad).

            for (int voluntario = 0; voluntario <= maxVolunteers; voluntario++) {
                for (int animal = 0; animal <= maxAnimals; animal++) {
                    // Opción 1: No participar en el evento actual.
                    int valorSin = dp[evento - 1][voluntario][animal];
                    // Opción 2: Participar en el evento si hay recursos suficientes.
                    int valorCon = 0;
                    if (voluntario >= voluntariosRequeridos && animal >= animalesRequeridos) {
                        valorCon = valor
                                + dp[evento - 1][voluntario - voluntariosRequeridos][animal - animalesRequeridos];
                    }
                    // Se elige la mejor opción.
                    dp[evento][voluntario][animal] = Math.max(valorSin, valorCon);
                }
            }
        }
        // Reconstruye la lista de eventos seleccionados a partir de la tabla de DP.
        List<EventDTO> eventosEscogidos = new ArrayList<>();
        int i = n;
        int v = maxVolunteers;
        int a = maxAnimals;
        while (i > 0) {
            // Si el valor cambia al excluir el evento actual, significa que fue incluido en la solución óptima.
            if (dp[i][v][a] != dp[i - 1][v][a]) {
                EventDTO event = eventos.get(i - 1);

                if (v < event.getVolunteersNeeded() || a < event.getAnimalEachShelter()) {
                    i--;
                    continue;
                }
                eventosEscogidos.add(event);
                // Se restan los recursos utilizados por el evento.
                v = v - event.getVolunteersNeeded();
                a = a - event.getAnimalEachShelter();

                if (v < 0 || a < 0) {
                    break;
                }
                i--;
            } else {
                // Si el valor no cambia, el evento no fue incluido, así que se pasa al anterior.
                i--;
            }
        }
        // Se invierte la lista para mantener el orden original.
        Collections.reverse(eventosEscogidos);

        if (dp[n][maxVolunteers][maxAnimals] == 0) {
        }
        return eventosEscogidos;
    }

    



    //BACKTRACKING
    @Transactional
    public List<List<String>> findAdoptionOptions(String userId) {
        // Encuentra combinaciones de mascotas para un usuario.
        List<List<String>> result = new ArrayList<>();
        List<Animal> availablePets = animalRepository.findByStatus(Status.AVAILABLE);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Se calcula la puntuación de compatibilidad para cada mascota disponible.
        List<AnimalScore> animalScores = availablePets.stream()
                .map(animal -> new AnimalScore(animal, calculateMatchScore(user, animal)))
                .filter(animal -> animal.getScore() > 0)
                .collect(Collectors.toList());
        // Se inicia la función de backtracking.
        backtrack(result, new ArrayList<>(), animalScores, user, 0, 0);
        return result;
    }

    private void backtrack(List<List<String>> result, List<String> current, List<AnimalScore> animalScores,
            User user, int start, int currentScore) {
        // Caso base de éxito: si la puntuación acumulada es suficiente, se añade la combinación a los resultados.
        if (currentScore > 5) {
            result.add(new ArrayList<>(current));
        }

        // Poda: si se excede el tamaño máximo o se han explorado todas las mascotas, se detiene la recursión.
        if (current.size() >= 3 || start >= animalScores.size())
            return;

        // Se exploran recursivamente las combinaciones posibles añadiendo una mascota a la vez.
        for (int i = start; i < animalScores.size(); i++) {
            AnimalScore pet = animalScores.get(i);
            current.add(pet.getAnimal().getId());
            // Llamada recursiva con la nueva mascota.
            backtrack(result, current, animalScores, user, i + 1, currentScore + pet.getScore());
            // Se elimina la mascota para explorar otras ramas del árbol de búsqueda (backtrack).
            current.remove(current.size() - 1);
        }
    }





    //BRANCH AND BOUND
    public PathResponse findOptimalShelterRoute(String startId, String userEmail) {
        // Encuentra la ruta óptima que visita todas las ubicaciones con refugios.
        Location start = locationRepository.findById(startId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        List<Location> shelterLocations = locationRepository.findTargetLocationsForUser(userEmail);
        List<Location> bestRoute = new ArrayList<>();
        double[] bestCost = { Double.MAX_VALUE };

        // Conjunto de IDs de las ubicaciones objetivo para una búsqueda más rápida.
        Set<String> targetIds = shelterLocations.stream()
                .map(Location::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Se establece una profundidad máxima para evitar recursiones infinitas.
        long maxDepth = locationRepository.count() + 5;
        // Se inicia el algoritmo de ramificación y poda.
        branchAndBound(start, new ArrayList<>(List.of(start)), new HashSet<>(List.of(startId)), 0.0, targetIds,
                bestRoute, bestCost, maxDepth, start);

        // Se convierte la ruta óptima encontrada a una lista de DTOs.
        List<LocationDTO> locationDTOs = bestRoute.stream()
                .map(loc -> new LocationDTO(loc.getId(), loc.getName(), loc.getAddress()))
                .collect(Collectors.toList());

        return new PathResponse(locationDTOs, bestCost[0]);
    }

    private void branchAndBound(Location current, List<Location> currentPath, Set<String> visited, double currentCost,
            Set<String> targetIds, List<Location> bestPath, double[] bestCost, long maxDepth, Location startNode) {
        // Se evita una recursión excesiva.
        if (currentPath.size() > maxDepth)
            return;

        // Si se han visitado todos los refugios, se actualiza la mejor ruta si el costo actual es menor.
        if (visited.containsAll(targetIds)) {
            Double returnCost = locationRepository.getDistanceBetween(current.getId(), startNode.getId());

            if (returnCost == null){return;} //no hay conexión de vuelta al inicio. Se descarta esta rama.
            double totalCost = currentCost + returnCost;
            
            if (totalCost < bestCost[0]) {
                bestPath.clear();
                bestPath.addAll(currentPath);
                bestPath.add(startNode); // Añade el nodo de inicio al final para cerrar el ciclo.
                bestCost[0] = totalCost;
            }
            return;
        }

        // Poda por costo: si el costo actual ya es mayor que el de la mejor ruta encontrada, se detiene la exploración de esta rama.
        if (currentCost >= bestCost[0])
            return;
        
        List<ConnectConnection> connections = current.getConnections();
        if (connections == null || connections.isEmpty())
            return;
        // Se exploran las ubicaciones vecinas.
        for (ConnectConnection conn : connections) {
            if (conn == null) continue;
            Location next = conn.getDestination();
            if (next == null) continue;
            String nextId = next.getId();
            if (nextId == null) continue;
            
            // Se evitan ciclos.
            if (visited.contains(nextId))
                continue;

            if (nextId.equals(current.getId()))
                continue;
            
            Double distance = conn.getDistance();
            if (distance == null) continue;

            visited.add(nextId);
            currentPath.add(next);
            // Llamada recursiva para el siguiente nodo en la ruta.
            branchAndBound(next, currentPath, visited, currentCost + distance, targetIds, bestPath, bestCost,
                    maxDepth, startNode);
            // Backtracking: se deshacen los cambios para explorar otras rutas.
            visited.remove(nextId);
            currentPath.remove(currentPath.size() - 1);
        }
    }
}
*/