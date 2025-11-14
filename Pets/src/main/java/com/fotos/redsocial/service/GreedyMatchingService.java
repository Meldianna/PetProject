package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.Specie;
import com.fotos.redsocial.entity.Status;
import com.fotos.redsocial.entity.Trait;
import com.fotos.redsocial.entity.User;
import com.fotos.redsocial.repository.AnimalRepository;
import com.fotos.redsocial.repository.UserRepository;

/**
 * ALGORITMO: Voraz (Greedy)
 *
 * CASO DE USO: "la app genera una especie de "perfect match2 entre los animales y los usuarios.
 * Esto puede ser utilizado para analizar la probabilidad de adopción o apadrinamiento de todos los animales en cualquier shelter.
 * Esto puede ayudar a los refugios a tomar decisiones informadas a identificar las chances de si sus animales podrían ser o no adoptados"
 * 
 * 
 * FUNCIONAMIENTO:
 * Un algoritmo voraz construye una solución paso a paso, eligiendo siempre la
 * opción que parece ser la mejor en ese momento, sin reconsiderar decisiones
 * pasadas. No garantiza encontrar la solución óptima global, pero a menudo
 * proporciona una aproximación buena y rápida.
 *
 * En este caso, el algoritmo intenta asignar la mejor mascota disponible a
 * cada usuario. Para hacerlo más justo, primero atiende a los usuarios con
 * menos opciones (los más "necesitados"), y luego les asigna la mascota
 * con la puntuación de compatibilidad más alta de las que aún no han sido asignadas.
 */
@Service
public class GreedyMatchingService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, List<String>> matchPetsToUsersGreedy() {
        // --- FASE 1: PREPARACIÓN DE DATOS ---
        
        // 1. Crear el mapa que almacenará las coincidencias finales (Usuario -> [Mascota]).
        Map<String, List<String>> matches = new LinkedHashMap<>();
        
        // 2. Obtener la lista de mascotas disponibles y todos los usuarios.
        List<Animal> availablePets = new ArrayList<>(animalRepository.findByStatus(Status.AVAILABLE));
        List<User> users = new ArrayList<>(userRepository.findAll());

        
        // --- FASE 2: CÁLCULO DE CANDIDATOS ---

        // 3. Para cada usuario, crear una lista de mascotas candidatas ordenadas
        // por puntuación de compatibilidad (de mayor a menor).
        Map<String, List<Animal>> candidates = new HashMap<>();
        for (User user : users) {
            List<Animal> userCandidates = availablePets.stream()
                .filter(p -> calculateMatchScore(user, p) > 0)
                .sorted((p1, p2) -> Integer.compare(calculateMatchScore(user, p2), calculateMatchScore(user, p1)))
                .collect(Collectors.toList());
            candidates.put(user.getId(), userCandidates);
        }

        
        // --- FASE 3: ORDENACIÓN DE USUARIOS (HEURÍSTICA GREEDY) ---

        // 4. Ordenar a los usuarios para priorizar a los que tienen menos opciones.
        // Esto aumenta la probabilidad de que más usuarios encuentren una mascota adecuada.
        users.sort(Comparator
            .comparingInt((User u) -> candidates.getOrDefault(u.getId(), Collections.emptyList()).size()) // Menos candidatos primero.
        );

        
        // --- FASE 4: PROCESO DE ASIGNACIÓN VORAZ ---
        
        // 5. Iterar sobre la lista de usuarios ordenados.
        for (User user : users) {
            List<Animal> userCandidates = candidates.getOrDefault(user.getId(), Collections.emptyList());
            
            // 6. Encontrar la mejor mascota *aún disponible* para este usuario.
            Animal bestChoice = null;
            for (Animal pet : userCandidates) {
                if (availablePets.contains(pet)) {
                    // La primera que se encuentre será la mejor, ya que la lista está ordenada por puntuación.
                    bestChoice = pet;
                    break;
                }
            }

            // 7. Si se encuentra una mascota adecuada y disponible.
            if (bestChoice != null) {
                // Asignarla al usuario.
                matches.put(user.getId(), new ArrayList<>(List.of(bestChoice.getId())));
                // Retirarla de la lista de mascotas disponibles para que no se asigne a otro usuario.
                availablePets.remove(bestChoice);
            }
        }
        
        // 8. Devolver el mapa de asignaciones.
        return matches;
    }

    private int calculateMatchScore(User user, Animal pet) {
        if (user == null || pet == null) return 0;
        int score = 0;
        
        // Otorga 10 puntos si la especie de la mascota coincide con una de las especies que busca el usuario.
        if (user.getLookingFor() != null && pet.getSpecie() != null) {
            String petSpecieId = pet.getSpecie().getId();
            for (Specie s : user.getLookingFor()) {
                if (petSpecieId.equals(s.getId())) {
                    score += 10;
                    break;
                }
            }
        }
        
        // Otorga 5 puntos por cada rasgo preferido del usuario que la mascota posea.
        if (user.getPreferredTraits() != null && pet.getTraits() != null) {
            Set<String> userTraitIds = user.getPreferredTraits().stream()
                .filter(Objects::nonNull).map(Trait::getId)
                .collect(Collectors.toSet());
            for (Trait trait : pet.getTraits()) {
                if (userTraitIds.contains(trait.getId())) {
                    score += 5;
                }
            }
        }
        return score;
    }
}