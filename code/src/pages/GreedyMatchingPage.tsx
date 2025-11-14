import { useMutation } from "@tanstack/react-query";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/card";
import { Alert } from "../components/ui/alert";
import { Spinner } from "../components/ui/spinner";
import { getGreedyMatches } from "../services/algorithms";
import type { GreedyMatchResponse } from "../types";

export function GreedyMatchingPage() {
  const mutation = useMutation({
    mutationFn: getGreedyMatches,
  });

  const matches: GreedyMatchResponse | undefined = mutation.data;

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">
          Asignación Rápida de Mascotas (Greedy)
        </h2>
        <p className="text-muted-foreground">
          Ejecuta el algoritmo greedy para asignar mascotas disponibles a usuarios
          compatibles.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Acciones</CardTitle>
        </CardHeader>
        <CardContent className="flex items-center gap-3">
          <Button onClick={() => mutation.mutate()} disabled={mutation.isPending}>
            Asignar Mascotas
          </Button>
          {mutation.isPending ? <Spinner label="Calculando asignaciones..." /> : null}
        </CardContent>
      </Card>

      {mutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">
            No fue posible obtener la asignación greedy.
          </p>
          <p>
            {(mutation.error as { message?: string })?.message ??
              "Intenta nuevamente o revisa el estado del backend."}
          </p>
        </Alert>
      ) : null}

      <Card className="animate-fade-in">
        <CardHeader>
          <CardTitle>Resultado de la Asignación</CardTitle>
        </CardHeader>
        <CardContent>
          {matches && Object.keys(matches).length ? (
            <div className="overflow-x-auto">
              <table className="w-full table-auto border-collapse text-sm">
                <thead>
                  <tr className="text-left text-muted-foreground">
                    <th className="border-b border-border px-4 py-2">ID de Usuario</th>
                    <th className="border-b border-border px-4 py-2">
                      ID(s) de Mascota Asignada
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {Object.entries(matches).map(([userId, pets]) => (
                    <tr key={userId} className="hover:bg-muted/60">
                      <td className="border-b border-border px-4 py-2 font-medium">
                        {userId}
                      </td>
                      <td className="border-b border-border px-4 py-2">
                        {pets.length ? pets.join(", ") : "Sin asignación"}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p className="text-muted-foreground">
              Presiona &quot;Asignar Mascotas&quot; para obtener resultados.
            </p>
          )}
        </CardContent>
      </Card>
    </section>
  );
}

