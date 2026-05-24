package com.alumipro.mobile.model;

public class LoginResponse {
    private String token;
    private UserView user;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserView getUser() { return user; }
    public void setUser(UserView user) { this.user = user; }

    public static class UserView {
        private int id;
        private String nombre;
        private String correo;
        private String rol;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
    }
}
