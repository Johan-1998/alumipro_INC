package com.alumipro.mobile.model;

public class UsuarioSesion {
    private int id;
    private String nombre;
    private String correo;
    private String rol;
    private String token;

    public UsuarioSesion() {
    }

    public UsuarioSesion(int id, String nombre, String correo, String rol, String token) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.token = token;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(rol);
    }
}
