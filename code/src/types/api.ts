export interface SocialConnectionResponse {
  friendEmail: string;
  connectionType: string;
}

export interface LocationDTO {
  id: string;
  name: string;
  address: string;
}

export interface PathResponse {
  locations: LocationDTO[];
  distance: number;
}

export interface MSTEdgeResponse {
  sourceId: string;
  destinationId: string;
  distance: number;
}

export interface MSTResponse {
  edges: MSTEdgeResponse[];
}

export interface ShelterDTO {
  id: string;
  name: string;
  capacity: number;
}

export interface EventDTO {
  id: string;
  name: string;
  date: string;
  animalEachShelter: number;
  volunteersNeeded: number;
}

export type GreedyMatchResponse = Record<string, string[]>;

export interface ApiErrorShape {
  message: string;
  status?: number;
  details?: unknown;
}

