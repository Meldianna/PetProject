package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotos.redsocial.entity.Animal;
import com.fotos.redsocial.entity.Specie;
import com.fotos.redsocial.entity.Status;
import com.fotos.redsocial.entity.Trait;
import com.fotos.redsocial.entity.User;
import com.fotos.redsocial.entity.dto.responses.AnimalScore;
import com.fotos.redsocial.repository.AnimalRepository;
import com.fotos.redsocial.repository.UserRepository;

/**
 * ALGORITMO: Backtracking (Vuelta Atrás)
 *
 *CASO DE USO: "Como usuario quisiera adoptar más de un animal y conocer las diferentes opciones para adoptarlos.
 * El algoritmo sugiere las diferentes posibilidades de adopción para el usuario según sus preferencias"
 *
 * FUNCIONAMIENTO:
 * El backtracking es una técnica algorítmica para encontrar soluciones a problemas
 * que tienen restricciones, construyendo soluciones candidatas de forma incremental.
 * Abandona una candidata ("retrocede" o "backtracks") tan pronto como determina
 * que no puede completarse para ser una solución válida.
 *
 * Se puede visualizar como una búsqueda en profundidad (DFS) en un árbol de
 * espacio de búsqueda. En cada paso, se explora una opción, y si esa opción

 * lleva a un callejón sin salida (no cumple las restricciones), se deshace
 * y se prueba la siguiente opción.
 *
 * Aquí, se usa para encontrar todas las combinaciones de hasta 3 mascotas
 * cuya suma de "puntuaciones de compatibilidad" con un usuario supere un
 * umbral determinado.
 */
@Service
public class BacktrackingService {

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<List<String>> findAdoptionOptions(String userId) {
        // --- FASE 1: PREPARACIÓN ---

        // 1. `result`: Lista final que contendrá todas las combinaciones válidas.
        List<List<String>> result = new ArrayList<>();
        // 2. Obtener los datos necesarios: usuario y mascotas disponibles.
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        List<Animal> availablePets = animalRepository.findByStatus(Status.AVAILABLE);

        // 3. Pre-calcular la puntuación de compatibilidad para cada mascota
        // y filtrar las que no tienen ninguna compatibilidad.
        List<AnimalScore> animalScores = availablePets.stream()
            .map(animal -> new AnimalScore(animal, calculateMatchScore(user, animal)))
            .filter(as -> as.getScore() > 0)
            .collect(Collectors.toList());
        
        // 4. Iniciar el proceso recursivo de backtracking.
        backtrack(result, new ArrayList<>(), animalScores, 0, 0);
        return result;
    }

    private void backtrack(List<List<String>> result, List<String> currentCombination,
                        List<AnimalScore> animalScores, int start, int currentScore) {
        // --- CASO BASE DE ÉXITO ---
        // 1. Si la puntuación actual ya supera el umbral, hemos encontrado una combinación válida.
        // Se añade una copia de la combinación actual a la lista de resultados.
        if (currentScore > 5) {
            result.add(new ArrayList<>(currentCombination));
        }

        // --- CONDICIONES DE PODA (PRUNING) / CASO BASE DE FALLO ---
        // 2. Si ya hemos seleccionado 3 mascotas o hemos considerado todas las mascotas disponibles,
        // no podemos seguir por esta rama. Se detiene la recursión para esta ruta.
        if (currentCombination.size() >= 3 || start >= animalScores.size()) {
            return;
        }

        // --- EXPLORACIÓN RECURSIVA ---
        // 3. Iterar a través de las mascotas restantes para crear nuevas combinaciones.
        for (int i = start; i < animalScores.size(); i++) {
            AnimalScore petScore = animalScores.get(i);
            
            // 4. ACCIÓN: Elegir una mascota y añadirla a la combinación actual.
            currentCombination.add(petScore.getAnimal().getId());
            
            // 5. LLAMADA RECURSIVA: Explorar las siguientes opciones.
            // Pasamos `i + 1` para no reutilizar la misma mascota y evitar duplicados.
            backtrack(result, currentCombination, animalScores, i + 1, currentScore + petScore.getScore());
            
            // 6. BACKTRACK: Deshacer la elección. Se elimina la última mascota añadida
            // para poder explorar otras ramas del árbol de búsqueda.
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
    
    // Método de ayuda para calcular la compatibilidad
    private int calculateMatchScore(User user, Animal pet) {
        // (La lógica es la misma que en el servicio Greedy)
        if (user == null || pet == null) return 0;
        int score = 0;
        if (user.getLookingFor() != null && pet.getSpecie() != null) {
            if (user.getLookingFor().stream().anyMatch(s -> s.getId().equals(pet.getSpecie().getId()))) {
                score += 10;
            }
        }
        if (user.getPreferredTraits() != null && pet.getTraits() != null) {
            Set<String> userTraitIds = user.getPreferredTraits().stream().map(Trait::getId).collect(Collectors.toSet());
            score += pet.getTraits().stream().filter(t -> userTraitIds.contains(t.getId())).count() * 5;
        }
        return score;
    }
}