import { type FormEvent, useMemo, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import ReactFlow, { Background, Controls, ReactFlowProvider } from "reactflow";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Alert } from "../components/ui/alert";
import { Spinner } from "../components/ui/spinner";
import { pathToFlowElements } from "../lib/graph";
import { findDfsPath, findDijkstraPath } from "../services/algorithms";
import type { LocationDTO, PathResponse } from "../types";

type ActivePath = "dfs" | "dijkstra" | null;

export function PathSearchPage() {
  const [startId, setStartId] = useState("");
  const [endId, setEndId] = useState("");
  const [dfsPath, setDfsPath] = useState<LocationDTO[] | null>(null);
  const [dijkstraPath, setDijkstraPath] = useState<PathResponse | null>(null);
  const [activePath, setActivePath] = useState<ActivePath>(null);

  const dfsMutation = useMutation({
    mutationFn: ({ start, end }: { start: string; end: string }) =>
      findDfsPath(start, end),
    onSuccess: (result) => {
      setDijkstraPath(null);
      setActivePath("dfs");
      setDfsPath(result.length ? result : null);
    },
  });

  const dijkstraMutation = useMutation({
    mutationFn: ({ start, end }: { start: string; end: string }) =>
      findDijkstraPath(start, end),
    onSuccess: (result) => {
      setDfsPath(null);
      setActivePath("dijkstra");
      setDijkstraPath(result.locations.length ? result : null);
    },
  });

  const isLoading = dfsMutation.isPending || dijkstraMutation.isPending;

  const sharedPath = useMemo(() => {
    if (activePath === "dfs" && dfsPath) {
      return pathToFlowElements(dfsPath);
    }
    if (activePath === "dijkstra" && dijkstraPath) {
      return pathToFlowElements(dijkstraPath.locations);
    }
    return { nodes: [], edges: [] };
  }, [activePath, dfsPath, dijkstraPath]);

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
  };

  const handleRunDfs = () => {
    setActivePath(null);
    setDfsPath(null);
    setDijkstraPath(null);
    dfsMutation.reset();
    dijkstraMutation.reset();
    dfsMutation.mutate({ start: startId.trim(), end: endId.trim() });
  };

  const handleRunDijkstra = () => {
    setActivePath(null);
    setDfsPath(null);
    setDijkstraPath(null);
    dijkstraMutation.reset();
    dfsMutation.reset();
    dijkstraMutation.mutate({ start: startId.trim(), end: endId.trim() });
  };

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">Calcular Ruta entre Ubicaciones</h2>
        <p className="text-muted-foreground">
          Ejecuta los algoritmos DFS o Dijkstra para descubrir rutas y distancias entre ubicaciones.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Parámetros de la ruta</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="grid gap-4 md:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="startId">ID de inicio</Label>
              <Input
                id="startId"
                placeholder="LOC-001"
                value={startId}
                onChange={(event) => setStartId(event.target.value)}
                required
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="endId">ID de destino</Label>
              <Input
                id="endId"
                placeholder="LOC-010"
                value={endId}
                onChange={(event) => setEndId(event.target.value)}
                required
              />
            </div>
            <div className="md:col-span-2 flex flex-wrap items-center gap-3 pt-2">
              <Button type="button" onClick={handleRunDfs} disabled={isLoading}>
                Buscar Camino (DFS)
              </Button>
              <Button type="button" variant="secondary" onClick={handleRunDijkstra} disabled={isLoading}>
                Buscar Camino Más Corto (Dijkstra)
              </Button>
              {isLoading ? <Spinner label="Calculando..." /> : null}
            </div>
          </form>
        </CardContent>
      </Card>

      {dfsMutation.isError || dijkstraMutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">No fue posible calcular la ruta.</p>
          <p>
            {(
              (dfsMutation.error ?? dijkstraMutation.error) as { message?: string }
            )?.message ?? "Revisa los IDs y vuelve a intentarlo."}
          </p>
        </Alert>
      ) : null}

      <div className="grid gap-6 lg:grid-cols-[2fr_1fr]">
        <Card className="min-h-[400px]">
          <CardHeader>
            <CardTitle>Visualización del grafo</CardTitle>
          </CardHeader>
          <CardContent className="h-[350px]">
            <ReactFlowProvider>
              <ReactFlow
                nodes={sharedPath.nodes}
                edges={sharedPath.edges}
                fitView
                minZoom={0.4}
                maxZoom={1.5}
                proOptions={{ hideAttribution: true }}
              >
                <Background />
                <Controls />
              </ReactFlow>
            </ReactFlowProvider>
          </CardContent>
        </Card>

        <div className="space-y-4">
          {activePath === "dfs" ? (
            <Card className="animate-fade-in">
              <CardHeader>
                <CardTitle>Resultado DFS</CardTitle>
              </CardHeader>
              <CardContent className="space-y-2">
                {dfsPath && dfsPath.length ? (
                  <>
                    <p className="text-sm text-muted-foreground">
                      Ruta encontrada de {startId} a {endId}:
                    </p>
                    <ol className="list-decimal space-y-1 pl-4 text-sm">
                      {dfsPath.map((location) => (
                        <li key={location.id}>
                          <span className="font-medium">{location.name ?? location.id}</span>
                          {location.address ? (
                            <span className="text-muted-foreground"> — {location.address}</span>
                          ) : null}
                        </li>
                      ))}
                    </ol>
                  </>
                ) : (
                  <p className="text-muted-foreground">
                    No se encontró un camino entre las ubicaciones indicadas.
                  </p>
                )}
              </CardContent>
            </Card>
          ) : null}

          {activePath === "dijkstra" ? (
            <Card className="animate-fade-in">
              <CardHeader>
                <CardTitle>Resultado Dijkstra</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                {dijkstraPath && dijkstraPath.locations.length ? (
                  <>
                    <div className="rounded-lg border border-primary/30 bg-primary/5 p-3 text-sm">
                      Distancia total:{" "}
                      <span className="font-semibold text-primary">
                        {dijkstraPath.distance.toFixed(2)} km
                      </span>
                    </div>
                    <ol className="list-decimal space-y-1 pl-4 text-sm">
                      {dijkstraPath.locations.map((location) => (
                        <li key={location.id}>
                          <span className="font-medium">{location.name ?? location.id}</span>
                          {location.address ? (
                            <span className="text-muted-foreground"> — {location.address}</span>
                          ) : null}
                        </li>
                      ))}
                    </ol>
                  </>
                ) : (
                  <p className="text-muted-foreground">
                    No se encontró una ruta óptima con los parámetros indicados.
                  </p>
                )}
              </CardContent>
            </Card>
          ) : null}
        </div>
      </div>
    </section>
  );
}

