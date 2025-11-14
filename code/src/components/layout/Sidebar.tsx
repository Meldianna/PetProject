import { NavLink } from "react-router-dom";
import { cn } from "../../lib/utils";

export interface SidebarItem {
  path: string;
  label: string;
  description?: string;
}

interface SidebarProps {
  items: SidebarItem[];
}

export function Sidebar({ items }: SidebarProps) {
  return (
    <aside className="flex h-full w-[var(--sidebar-width)] flex-col border-r border-border bg-card/40">
      <div className="border-b border-border p-6">
        <h1 className="text-lg font-semibold text-primary">Algoritmos de Adopción</h1>
        <p className="mt-1 text-sm text-muted-foreground">
          Selecciona un algoritmo para interactuar con la API.
        </p>
      </div>
      <nav className="flex-1 space-y-1 overflow-y-auto p-4">
        {items.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              cn(
                "block rounded-lg px-4 py-3 text-sm transition-colors",
                isActive
                  ? "bg-primary text-primary-foreground shadow"
                  : "text-muted-foreground hover:bg-muted hover:text-foreground",
              )
            }
          >
            <div className="font-medium">{item.label}</div>
            {item.description ? (
              <p className="text-xs text-muted-foreground">
                {item.description}
              </p>
            ) : null}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}

