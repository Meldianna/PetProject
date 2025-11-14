import { Navigate, Route, Routes } from "react-router-dom";
import { Layout } from "./components/layout/Layout";
import type { SidebarItem } from "./components/layout/Sidebar";
import { BfsPage } from "./pages/BfsPage";
import { PathSearchPage } from "./pages/PathSearchPage";
import { MstPage } from "./pages/MstPage";
import { GreedyMatchingPage } from "./pages/GreedyMatchingPage";
import { QuickSortSheltersPage } from "./pages/QuickSortSheltersPage";
import { DynamicProgrammingPage } from "./pages/DynamicProgrammingPage";
import { BacktrackingOptionsPage } from "./pages/BacktrackingOptionsPage";
import { BranchAndBoundPage } from "./pages/BranchAndBoundPage";
import "reactflow/dist/style.css";

const routes: SidebarItem[] = [
  {
    path: "/bfs",
    label: "Conexión Social (BFS)",
    description: "Encuentra amigos en común en 3 grados.",
  },
  {
    path: "/paths",
    label: "Rutas (DFS & Dijkstra)",
    description: "Explora rutas y la más corta entre ubicaciones.",
  },
  {
    path: "/mst",
    label: "MST - Prim",
    description: "Conecta refugios con el costo mínimo.",
  },
  {
    path: "/greedy",
    label: "Asignación Greedy",
    description: "Asigna mascotas rápidamente a usuarios.",
  },
  {
    path: "/quicksort",
    label: "Ordenar Refugios",
    description: "Clasifica refugios por capacidad.",
  },
  {
    path: "/dynamic",
    label: "Optimizar Eventos",
    description: "Selecciona eventos óptimos para un refugio.",
  },
  {
    path: "/backtracking",
    label: "Opciones de Adopción",
    description: "Genera combinaciones válidas.",
  },
  {
    path: "/branch-bound",
    label: "Ruta Branch & Bound",
    description: "Calcula una ruta cíclica óptima.",
  },
];

function App() {
  return (
    <Layout routes={routes}>
      <Routes>
        <Route index element={<Navigate to="/bfs" replace />} />
        <Route path="/bfs" element={<BfsPage />} />
        <Route path="/paths" element={<PathSearchPage />} />
        <Route path="/mst" element={<MstPage />} />
        <Route path="/greedy" element={<GreedyMatchingPage />} />
        <Route path="/quicksort" element={<QuickSortSheltersPage />} />
        <Route path="/dynamic" element={<DynamicProgrammingPage />} />
        <Route path="/backtracking" element={<BacktrackingOptionsPage />} />
        <Route path="/branch-bound" element={<BranchAndBoundPage />} />
        <Route path="*" element={<Navigate to="/bfs" replace />} />
      </Routes>
    </Layout>
  );
}

export default App;
