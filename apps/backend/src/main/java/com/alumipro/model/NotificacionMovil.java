/*
 * DTO para el endpoint /api/movil/notificaciones.
 * Los campos deben coincidir exactamente con la clase NotificacionMovil
 * del proyecto móvil (app/src/main/java/com/alumipro/mobile/model/).
 */
package com.alumipro.model;

public class NotificacionMovil {

    private long id;
    private String titulo;
    private String mensaje;
    private String tipo;
    // campo "createdAt" para compatibilidad con el modelo del cliente móvil
    private String createdAt;

    public long getId()                   { return id; }
    public void setId(long id)            { this.id = id; }

    public String getTitulo()             { return titulo; }
    public void setTitulo(String titulo)  { this.titulo = titulo; }

    public String getMensaje()            { return mensaje; }
    public void setMensaje(String msg)    { this.mensaje = msg; }

    public String getTipo()               { return tipo; }
    public void setTipo(String tipo)      { this.tipo = tipo; }

    public String getCreatedAt()          { return createdAt; }
    public void setCreatedAt(String v)    { this.createdAt = v; }
}
