import type { Edge, Node } from "reactflow";
import { MarkerType } from "reactflow";
import type { LocationDTO, MSTEdgeResponse } from "../types";

interface FlowBuilderOptions {
  highlightColor?: string;
  cycle?: boolean;
}

const DEFAULT_HIGHLIGHT = "#2563eb";

export function pathToFlowElements(
  path: LocationDTO[],
  options: FlowBuilderOptions = {},
): { nodes: Node[]; edges: Edge[] } {
  const highlightColor = options.highlightColor ?? DEFAULT_HIGHLIGHT;

  const nodes: Node[] = path.map((location, index) => ({
    id: location.id,
    data: {
      label: `${location.name ?? location.id}`,
      description: location.address,
    },
    position: { x: index * 220, y: 0 },
    className:
      "rounded-lg border-2 border-primary/60 bg-card px-4 py-2 shadow-md data-[highlighted=true]:bg-primary data-[highlighted=true]:text-primary-foreground",
    draggable: false,
    type: "default",
  }));

  const edges: Edge[] = [];

  for (let index = 0; index < path.length - 1; index += 1) {
    const current = path[index];
    const next = path[index + 1];
    edges.push({
      id: `${current.id}-${next.id}`,
      source: current.id,
      target: next.id,
      animated: true,
      style: {
        stroke: highlightColor,
        strokeWidth: 3,
      },
      markerEnd: {
        type: MarkerType.ArrowClosed,
      },
    });
  }

  if (options.cycle && path.length > 1) {
    const first = path[0];
    const last = path[path.length - 1];
    edges.push({
      id: `${last.id}-${first.id}-cycle`,
      source: last.id,
      target: first.id,
      animated: true,
      style: {
        stroke: highlightColor,
        strokeWidth: 3,
      },
      markerEnd: {
        type: MarkerType.ArrowClosed,
      },
    });
  }

  return { nodes, edges };
}

export function mstToFlowElements(
  locations: LocationDTO[],
  edges: MSTEdgeResponse[],
  options: { highlightColor?: string } = {},
): { nodes: Node[]; edges: Edge[] } {
  const highlightColor = options.highlightColor ?? DEFAULT_HIGHLIGHT;
  const locationMap = new Map<string, LocationDTO>();
  locations.forEach((location) => locationMap.set(location.id, location));

  const uniqueIds = new Set<string>();
  edges.forEach((edge) => {
    uniqueIds.add(edge.sourceId);
    uniqueIds.add(edge.destinationId);
  });

  const ids = Array.from(uniqueIds);
  const radius = 250;

  const nodes: Node[] = ids.map((id, index) => {
    const location = locationMap.get(id);
    const angle = (index / ids.length) * 2 * Math.PI;
    return {
      id,
      data: {
        label: location?.name ?? id,
        description: location?.address,
      },
      position: {
        x: radius * Math.cos(angle),
        y: radius * Math.sin(angle),
      },
      className:
        "rounded-lg border-2 border-primary/60 bg-card px-4 py-2 shadow-md",
      draggable: false,
    };
  });

  const flowEdges: Edge[] = edges.map((edge) => ({
    id: `${edge.sourceId}-${edge.destinationId}`,
    source: edge.sourceId,
    target: edge.destinationId,
    animated: true,
    style: {
      stroke: highlightColor,
      strokeWidth: 2,
    },
    markerEnd: { type: MarkerType.ArrowClosed },
    label: `${edge.distance.toFixed(2)} km`,
    labelStyle: { fill: highlightColor, fontWeight: 500 },
  }));

  return { nodes, edges: flowEdges };
}

export function normaliseLocations(locations: LocationDTO[]) {
  const seen = new Map<string, LocationDTO>();
  locations.forEach((location) => {
    if (!seen.has(location.id)) {
      seen.set(location.id, location);
    }
  });
  return Array.from(seen.values());
}

