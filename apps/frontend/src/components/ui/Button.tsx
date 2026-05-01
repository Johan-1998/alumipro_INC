/* archivo de componente button */
import { forwardRef } from "react";
import type { ButtonHTMLAttributes, ReactNode } from "react";
import styles from "./Button.module.css";

export type ButtonVariant = "primary" | "secondary" | "danger" | "ghost";
export type ButtonSize = "sm" | "md" | "lg";

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: ButtonVariant;
  size?: ButtonSize;
  isLoading?: boolean;
  leftIcon?: ReactNode;
  rightIcon?: ReactNode;
};

export const Button = forwardRef<HTMLButtonElement, Props>(function Button(
  { variant = "primary", size = "md", isLoading = false, leftIcon, rightIcon, className = "", disabled, children, ...rest },
  ref
) {
  const isDisabled = disabled || isLoading;

  return (
    <button
      ref={ref}
      className={`${styles.btn} ${styles[variant]} ${styles[size]} ${className}`}
      disabled={isDisabled}
      {...rest}
    >
      {isLoading ? <span className={styles.spinner} aria-hidden="true" /> : null}
      {!isLoading && leftIcon ? <span className={styles.icon} aria-hidden="true">{leftIcon}</span> : null}
      <span className={styles.label}>{children}</span>
      {!isLoading && rightIcon ? <span className={styles.icon} aria-hidden="true">{rightIcon}</span> : null}
    </button>
  );
});
