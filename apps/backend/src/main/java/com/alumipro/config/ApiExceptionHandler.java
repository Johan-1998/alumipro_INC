/* archivo java apiexceptionhandler */
package com.alumipro.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * FIX: los errores ahora retornan String plano (no JSON anidado) para que
 * ApiErrorUtils.readError() en la app móvil los muestre directamente al usuario.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /*
     * FIX: DataIntegrityViolationException manejada explícitamente.
     * Antes llegaba al handler genérico y mostraba al usuario:
     *   "Error interno: could not execute statement; SQL [n/a]; ..."
     * Ahora da un mensaje comprensible.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause().getMessage();
        System.err.println("[ALUMIPRO] DataIntegrityViolation: " + msg);

        if (msg != null && (msg.contains("Duplicate entry") || msg.contains("unique"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un registro con ese valor (correo u otro campo único).");
        }
        if (msg != null && msg.contains("foreign key constraint")) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar: el registro tiene ventas u otros datos relacionados.");
        }
        if (msg != null && msg.contains("Data too long")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Un campo supera el tamaño máximo permitido.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error de integridad en base de datos. Revise los datos ingresados.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        System.err.println("[ALUMIPRO] Error interno: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor. Intente de nuevo.");
    }
}
