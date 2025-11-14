package com.fotos.redsocial.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fotos.redsocial.entity.Shelter;
import com.fotos.redsocial.entity.dto.responses.EventDTO;
import com.fotos.redsocial.repository.EventRepository;
import com.fotos.redsocial.repository.ShelterRepository;

/**
 * ALGORITMO: Programación Dinámica (Problema de la Mochila 0/1)
 *
 * CASO DE USO: "Optimizar la asignación de recursos de shelters (personal, vehículos, capacidad) entre varios eventos sin exceder límites.
 * Cada shelter tiene una capacidad de recursos (horas voluntarios, dinero, transporte) y cada evento requiere cierta cantidad de esos recursos.
 * Le permitiría a cada shelter planificar mejor a qué eventos asistir maximizando su visibilidad sin pasarse de sus recursos."
 * 
 * FUNCIONAMIENTO:
 * La programación dinámica es una técnica para resolver problemas complejos
 * dividiéndolos en subproblemas más simples y superpuestos. Almacena las
 * soluciones de los subproblemas para evitar recalcularlas.
 *
 * Este problema es una variación del "Problema de la Mochila 0/1 con Múltiples
 * Restricciones". El objetivo es seleccionar un subconjunto de "ítems" (eventos)
 * para maximizar un "valor" (cantidad de eventos), sujeto a múltiples
 * "pesos" o restricciones (animales y voluntarios disponibles).
 *
 * Se construye una tabla de DP (dp[evento][voluntario][animal]) que almacena
 * el máximo número de eventos a los que se puede asistir considerando los
 * primeros 'evento' eventos con una capacidad de 'voluntario' voluntarios y
 * 'animal' animales.
 */
@Service
public class DynamicProgrammingService {

    @Autowired
    private ShelterRepository shelterRepository;
    @Autowired
    private EventRepository eventRepository;
    
    @Transactional(readOnly = true)
    public List<EventDTO> optimizeEventEnrollment(String shelterId) {
        // --- FASE 1: OBTENCIÓN Y VALIDACIÓN DE DATOS ---

        // 1. Obtener los recursos del refugio: animales y voluntarios disponibles.
        Shelter shelter = shelterRepository.findById(shelterId)
            .orElseThrow(() -> new RuntimeException("Refugio no encontrado con id: " + shelterId));
        int maxAnimals = shelterRepository.countAnimalsForShelter(shelterId);
        int maxVolunteers = shelterRepository.countVolunteersForShelter(shelterId);
        
        // 2. Obtener los "ítems" disponibles: eventos cercanos.
        List<EventDTO> nearbyEvents = eventRepository.findNearbyEventsForShelter(shelterId).stream()
            .map(e -> new EventDTO(e.getId(), e.getName(), e.getDate(), e.getAnimalEachShelter(), e.getVolunteersNeeded()))
            .collect(Collectors.toList());
        
        int n = nearbyEvents.size();
        if (n == 0) return new ArrayList<>();

        // --- FASE 2: CONSTRUCCIÓN DE LA TABLA DE DP ---

        // 3. Crear la tabla de DP. dp[i][j][k] representará la solución óptima
        // para los primeros `i` eventos, con `j` voluntarios y `k` animales.
        int[][][] dp = new int[n + 1][maxVolunteers + 1][maxAnimals + 1];

        // Rellenar la tabla de DP para determinar la máxima cantidad de eventos a los que se puede asistir.
        for (int i = 1; i <= n; i++) {
            EventDTO currentEvent = nearbyEvents.get(i - 1);
            int animalsNeeded = currentEvent.getAnimalEachShelter();
            int volunteersNeeded = currentEvent.getVolunteersNeeded();
            
            for (int v = 0; v <= maxVolunteers; v++) {
                for (int a = 0; a <= maxAnimals; a++) {
                    // Opción 1: No participar en el evento actual. El valor es el mismo
                    // que la solución para el evento anterior con los mismos recursos.
                    int valueWithout = dp[i - 1][v][a];
                    
                    // Opción 2: Participar en el evento si hay recursos suficientes.
                    int valueWith = 0;
                    if (v >= volunteersNeeded && a >= animalsNeeded) {
                        // El valor es 1 (por este evento) + la solución óptima para los eventos
                        // anteriores con los recursos restantes.
                        valueWith = 1 + dp[i - 1][v - volunteersNeeded][a - animalsNeeded];
                    }
                    
                    // Elegir la mejor opción.
                    dp[i][v][a] = Math.max(valueWithout, valueWith);
                }
            }
        }
        
        // --- FASE 3: RECONSTRUCCIÓN DE LA SOLUCIÓN ---
        // Ahora, recorrer la tabla de DP hacia atrás para descubrir qué eventos fueron seleccionados.
        List<EventDTO> chosenEvents = new ArrayList<>();
        int i = n, v = maxVolunteers, a = maxAnimals;
        while (i > 0 && v >= 0 && a >= 0) {
            EventDTO currentEvent = nearbyEvents.get(i - 1);
            int animalsNeeded = currentEvent.getAnimalEachShelter();
            int volunteersNeeded = currentEvent.getVolunteersNeeded();

            // Si el valor en dp[i][v][a] es diferente de dp[i-1][v][a],
            // significa que el evento `i` FUE incluido en la solución óptima.
            if (v >= volunteersNeeded && a >= animalsNeeded &&
                dp[i][v][a] == 1 + dp[i - 1][v - volunteersNeeded][a - animalsNeeded]) {
                
                chosenEvents.add(currentEvent);
                // Restamos los recursos utilizados para seguir reconstruyendo la decisión anterior.
                v -= volunteersNeeded;
                a -= animalsNeeded;
            }
            
            // Pasamos a evaluar la decisión para el evento anterior.
            i--;
        }
        
        // La reconstrucción se hace de atrás hacia adelante, así que invertimos la lista.
        Collections.reverse(chosenEvents);
        return chosenEvents;
    }
}