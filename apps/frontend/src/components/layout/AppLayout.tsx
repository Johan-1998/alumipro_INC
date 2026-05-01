/* archivo de componente applayout */
import { useEffect, useMemo, useState } from "react";
import { Outlet, useLocation, useMatches } from "react-router-dom";
import styles from "./AppLayout.module.css";
import { Sidebar } from "./Sidebar";
import { Topbar } from "./Topbar";
import type { BreadcrumbItem } from "./Topbar";

type RouteHandle = { title?: string; crumb?: string };

export function AppLayout() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const location = useLocation();
  const matches = useMatches();

  const { title, breadcrumbs } = useMemo(() => {
    const crumbs: BreadcrumbItem[] = [];
    let pageTitle = "ALUMIPRO";

    for (const m of matches) {
      const handle = (m.handle ?? {}) as RouteHandle;
      if (handle.crumb) {
        crumbs.push({ label: handle.crumb, href: m.pathname });
      }
      if (handle.title) pageTitle = handle.title;
    }

    return { title: pageTitle, breadcrumbs: crumbs };
  }, [matches]);

  useEffect(() => {
    setIsSidebarOpen(false);
  }, [location.pathname]);

  return (
    <div className={styles.shell}>
      <Sidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
      <div className={styles.main}>
        <Topbar
          title={title}
          breadcrumbs={breadcrumbs}
          onToggleSidebar={() => setIsSidebarOpen((v) => !v)}
        />
        <main className={styles.content} role="main">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
