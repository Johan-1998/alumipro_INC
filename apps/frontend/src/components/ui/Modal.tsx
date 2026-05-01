/* archivo de componente modal */
import { useEffect, useMemo, useRef } from "react";
import type { ReactNode } from "react";
import { createPortal } from "react-dom";
import { X } from "lucide-react";
import styles from "./Modal.module.css";
import { Button } from "./Button";

type Props = {
  isOpen: boolean;
  title: string;
  children: ReactNode;
  footer?: ReactNode;
  onClose: () => void;
  ariaLabel?: string;
};

export function Modal({ isOpen, title, children, footer, onClose, ariaLabel }: Props) {
  const el = useMemo(() => document.createElement("div"), []);
  const onCloseRef = useRef(onClose);

  useEffect(() => {
    onCloseRef.current = onClose;
  }, [onClose]);

  useEffect(() => {
    if (!isOpen) return;

    document.body.appendChild(el);
    const prevOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";

    const onKeyDown = (e: KeyboardEvent) => {
      if (e.key === "Escape") onCloseRef.current();
    };

    window.addEventListener("keydown", onKeyDown);
    return () => {
      window.removeEventListener("keydown", onKeyDown);
      document.body.style.overflow = prevOverflow;
      el.remove();
    };
  }, [isOpen, el]);

  if (!isOpen) return null;

  return createPortal(
    <div className={styles.backdrop} role="dialog" aria-modal="true" aria-label={ariaLabel ?? title}>
      <button
        className={styles.backdropBtn}
        type="button"
        onClick={() => onCloseRef.current()}
        aria-label="Cerrar modal (fondo)"
      />
      <div className={styles.modal}>
        <div className={styles.header}>
          <div className={styles.title}>{title}</div>
          <Button
            variant="secondary"
            size="sm"
            type="button"
            onClick={() => onCloseRef.current()}
            aria-label="Cerrar modal"
            className={styles.close}
          >
            <X size={16} />
          </Button>
        </div>
        <div className={styles.body}>{children}</div>
        {footer ? <div className={styles.footer}>{footer}</div> : null}
      </div>
    </div>,
    el
  );
}