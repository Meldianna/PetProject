import { useMutation } from "@tanstack/react-query";
import { Button } from "../components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "../components/ui/card";
import { Alert } from "../components/ui/alert";
import { Spinner } from "../components/ui/spinner";
import { getSortedShelters } from "../services/algorithms";
import type { ShelterDTO } from "../types";

export function QuickSortSheltersPage() {
  const mutation = useMutation({
    mutationFn: getSortedShelters,
  });

  const shelters: ShelterDTO[] | undefined = mutation.data;

  return (
    <section className="space-y-6">
      <header className="space-y-2">
        <h2 className="text-2xl font-semibold">
          Clasificar Refugios por Capacidad (QuickSort)
        </h2>
        <p className="text-muted-foreground">
          Obtén un listado de refugios ordenado por su capacidad disponible mediante
          QuickSort iterativo.
        </p>
      </header>

      <Card>
        <CardHeader>
          <CardTitle>Acción</CardTitle>
        </CardHeader>
        <CardContent className="flex items-center gap-3">
          <Button onClick={() => mutation.mutate()} disabled={mutation.isPending}>
            Ordenar Refugios
          </Button>
          {mutation.isPending ? <Spinner label="Ordenando..." /> : null}
        </CardContent>
      </Card>

      {mutation.isError ? (
        <Alert variant="destructive">
          <p className="font-semibold">No fue posible obtener los refugios ordenados.</p>
          <p>
            {(mutation.error as { message?: string })?.message ??
              "Intenta nuevamente o verifica el backend."}
          </p>
        </Alert>
      ) : null}

      <Card className="animate-fade-in">
        <CardHeader>
          <CardTitle>Listado Ordenado</CardTitle>
        </CardHeader>
        <CardContent>
          {shelters && shelters.length ? (
            <div className="overflow-x-auto">
              <table className="w-full table-auto border-collapse text-sm">
                <thead>
                  <tr className="text-left text-muted-foreground">
                    <th className="border-b border-border px-4 py-2">ID</th>
                    <th className="border-b border-border px-4 py-2">Nombre</th>
                    <th className="border-b border-border px-4 py-2 text-right">Capacidad</th>
                  </tr>
                </thead>
                <tbody>
                  {shelters.map((shelter) => (
                    <tr key={shelter.id} className="hover:bg-muted/60">
                      <td className="border-b border-border px-4 py-2 font-medium">
                        {shelter.id}
                      </td>
                      <td className="border-b border-border px-4 py-2">{shelter.name}</td>
                      <td className="border-b border-border px-4 py-2 text-right font-semibold">
                        {shelter.capacity}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p className="text-muted-foreground">
              Haz clic en &quot;Ordenar Refugios&quot; para visualizar resultados.
            </p>
          )}
        </CardContent>
      </Card>
    </section>
  );
}

