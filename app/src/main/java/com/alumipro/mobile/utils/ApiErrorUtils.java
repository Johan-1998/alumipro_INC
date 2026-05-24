package com.alumipro.mobile.utils;

import java.io.IOException;

import retrofit2.Response;

public final class ApiErrorUtils {
    private ApiErrorUtils() {
    }

    public static String readError(Response<?> response) {
        if (response == null) {
            return "No se recibió respuesta del servidor.";
        }
        try {
            if (response.errorBody() != null) {
                String body = response.errorBody().string();
                if (body != null && !body.trim().isEmpty()) {
                    return body.trim();
                }
            }
        } catch (IOException ignored) {
        }
        if (response.code() == 401) {
            return "Sesión no válida. Inicia sesión nuevamente.";
        }
        if (response.code() == 500) {
            return "Error interno del servidor.";
        }
        return "Error HTTP " + response.code();
    }

    public static String fromThrowable(Throwable throwable) {
        return throwable == null || throwable.getMessage() == null || throwable.getMessage().trim().isEmpty()
                ? "No se pudo completar la operación."
                : throwable.getMessage();
    }
}
