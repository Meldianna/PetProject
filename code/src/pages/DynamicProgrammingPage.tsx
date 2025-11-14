import { type FormEvent, useMemo, useState } from "react";
import { useMutation } from "@tanstack/react-query";
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
import { optimizeEvents } from "../services/algorithms";
import type { EventDTO } from "../types";

const dateFormatter = new Intl.DateTimeFormat("es-ES", {
  year: "numeric",
  month: "short",
  day: "numeric",
});

export function DynamicProgrammingPage() {
  const [shelterId, setShelterId] = useState("");

  const mutation = useMutation({
    mutationFn: (id: string) => optimizeEvents(id),
  });

  const events: EventDTO[] | undefined = mutation.data;

  const subtitle = useMemo(() => {
    if (!events || !events.length) {
      return "Ejecuta la optimización para ver los eventos seleccionados.";
    }
    return `Se seleccionaron ${events.length} eventos óptimos con los recursos disponibles.`;
  }, [events]);

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    mutation.reset();
    mutation.mutate(shelterId.trim());
  };

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">
          Optimizar Participación en Eventos (Prog. Dinámica)
        </h2>
        <p className="text-muted-foreground">
          Determina qué eventos conviene atender según los recursos del refugio.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Parámetros</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="grid gap-4 md:grid-cols-2">
            <div className="grid gap-2">
              <Label htmlFor="shelterId">ID del refugio</Label>
              <Input
                id="shelterId"
                placeholder="SHELTER-001"
                value={shelterId}
                onChange={(event) => setShelterId(event.target.value)}
                required
              />
            </div>
            <div className="flex items-end">
              <Button type="submit" disabled={mutation.isPending}>
                Optimizar Eventos
              </Button>
            </div>
          </form>
          {mutation.isPending ? (
            <div className="mt-4">
              <Spinner label="Calculando combinación óptima..." />
            </div>
          ) : null}
        </CardContent>
      </Card>

      {mutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">
            Ocurrió un error al optimizar los eventos del refugio.
          </p>
          <p>
            {(mutation.error as { message?: string })?.message ??
              "Verifica el ID del refugio e inténtalo de nuevo."}
          </p>
        </Alert>
      ) : null}

      <section className="space-y-3">
        <h3 className="text-xl font-semibold">Eventos seleccionados</h3>
        <p className="text-sm text-muted-foreground">{subtitle}</p>
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {events && events.length ? (
            events.map((eventData) => (
              <Card
                key={eventData.id}
                className="border-l-4 border-l-primary/80 bg-primary/5"
              >
                <CardHeader className="pb-2">
                  <CardTitle className="text-base">{eventData.name}</CardTitle>
                  <CardDescription>
                    Fecha: {dateFormatter.format(new Date(eventData.date))}
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-2 text-sm">
                  <p>
                    <span className="font-semibold">ID:</span> {eventData.id}
                  </p>
                  <p>
                    <span className="font-semibold">Animales por refugio:</span>{" "}
                    {eventData.animalEachShelter}
                  </p>
                  <p>
                    <span className="font-semibold">Voluntarios necesarios:</span>{" "}
                    {eventData.volunteersNeeded}
                  </p>
                </CardContent>
              </Card>
            ))
          ) : (
            <Card>
              <CardContent className="py-6 text-sm text-muted-foreground">
                No hay eventos optimizados aún.
              </CardContent>
            </Card>
          )}
        </div>
      </section>
    </section>
  );
}

