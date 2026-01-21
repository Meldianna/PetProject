package com.fotos.redsocial.service;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fotos.redsocial.entity.Shelter;
import com.fotos.redsocial.entity.dto.responses.ShelterDTO;
import com.fotos.redsocial.repository.ShelterRepository;

/**
 * ALGORITMO: QuickSort (Ordenamiento Rápido)
 * - ordena de menor a mayor los shelters en base a la capacidad máxima de
 * animales que soporta.
 * 
 * CASO DE USO: "Un grupo de rescatistas acaba de salvar a cierta cantidad de
 * animales y
 * necesitan ser distribuidos urgentemente entre los refugios de la red de
 * nuestra aplicación, y
 * el coordinador necesita tomar decisiones rápidas y eficientes sobre dónde
 * alojar a estos animales.
 * Por lo tanto, ordena aquellos shelters que tienen mayor / menor capacidad
 * para optimizar la distribución."
 *
 *
 * FUNCIONAMIENTO:
 * QuickSort es un eficiente algoritmo de ordenamiento basado en la técnica de
 * "divide y vencerás". Funciona de la siguiente manera:
 * 1. PIVOTE: Elige un elemento del arreglo como pivote.
 * 2. PARTICIÓN: Reorganiza el arreglo de manera que todos los elementos menores
 * que el pivote queden a su izquierda y todos los mayores a su derecha.
 * 3. RECURSIÓN: Aplica el mismo proceso de forma recursiva (o iterativa) a los
 * sub-arreglos izquierdo y derecho del pivote.
 *
 * Esta implementación es iterativa (no recursiva) y utiliza una pila (Deque)
 * para gestionar los rangos de los sub-arreglos, evitando así un posible
 * `StackOverflowError` con listas muy grandes.
 */
@Service
public class QuickSortService {

    @Autowired
    private ShelterRepository shelterRepository;

    public List<ShelterDTO> sortSheltersByCapacity() {
        // Obtener todos los refugios de la base de datos.
        List<Shelter> shelters = shelterRepository.findAll();
        if (shelters == null || shelters.isEmpty()) {
            return Collections.emptyList();
        }
        // Llamar al método de ordenamiento para que actúe sobre la lista.
        quickSort(shelters, 0, shelters.size() - 1);

        // Convertir la lista ordenada de entidades a una lista de DTOs para la
        // respuesta.
        return shelters.stream()
                .map(s -> new ShelterDTO(s.getId(), s.getName(), s.getCapacity()))
                .collect(Collectors.toList());
    }

    private void quickSort(List<Shelter> shelters, int low, int high) {
        if (low >= high)
            return;

        // Utilizar una pila (Deque como stack) para la implementación iterativa.
        Deque<int[]> stack = new ArrayDeque<>();

        stack.push(new int[] { low, high });

        while (!stack.isEmpty()) {
            // 3. Sacar un rango [l, h] para procesar.
            int[] range = stack.pop();
            int l = range[0];
            int h = range[1];

            // "pi" es el índice final del pivote.
            int pi = partition(shelters, l, h);

            // Si hay elementos a la izquierda del pivote, meter ese rango en la pila.
            if (pi - 1 > l) {
                stack.push(new int[] { l, pi - 1 });
            }
            // Si hay elementos a la derecha del pivote, meter ese rango en la pila.
            if (pi + 1 < h) {
                stack.push(new int[] { pi + 1, h });
            }
        }
    }

    private int partition(List<Shelter> shelters, int low, int high) {
        // Elegir el último elemento como pivote.
        int pivotCapacity = shelters.get(high).getCapacity();

        // "i" es el índice del último elemento que era más pequeño que el pivote.
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            // Si el elemento actual es menor o igual al pivote...
            if (shelters.get(j).getCapacity() <= pivotCapacity) {
                // ...incrementar "i" y mover este elemento a la "sección de los menores".
                i++;
                Collections.swap(shelters, i, j);
            }
        }

        // Colocar el pivote en su posición final correcta, intercambiándolo con
        // el elemento en `i+1`.
        Collections.swap(shelters, i + 1, high);

        // Devolver el índice de partición (la posición final del pivote).
        return i + 1;
    }
}