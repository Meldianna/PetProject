import { type FormEvent, useMemo, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import ReactFlow, { Background, Controls, ReactFlowProvider } from "reactflow";
import { Button } from "../components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Alert } from "../components/ui/alert";
import { Spinner } from "../components/ui/spinner";
import { pathToFlowElements } from "../lib/graph";
import { findOptimalRoute } from "../services/algorithms";
import type { PathResponse } from "../types";

export function BranchAndBoundPage() {
  const [startId, setStartId] = useState("");
  const [userEmail, setUserEmail] = useState("");

  const mutation = useMutation({
    mutationFn: ({ start, email }: { start: string; email: string }) =>
      findOptimalRoute(start, email),
  });

  const result: PathResponse | undefined = mutation.data;

  const flow = useMemo(() => {
    if (!result) {
      return { nodes: [], edges: [] };
    }
    return pathToFlowElements(result.locations, { cycle: true });
  }, [result]);

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    mutation.reset();
    mutation.mutate({ start: startId.trim(), email: userEmail.trim() });
  };

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">
          Ruta Óptima para el Usuario (Branch and Bound)
        </h2>
        <p className="text-muted-foreground">
          Calcula una ruta óptima y cíclica que conecta refugios relevantes para el
          usuario.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Parámetros</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="grid gap-4 md:grid-cols-3">
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
            <div className="grid gap-2 md:col-span-2">
              <Label htmlFor="userEmail">Correo del usuario</Label>
              <Input
                id="userEmail"
                type="email"
                placeholder="usuario@example.com"
                value={userEmail}
                onChange={(event) => setUserEmail(event.target.value)}
                required
              />
            </div>
            <div className="md:col-span-3 flex items-end gap-3">
              <Button type="submit" disabled={mutation.isPending}>
                Calcular Ruta Óptima
              </Button>
              {mutation.isPending ? (
                <Spinner label="Calculando mejor ruta..." />
              ) : null}
            </div>
          </form>
        </CardContent>
      </Card>

      {mutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">
            No fue posible calcular la ruta óptima para el usuario.
          </p>
          <p>
            {(mutation.error as { message?: string })?.message ??
              "Verifica los datos e inténtalo nuevamente."}
          </p>
        </Alert>
      ) : null}

      <div className="grid gap-6 lg:grid-cols-[2fr_1fr]">
        <Card className="min-h-[420px]">
          <CardHeader>
            <CardTitle>Visualización de la ruta</CardTitle>
            <CardDescription>
              La ruta es cíclica: el último nodo se conecta nuevamente con el inicial.
            </CardDescription>
          </CardHeader>
          <CardContent className="h-[360px]">
            <ReactFlowProvider>
              <ReactFlow
                nodes={flow.nodes}
                edges={flow.edges}
                fitView
                minZoom={0.5}
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
            <CardTitle>Detalle de la ruta</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3 text-sm">
            {result ? (
              <>
                <div className="rounded-lg border border-primary/30 bg-primary/5 p-3">
                  <p>
                    Coste total:{" "}
                    <span className="font-semibold text-primary">
                      {result.distance.toFixed(2)} km
                    </span>
                  </p>
                </div>
                <ol className="list-decimal space-y-1 pl-4">
                  {result.locations.map((location, index) => (
                    <li key={`${location.id}-${index}`}>
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
                Ingresa los parámetros y calcula la ruta para ver el resultado.
              </p>
            )}
          </CardContent>
        </Card>
      </div>
    </section>
  );
}

