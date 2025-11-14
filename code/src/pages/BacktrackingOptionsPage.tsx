import { type FormEvent, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { Button } from "../components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Alert } from "../components/ui/alert";
import { Spinner } from "../components/ui/spinner";
import { findAdoptionOptions } from "../services/algorithms";

export function BacktrackingOptionsPage() {
  const [userId, setUserId] = useState("");

  const mutation = useMutation({
    mutationFn: (id: string) => findAdoptionOptions(id),
  });

  const options = mutation.data;

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    mutation.reset();
    mutation.mutate(userId.trim());
  };

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">
          Encontrar Combinaciones de Adopción (Backtracking)
        </h2>
        <p className="text-muted-foreground">
          Genera combinaciones de mascotas compatibles para un usuario específico.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Parámetros</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="grid gap-4 md:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="userId">ID del usuario</Label>
              <Input
                id="userId"
                placeholder="USER-001"
                value={userId}
                onChange={(event) => setUserId(event.target.value)}
                required
              />
            </div>
            <div className="flex items-end">
              <Button type="submit" disabled={mutation.isPending}>
                Buscar Opciones
              </Button>
            </div>
          </form>
          {mutation.isPending ? (
            <div className="mt-4">
              <Spinner label="Buscando combinaciones compatibles..." />
            </div>
          ) : null}
        </CardContent>
      </Card>

      {mutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">No fue posible obtener las combinaciones.</p>
          <p>
            {(mutation.error as { message?: string })?.message ??
              "Revisa el ID del usuario o la conexión con el backend."}
          </p>
        </Alert>
      ) : null}

      <Card className="animate-fade-in">
        <CardHeader>
          <CardTitle>Opciones de adopción</CardTitle>
        </CardHeader>
        <CardContent className="space-y-3">
          {options && options.length ? (
            options.map((combo, index) => (
              <div
                key={`${combo.join("-")}-${index}`}
                className="rounded-lg border border-primary/30 bg-primary/5 p-3 text-sm"
              >
                <p className="font-semibold text-primary">
                  Opción {index + 1}:{" "}
                  <span className="font-normal text-foreground">
                    [{combo.join(", ")}]
                  </span>
                </p>
              </div>
            ))
          ) : (
            <p className="text-sm text-muted-foreground">
              Aún no hay combinaciones calculadas.
            </p>
          )}
        </CardContent>
      </Card>
    </section>
  );
}

