import { type FormEvent, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { findSocialConnection } from "../services/algorithms";
import type { SocialConnectionResponse } from "../types";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/card";
import { Spinner } from "../components/ui/spinner";
import { Alert } from "../components/ui/alert";

export function BfsPage() {
  const [userEmail, setUserEmail] = useState("");
  const [animalId, setAnimalId] = useState("");

  const [result, setResult] = useState<SocialConnectionResponse | null | undefined>(undefined);

  const mutation = useMutation({
    mutationFn: findSocialConnection,
    onSuccess: (data) => {
      setResult(data ?? null);
    },
  });

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setResult(undefined);
    mutation.reset();
    mutation.mutate(
      {
        userEmail: userEmail.trim(),
        animalId: animalId.trim(),
      },
      {
        onError: () => {
          setResult(null);
        },
      },
    );
  };

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">Encontrar Conexión Social (BFS)</h2>
        <p className="text-muted-foreground">
          Busca conexiones sociales hasta 3 grados entre un usuario y la mascota indicada.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Parámetros de búsqueda</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid gap-2">
              <Label htmlFor="userEmail">Correo del usuario</Label>
              <Input
                id="userEmail"
                type="email"
                placeholder="juan@example.com"
                value={userEmail}
                onChange={(event) => setUserEmail(event.target.value)}
                required
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="animalId">ID de la mascota</Label>
              <Input
                id="animalId"
                placeholder="ANIMAL-123"
                value={animalId}
                onChange={(event) => setAnimalId(event.target.value)}
                required
              />
            </div>
            <div className="flex items-center gap-3">
              <Button type="submit" disabled={mutation.isPending}>
                Buscar Conexión
              </Button>
              {mutation.isPending ? <Spinner label="Buscando..." /> : null}
            </div>
          </form>
        </CardContent>
      </Card>

      {mutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">Hubo un problema al consultar el BFS.</p>
          <p>{(mutation.error as { message?: string })?.message ?? "Intenta nuevamente."}</p>
        </Alert>
      ) : null}

      {result !== undefined && !mutation.isPending ? (
        <Card className="animate-fade-in border-primary/40">
          <CardContent className="pt-6">
            {result ? (
              <div className="space-y-2">
                <p className="text-lg font-semibold text-primary">
                  ¡Conexión encontrada!
                </p>
                <p>
                  El usuario <span className="font-medium">{result.friendEmail}</span> tiene una
                  conexión de tipo{" "}
                  <span className="font-semibold text-primary">{result.connectionType}</span> con la
                  mascota solicitada.
                </p>
              </div>
            ) : (
              <p className="text-muted-foreground">
                No se encontró una conexión social en 3 grados de separación.
              </p>
            )}
          </CardContent>
        </Card>
      ) : null}
    </section>
  );
}

