/* archivo de página errorinfopage */
import { AlertCircle } from "lucide-react";
import styles from "./ErrorInfoPage.module.css";

export function ErrorInfoPage() {
  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <div className={styles.icon} aria-hidden="true">
          <AlertCircle size={18} />
        </div>
        <div>
          <div className={styles.title}>Mensajes de error</div>
          <div className={styles.desc}>
            Esta pantalla existe para cumplir con la guía de actividades (prototipos de error) y para centralizar
            ejemplos de validaciones: campos obligatorios, formatos inválidos, stock insuficiente y errores HTTP del servidor.
          </div>

          <ul className={styles.list}>
            <li><strong>Validación:</strong> campos obligatorios y formatos (email, teléfono).</li>
            <li><strong>Regla de negocio:</strong> stock insuficiente al registrar una venta (mensaje del backend).</li>
            <li><strong>Errores HTTP:</strong> 404 recurso no encontrado, 500 error interno.</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
