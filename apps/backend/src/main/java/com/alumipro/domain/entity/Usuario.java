/* archivo de entidad usuario */
package com.alumipro.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 120, unique = true)
    private String correo;

    @Column(nullable = false, length = 255)
    private String password;

    /*
     * FIX: columnDefinition explícita para que Hibernate no intente alterar la
     * columna de ENUM('ADMIN','VENDEDOR') a VARCHAR(20) al arrancar con ddl-auto=update.
     * Sin esto, MySQL 8 con strict mode puede dejar el schema en estado inconsistente.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('ADMIN','VENDEDOR')")
    private RolUsuario rol;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (rol == null) rol = RolUsuario.VENDEDOR;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Integer getId()                          { return id; }
    public void setId(Integer id)                  { this.id = id; }

    public String getNombre()                       { return nombre; }
    public void setNombre(String nombre)            { this.nombre = nombre; }

    public String getCorreo()                       { return correo; }
    public void setCorreo(String correo)            { this.correo = correo; }

    public String getPassword()                     { return password; }
    public void setPassword(String password)        { this.password = password; }

    public RolUsuario getRol()                      { return rol; }
    public void setRol(RolUsuario rol)              { this.rol = rol; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void setCreatedAt(LocalDateTime v)       { this.createdAt = v; }

    public LocalDateTime getUpdatedAt()             { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v)       { this.updatedAt = v; }
}
