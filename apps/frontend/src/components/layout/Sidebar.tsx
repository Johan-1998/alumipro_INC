/* archivo de componente sidebar */
import { NavLink, useNavigate } from "react-router-dom";
import { Home, Users, Package, ShoppingCart, History, Shield, LogOut } from "lucide-react";
import styles from "./Sidebar.module.css";
import { Button } from "../ui/Button";
import { useAuth } from "../../app/auth";

type Props = {
  isOpen: boolean;
  onClose: () => void;
};

export function Sidebar({ isOpen, onClose }: Props) {
  const navigate = useNavigate();
  const { user, isAdmin, logout } = useAuth();

  const mainItems = [
    { to: "/", label: "Dashboard", icon: Home },
    { to: "/ventas/nueva", label: "Nueva venta", icon: ShoppingCart },
    { to: "/ventas/historico", label: "Histórico", icon: History },
  ];

  const dataItems = [
    { to: "/clientes", label: "Clientes", icon: Users },
    { to: "/productos", label: "Productos", icon: Package },
  ];

  if (isAdmin) {
    dataItems.push({ to: "/usuarios", label: "Usuarios", icon: Shield });
  }

  const renderItem = (item: { to: string; label: string; icon: React.ElementType }) => {
    const Icon = item.icon;
    return (
      <NavLink
        key={item.to}
        to={item.to}
        end={item.to === "/"}
        className={({ isActive }) => `${styles.link} ${isActive ? styles.active : ""}`}
      >
        <span className={styles.iconWrap} aria-hidden="true"><Icon size={16} /></span>
        <span className={styles.linkLabel}>{item.label}</span>
        <span className={styles.activePill} aria-hidden="true" />
      </NavLink>
    );
  };

  return (
    <>
      <aside className={`${styles.sidebar} ${isOpen ? styles.open : ""}`} aria-label="Barra lateral">
        <div className={styles.header}>
          <div className={styles.logoMark} aria-hidden="true">
            <svg className={styles.logoGlyph} viewBox="0 0 22 22" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="2" y="14" width="18" height="2.5" rx="1.25" fill="#E05C1A"/>
              <polygon points="11,3 2,14 5.5,14 11,6.5 16.5,14 20,14" fill="#E8ECF2"/>
              <rect x="8.5" y="9" width="5" height="5" rx="0.8" fill="#E05C1A" opacity="0.65"/>
            </svg>
          </div>
          <div className={styles.brand}>
            <div className={styles.brandName}>ALUMIPRO</div>
            <div className={styles.brandTag}>Gestión interna</div>
          </div>
        </div>

        <nav className={styles.nav} aria-label="Navegación principal">
          <div className={styles.sectionLabel}>Operaciones</div>
          {mainItems.map(renderItem)}
          <div className={styles.sectionLabel}>Inventario</div>
          {dataItems.map(renderItem)}
        </nav>

        <div className={styles.footer}>
          <div className={styles.userBox}>
            <div className={styles.userName}>{user?.nombre}</div>
            <div className={styles.userMeta}>
              <span className={styles.userRole}>{user?.rol}</span>
              <span className={styles.userSep} aria-hidden="true">·</span>
              <span className={styles.userMail}>{user?.correo}</span>
            </div>
          </div>

          <Button
            variant="ghost"
            size="sm"
            leftIcon={<LogOut size={14} />}
            onClick={() => { logout(); navigate("/auth/login"); }}
            aria-label="Cerrar sesión"
            className={styles.sidebarGhost}
            type="button"
          >
            Cerrar sesión
          </Button>

          <div className={styles.meta}>
            <span className={styles.metaLine}>Colombia · Industrial</span>
            <span className={styles.metaLine}>v1.1</span>
          </div>
        </div>

        <button className={styles.closeBtn} onClick={onClose} aria-label="Cerrar menú">×</button>
      </aside>

      <button
        className={`${styles.overlay} ${isOpen ? styles.overlayOpen : ""}`}
        onClick={onClose}
        aria-label="Cerrar menú"
      />
    </>
  );
}
