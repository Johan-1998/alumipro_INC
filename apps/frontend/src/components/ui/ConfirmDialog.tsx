/* archivo de componente confirmdialog */
import { Modal } from "./Modal";
import { Button } from "./Button";
import styles from "./ConfirmDialog.module.css";

type Props = {
  isOpen: boolean;
  title?: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  isDanger?: boolean;
  isLoading?: boolean;
  onConfirm: () => void;
  onClose: () => void;
};

export function ConfirmDialog({
  isOpen,
  title = "Confirmar acción",
  message,
  confirmText = "Confirmar",
  cancelText = "Cancelar",
  isDanger = false,
  isLoading = false,
  onConfirm,
  onClose
}: Props) {
  return (
    <Modal
      isOpen={isOpen}
      title={title}
      onClose={onClose}
      footer={
        <>
          <Button variant="secondary" onClick={onClose} disabled={isLoading}>
            {cancelText}
          </Button>
          <Button variant={isDanger ? "danger" : "primary"} onClick={onConfirm} isLoading={isLoading}>
            {confirmText}
          </Button>
        </>
      }
    >
      <div className={styles.message}>{message}</div>
    </Modal>
  );
}
