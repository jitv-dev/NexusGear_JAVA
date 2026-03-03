package com.nexusgear.model;

import java.sql.Timestamp;

/**
 * MODEL — Clase de modelo para la entidad Usuario.
 * Patrón MVC: representa los datos del usuario en el sistema.
 */
public class Usuario {

    private int       id;
    private String    nombre;
    private String    apellido;
    private String    email;
    private String    password;
    private String    rol;        // ADMIN | CLIENTE
    private boolean   activo;
    private Timestamp createdAt;

    // ===== Constructores =====
    public Usuario() {}

    public Usuario(int id, String nombre, String apellido,
                   String email, String rol, boolean activo) {
        this.id       = id;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.email    = email;
        this.rol      = rol;
        this.activo   = activo;
    }

    // ===== Getters y Setters =====
    public int     getId()                    { return id; }
    public void    setId(int id)              { this.id = id; }

    public String  getNombre()                { return nombre; }
    public void    setNombre(String n)        { this.nombre = n; }

    public String  getApellido()              { return apellido; }
    public void    setApellido(String a)      { this.apellido = a; }

    public String  getNombreCompleto()        { return nombre + " " + apellido; }

    public String  getEmail()                 { return email; }
    public void    setEmail(String e)         { this.email = e; }

    public String  getPassword()              { return password; }
    public void    setPassword(String p)      { this.password = p; }

    public String  getRol()                   { return rol; }
    public void    setRol(String r)           { this.rol = r; }

    public boolean isActivo()                 { return activo; }
    public void    setActivo(boolean a)       { this.activo = a; }

    public Timestamp getCreatedAt()           { return createdAt; }
    public void setCreatedAt(Timestamp t)     { this.createdAt = t; }

    /** Verifica si el usuario tiene rol de administrador */
    public boolean isAdmin() { return "ADMIN".equals(rol); }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", email='" + email + "', rol='" + rol + "'}";
    }
}
