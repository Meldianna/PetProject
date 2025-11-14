package com.fotos.redsocial.entity.dto.responses;

/**
 * Clase de datos (DTO interno) para mantener el estado 
 * de cada nodo dentro de la cola del algoritmo BFS.
 *
 * @param userEmail El email del usuario en este estado.
 * @param distance La distancia (número de saltos) desde el usuario inicial.
 */
public record BfsState(String userEmail, int distance) {
    
}
