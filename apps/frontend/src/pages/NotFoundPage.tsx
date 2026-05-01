/* archivo de página notfoundpage */
import { useNavigate } from "react-router-dom";
import { AlertTriangle, ArrowLeft } from "lucide-react";
import styles from "./NotFoundPage.module.css";
import { EmptyState } from "../components/ui/EmptyState";
import { Button } from "../components/ui/Button";

export function NotFoundPage() {
  const navigate = useNavigate();
  return (
    <div className={styles.wrap}>
      <EmptyState
        title="Página no encontrada"
        description="La ruta solicitada no existe o fue movida."
        icon={<AlertTriangle size={24} />}
        action={
          <Button variant="primary" leftIcon={<ArrowLeft size={16} />} onClick={() => navigate("/")}>
            Ir al dashboard
          </Button>
        }
      />
    </div>
  );
}
