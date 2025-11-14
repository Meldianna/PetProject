import type { ReactNode } from "react";
import { Sidebar, type SidebarItem } from "./Sidebar";

interface LayoutProps {
  routes: SidebarItem[];
  children: ReactNode;
}

export function Layout({ routes, children }: LayoutProps) {
  return (
    <div className="flex min-h-screen bg-background text-foreground">
      <Sidebar items={routes} />
      <main className="flex-1 overflow-y-auto bg-background">
        <div className="mx-auto w-full max-w-6xl p-6 md:p-10">{children}</div>
      </main>
    </div>
  );
}

