/* archivo de página errorpage */
import { isRouteErrorResponse, useNavigate, useRouteError } from "react-router-dom";
import { AlertOctagon, ArrowLeft } from "lucide-react";
import styles from "./ErrorPage.module.css";
import { EmptyState } from "../components/ui/EmptyState";
import { Button } from "../components/ui/Button";

export function ErrorPage() {
  const navigate = useNavigate();
  const err = useRouteError();

  const message = (() => {
    if (isRouteErrorResponse(err)) {
      return `${err.status} - ${err.statusText}`;
    }
    if (err instanceof Error) return err.message;
    return "Ha ocurrido un error inesperado.";
  })();

  return (
    <div className={styles.wrap}>
      <EmptyState
        title="Error en la aplicación"
        description={message}
        icon={<AlertOctagon size={24} />}
        action={
          <Button variant="primary" leftIcon={<ArrowLeft size={16} />} onClick={() => navigate("/")}>
            Volver al dashboard
          </Button>
        }
      />
    </div>
  );
}
