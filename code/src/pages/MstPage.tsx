import { useMemo, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import ReactFlow, { Background, Controls, ReactFlowProvider } from "reactflow";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/card";
import { Alert } from "../components/ui/alert";
import { Spinner } from "../components/ui/spinner";
import { mstToFlowElements, normaliseLocations } from "../lib/graph";
import { getAllLocations, getMinimumSpanningTree } from "../services/algorithms";
import type { LocationDTO, MSTResponse } from "../types";

export function MstPage() {
  const [mst, setMst] = useState<MSTResponse | null>(null);
  const [locations, setLocations] = useState<LocationDTO[]>([]);

  const mutation = useMutation({
    mutationFn: async () => {
      const mstResult = await getMinimumSpanningTree();
      const locationResult = await Promise.allSettled([getAllLocations()]);

      const availableLocations =
        locationResult[0].status === "fulfilled" ? locationResult[0].value : [];

      setMst(mstResult);
      setLocations(normaliseLocations(availableLocations));
      return mstResult;
    },
  });

  const flowData = useMemo(() => {
    if (!mst) {
      return { nodes: [], edges: [] };
    }

    const baseLocations = locations.length
      ? locations
      : Array.from(
          new Set(
            mst.edges.flatMap((edge) => [edge.sourceId, edge.destinationId]),
          ),
        ).map((id) => ({
          id,
          name: id,
          address: "",
        }));

    return mstToFlowElements(baseLocations, mst.edges);
  }, [mst, locations]);

  const handleCalculate = () => {
    mutation.reset();
    setMst(null);
    mutation.mutate();
  };

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">
          Red de Conexión Mínima (MST - Prim)
        </h2>
        <p className="text-muted-foreground">
          Calcula el árbol de expansión mínima para conectar refugios con la menor
          distancia total.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Acción</CardTitle>
        </CardHeader>
        <CardContent className="flex items-center gap-3">
          <Button onClick={handleCalculate} disabled={mutation.isPending}>
            Calcular MST
          </Button>
          {mutation.isPending ? <Spinner label="Procesando MST..." /> : null}
        </CardContent>
      </Card>

      {mutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">
            No fue posible obtener el árbol de expansión mínima.
          </p>
          <p>
            {(mutation.error as { message?: string })?.message ??
              "Verifica que el backend esté disponible y vuelve a intentarlo."}
          </p>
        </Alert>
      ) : null}

      <div className="grid gap-6 lg:grid-cols-[2fr_1fr]">
        <Card className="min-h-[420px]">
          <CardHeader>
            <CardTitle>Visualización del árbol</CardTitle>
          </CardHeader>
          <CardContent className="h-[360px]">
            <ReactFlowProvider>
              <ReactFlow
                nodes={flowData.nodes}
                edges={flowData.edges}
                fitView
                minZoom={0.4}
                maxZoom={1.6}
                proOptions={{ hideAttribution: true }}
              >
                <Background />
                <Controls />
              </ReactFlow>
            </ReactFlowProvider>
          </CardContent>
        </Card>

        <Card className="animate-fade-in">
          <CardHeader>
            <CardTitle>Aristas del MST</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2 text-sm">
            {mst && mst.edges.length ? (
              mst.edges.map((edge) => (
                <div
                  key={`${edge.sourceId}-${edge.destinationId}`}
                  className="rounded-lg border border-primary/30 bg-primary/5 p-3"
                >
                  <p>
                    <span className="font-semibold text-primary">
                      {edge.sourceId}
                    </span>{" "}
                    →{" "}
                    <span className="font-semibold text-primary">
                      {edge.destinationId}
                    </span>
                  </p>
                  <p className="text-muted-foreground">
                    Distancia: {edge.distance.toFixed(2)} km
                  </p>
                </div>
              ))
            ) : (
              <p className="text-muted-foreground">
                Ejecuta el algoritmo para ver los resultados.
              </p>
            )}
          </CardContent>
        </Card>
      </div>
    </section>
  );
}

