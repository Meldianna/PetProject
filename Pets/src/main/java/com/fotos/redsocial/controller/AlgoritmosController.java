package com.fotos.redsocial.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fotos.redsocial.entity.dto.responses.EventDTO;
import com.fotos.redsocial.entity.dto.responses.LocationDTO;
import com.fotos.redsocial.entity.dto.responses.MSTResponse;
import com.fotos.redsocial.entity.dto.responses.PathResponse;
import com.fotos.redsocial.entity.dto.responses.ShelterDTO;
import com.fotos.redsocial.entity.dto.responses.SocialConnectionResponse;

import com.fotos.redsocial.service.BFSService;
import com.fotos.redsocial.service.DFSService;
import com.fotos.redsocial.service.DijkstraService;
import com.fotos.redsocial.service.PrimService;
import com.fotos.redsocial.service.GreedyMatchingService;
import com.fotos.redsocial.service.QuickSortService;
import com.fotos.redsocial.service.DynamicProgrammingService;
import com.fotos.redsocial.service.BacktrackingService;
import com.fotos.redsocial.service.BranchAndBoundService;

@RestController
@RequestMapping("/api/algoritmos")
public class AlgoritmosController {

    // Se inyecta una instancia de cada servicio de algoritmo.
    @Autowired private BFSService bfsService;
    @Autowired private DFSService dfsService;
    @Autowired private DijkstraService dijkstraService;
    @Autowired private PrimService primService;
    @Autowired private GreedyMatchingService greedyMatchingService;
    @Autowired private QuickSortService quickSortService;
    @Autowired private DynamicProgrammingService dynamicProgrammingService;
    @Autowired private BacktrackingService backtrackingService;
    @Autowired private BranchAndBoundService branchAndBoundService;

    @GetMapping("/bfs")
    public ResponseEntity<Optional<SocialConnectionResponse>> findConnection(@RequestParam String  userEmail, @RequestParam String animalId) {
        
        try {
            Optional<SocialConnectionResponse> connections = bfsService.findSocialConnection(userEmail, animalId);
            return ResponseEntity.ok(connections);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/dfs/{startId}/{endId}")
    public ResponseEntity<List<LocationDTO>> dfsPath(@PathVariable String startId, @PathVariable String endId) {
        // Llama directamente al servicio de DFS.
        List<LocationDTO> path = dfsService.findPath(startId, endId);
        return ResponseEntity.ok(path);
    }

    @GetMapping("/dijkstra/{startId}/{endId}")
    public ResponseEntity<PathResponse> findShortestPath(
            @PathVariable String startId,
            @PathVariable String endId) {
        // Llama directamente al servicio de Dijkstra.
        PathResponse path = dijkstraService.findShortestPath(startId, endId);
        return ResponseEntity.ok(path);
    }

    @GetMapping("/mst")
    public ResponseEntity<MSTResponse> getMinimumSpanningTree() {
        // Llama directamente al servicio de Prim.
        MSTResponse mst = primService.getMinimumSpanningTree();
        return ResponseEntity.ok(mst);
    }

    @GetMapping("/greedy/match-pets")
    public ResponseEntity<Map<String, List<String>>> matchPetsToUsers() {
        // Llama directamente al servicio de Greedy.
        Map<String, List<String>> matches = greedyMatchingService.matchPetsToUsersGreedy();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/sort/shelters")
    public ResponseEntity<List<ShelterDTO>> sortSheltersByCapacity() {
        // Llama directamente al servicio de QuickSort.
        List<ShelterDTO> sortedShelters = quickSortService.sortSheltersByCapacity();
        return ResponseEntity.ok(sortedShelters);
    }

    @GetMapping("/dynamic/optimize")
    public ResponseEntity<List<EventDTO>> optimizePetDistribution(@RequestParam String shelterId) {
        // Llama directamente al servicio de Programación Dinámica.
        List<EventDTO> distribution = dynamicProgrammingService.optimizeEventEnrollment(shelterId);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/backtrack/adoption-options/{userId}")
    public ResponseEntity<List<List<String>>> findAdoptionOptions(@PathVariable String userId) {
        // Llama directamente al servicio de Backtracking.
        List<List<String>> options = backtrackingService.findAdoptionOptions(userId);
        return ResponseEntity.ok(options);
    }

    @GetMapping("/branch-bound/route/{startId}/{userEmail}")
    public ResponseEntity<PathResponse> findOptimalShelterRoute(@PathVariable String startId, @PathVariable String userEmail) {
        // Llama directamente al servicio de Branch and Bound.
        PathResponse route = branchAndBoundService.findOptimalShelterRoute(startId, userEmail);
        return ResponseEntity.ok(route);
    }
}