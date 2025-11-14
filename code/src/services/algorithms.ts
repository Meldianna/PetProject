import { apiClient } from "./apiClient";
import type {
  EventDTO,
  GreedyMatchResponse,
  LocationDTO,
  MSTResponse,
  PathResponse,
  ShelterDTO,
  SocialConnectionResponse,
} from "../types";

export async function findSocialConnection(params: {
  userEmail: string;
  animalId: string;
}): Promise<SocialConnectionResponse | null> {
  const response = await apiClient.get<
    SocialConnectionResponse | null | { present: boolean; value?: SocialConnectionResponse }
  >("bfs", {
    params,
  });
  const payload = response.data;

  if (!payload) {
    return null;
  }

  if ("friendEmail" in payload) {
    return payload;
  }

  if ("value" in payload && payload.value) {
    return payload.value;
  }

  if ("present" in payload && !payload.present) {
    return null;
  }

  return null;
}

export async function findDfsPath(startId: string, endId: string) {
  const response = await apiClient.get<LocationDTO[]>(`dfs/${startId}/${endId}`);
  return response.data;
}

export async function findDijkstraPath(startId: string, endId: string) {
  const response = await apiClient.get<PathResponse>(
    `dijkstra/${startId}/${endId}`,
  );
  return response.data;
}

export async function getMinimumSpanningTree() {
  const response = await apiClient.get<MSTResponse>("mst");
  return response.data;
}

export async function getAllLocations() {
  const response = await apiClient.get<LocationDTO[]>("locations");
  return response.data;
}

export async function getGreedyMatches() {
  const response = await apiClient.get<GreedyMatchResponse>("greedy/match-pets");
  return response.data;
}

export async function getSortedShelters() {
  const response = await apiClient.get<ShelterDTO[]>("sort/shelters");
  return response.data;
}

export async function optimizeEvents(shelterId: string) {
  const response = await apiClient.get<EventDTO[]>("dynamic/optimize", {
    params: { shelterId },
  });
  return response.data;
}

export async function findAdoptionOptions(userId: string) {
  const response = await apiClient.get<string[][]>(
    `backtrack/adoption-options/${userId}`,
  );
  return response.data;
}

export async function findOptimalRoute(startId: string, userEmail: string) {
  const response = await apiClient.get<PathResponse>(
    `branch-bound/route/${startId}/${userEmail}`,
  );
  return response.data;
}

