/* archivo de página dashboard */
import { Building2, Package, Users, BadgeDollarSign, Award, CheckCircle2, Zap, Settings, Layers, Wrench } from "lucide-react";
import styles from "./DashboardPage.module.css";
import { StatCard } from "../components/ui/StatCard";
import { useClientes } from "../hooks/useClientes";
import { useProductos } from "../hooks/useProductos";
import { useVentas } from "../hooks/useVentas";
import { formatCOP } from "../utils/format";
import { ProjectThumb } from "../components/dashboard/ProjectThumb";

type ProjectKind = "Fachadas" | "Ventanería" | "Estructuras" | "Residencial" | "Industrial";

type Project = {
  title: string;
  city: string;
  category: string;
  kind: ProjectKind;
};

const projects: Project[] = [
  { title: "Fachada Centro Comercial El Tesoro", city: "Medellín", category: "Fachadas", kind: "Fachadas" },
  { title: "Ventanería Torre Empresarial", city: "Bogotá", category: "Ventanería", kind: "Ventanería" },
  { title: "Estructura Aluminio Aeropuerto", city: "Cali", category: "Estructuras", kind: "Estructuras" },
  { title: "Ventanas Residencial La Colina", city: "Bucaramanga", category: "Residencial", kind: "Residencial" },
  { title: "Fachada Flotante Plaza Norte", city: "Barranquilla", category: "Fachadas flotantes", kind: "Fachadas" },
  { title: "Cerramientos Zona Franca Industrial", city: "Cartagena", category: "Industrial", kind: "Industrial" },
];

const services = [
  { icon: <Layers size={22} />, title: "Perfiles estructurales", desc: "Perfiles para marcos, refuerzos y soluciones de carga industrial." },
  { icon: <Zap size={22} />, title: "Ventanería arquitectónica", desc: "Sistemas corredizos, proyectantes y batientes para obra civil." },
  { icon: <Building2 size={22} />, title: "Fachadas flotantes", desc: "Envolventes con diseño contemporáneo y precisión de ensamble." },
  { icon: <Settings size={22} />, title: "Corte y maquinado CNC", desc: "Tolerancias controladas para piezas y series de producción." },
  { icon: <Wrench size={22} />, title: "Instalación en sitio", desc: "Montaje con enfoque en seguridad, detalle y acabado." },
];

const certs = [
  { label: "ISO 9001", sub: "Calidad certificada" },
  { label: "ICONTEC", sub: "Normalización técnica" },
  { label: "NTC 2279", sub: "Perfil de aluminio" },
];

export function DashboardPage() {
  const clientes = useClientes();
  const productos = useProductos();
  const ventas = useVentas();

  const totalProductos = productos.data.length;
  const totalClientes = clientes.data.length;

  return (
    <div className={styles.page}>
      {/* ── HÉROE ── */}
      <div className={styles.hero}>
        <div className={styles.heroContent}>
          <div className={styles.heroBadge}>
            <span className={styles.heroBadgeDot} />
            <span className={styles.heroBadgeText}>Sistema operativo</span>
          </div>
          <h1 className={styles.heroTitle}>Panel de gestión · ALUMIPRO</h1>
          <p className={styles.heroSub}>Control de inventario, clientes y ventas en tiempo real</p>
        </div>
      </div>

      {/* ── ESTADÍSTICAS ── */}
      <section className={styles.statsGrid} aria-label="Resumen">
        <StatCard label="Productos" value={String(totalProductos)} icon={<Package size={18} />} tone="primary" helper="Inventario activo" />
        <StatCard label="Clientes" value={String(totalClientes)} icon={<Users size={18} />} tone="neutral" helper="Clientes registrados" />
        <StatCard label="Ventas" value={String(ventas.stats.count)} icon={<BadgeDollarSign size={18} />} tone="accent" helper="Total registradas" />
        <StatCard label="Ingresos" value={formatCOP(ventas.stats.total)} icon={<BadgeDollarSign size={18} />} tone="accent" helper="Suma ventas" />
      </section>

      {/* ── QUIÉNES SOMOS ── */}
      <section className={styles.section} aria-label="Quiénes somos">
        <div className={styles.sectionHead}>
          <h2 className={styles.h2}>Quiénes somos</h2>
          <span className={styles.sub}>Solidez · Precisión · Cumplimiento</span>
        </div>
        <div className={styles.aboutCard}>
          <div className={styles.aboutIcon} aria-hidden="true">
            <Building2 size={22} />
          </div>
          <div className={styles.aboutText}>
            <p className={styles.p}>
              <strong>ALUMIPRO</strong> es una empresa colombiana especializada en la fabricación y comercialización
              de perfiles de aluminio, ventanería arquitectónica, fachadas y estructuras metálicas para el sector de
              la construcción. Con más de 12 años de experiencia, combinamos precisión en procesos, control de
              calidad y entregas alineadas con los estándares del cliente y la obra.
            </p>
            <p className={styles.p} style={{ color: "var(--c-text-muted)" }}>
              Atendemos proyectos residenciales, comerciales e industriales en todo el territorio colombiano,
              con personal técnico especializado y materiales de primera calidad con trazabilidad completa.
            </p>
          </div>
        </div>
      </section>

      {/* ── PROYECTOS ── */}
      <section className={styles.section} aria-label="Proyectos destacados">
        <div className={styles.sectionHead}>
          <h2 className={styles.h2}>Proyectos destacados</h2>
          <span className={styles.sub}>Obras ejecutadas por ALUMIPRO</span>
        </div>
        <div className={styles.projectsGrid}>
          {projects.map((p) => (
            <article key={p.title} className={styles.projectCard}>
              <div className={styles.projectImg}>
                <ProjectThumb kind={p.kind} />
              </div>
              <div className={styles.projectBody}>
                <div className={styles.projectTitle}>{p.title}</div>
                <div className={styles.projectMeta}>
                  <span className={styles.projectCity}>{p.city}</span>
                  <span className={styles.dot} aria-hidden="true">·</span>
                  <span className={styles.projectCat}>{p.category}</span>
                </div>
              </div>
            </article>
          ))}
        </div>
      </section>

      {/* ── SERVICIOS ── */}
      <section className={styles.section} aria-label="Servicios">
        <div className={styles.sectionHead}>
          <h2 className={styles.h2}>Servicios</h2>
          <span className={styles.sub}>Capacidades operativas de ALUMIPRO</span>
        </div>
        <div className={styles.servicesGrid}>
          {services.map((s) => (
            <div key={s.title} className={styles.service}>
              <div className={styles.serviceIcon} aria-hidden="true">{s.icon}</div>
              <div className={styles.serviceTitle}>{s.title}</div>
              <div className={styles.serviceDesc}>{s.desc}</div>
            </div>
          ))}
        </div>
      </section>

      {/* ── CERTIFICACIONES ── */}
      <section className={styles.section} aria-label="Certificaciones">
        <div className={styles.sectionHead}>
          <h2 className={styles.h2}>Certificaciones</h2>
          <span className={styles.sub}>Estándares técnicos y de calidad</span>
        </div>
        <div className={styles.certsStrip}>
          {certs.map((c) => (
            <div key={c.label} className={styles.certBadge}>
              <div className={styles.certIcon}><Award size={16} /></div>
              <div>
                <div className={styles.certText}>{c.label}</div>
                <div className={styles.certSub}>{c.sub}</div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
