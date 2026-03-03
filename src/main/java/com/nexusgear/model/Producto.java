package com.nexusgear.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * MODEL — Clase de modelo para la entidad Producto.
 * Patrón MVC: encapsula los datos de cada producto gamer.
 */
public class Producto {

    private int        id;
    private String     nombre;
    private String     descripcion;
    private String     especificaciones;
    private BigDecimal precio;
    private BigDecimal precioAnterior;
    private int        stock;
    private String     imagen;            // emoji representativo
    private int        categoriaId;
    private String     categoriaNombre;
    private int        marcaId;
    private String     marcaNombre;
    private String     badge;             // new, hot, sale
    private boolean    activo;
    private Timestamp  createdAt;

    // ===== Constructores =====
    public Producto() {}

    public Producto(int id, String nombre, BigDecimal precio, int stock, String imagen) {
        this.id     = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock  = stock;
        this.imagen = imagen;
    }

    // ===== Getters y Setters =====
    public int        getId()                      { return id; }
    public void       setId(int id)                { this.id = id; }

    public String     getNombre()                  { return nombre; }
    public void       setNombre(String n)          { this.nombre = n; }

    public String     getDescripcion()             { return descripcion; }
    public void       setDescripcion(String d)     { this.descripcion = d; }

    public String     getEspecificaciones()        { return especificaciones; }
    public void       setEspecificaciones(String e){ this.especificaciones = e; }

    public BigDecimal getPrecio()                  { return precio; }
    public void       setPrecio(BigDecimal p)      { this.precio = p; }

    public BigDecimal getPrecioAnterior()          { return precioAnterior; }
    public void       setPrecioAnterior(BigDecimal p){ this.precioAnterior = p; }

    public int        getStock()                   { return stock; }
    public void       setStock(int s)              { this.stock = s; }

    public String     getImagen()                  { return imagen; }
    public void       setImagen(String i)          { this.imagen = i; }

    public int        getCategoriaId()             { return categoriaId; }
    public void       setCategoriaId(int id)       { this.categoriaId = id; }

    public String     getCategoriaNombre()         { return categoriaNombre; }
    public void       setCategoriaNombre(String c) { this.categoriaNombre = c; }

    public int        getMarcaId()                 { return marcaId; }
    public void       setMarcaId(int id)           { this.marcaId = id; }

    public String     getMarcaNombre()             { return marcaNombre; }
    public void       setMarcaNombre(String m)     { this.marcaNombre = m; }

    public String     getBadge()                   { return badge; }
    public void       setBadge(String b)           { this.badge = b; }

    public boolean    isActivo()                   { return activo; }
    public void       setActivo(boolean a)         { this.activo = a; }

    public Timestamp  getCreatedAt()               { return createdAt; }
    public void       setCreatedAt(Timestamp t)    { this.createdAt = t; }

    /** Retorna true si hay stock disponible */
    public boolean isDisponible() { return stock > 0; }

    /** Precio formateado en pesos chilenos */
    public String getPrecioFormateado() {
        if (precio == null) return "$0";
        return "$" + String.format("%,.0f", precio).replace(",", ".");
    }

    public String getPrecioAnteriorFormateado() {
        if (precioAnterior == null) return "";
        return "$" + String.format("%,.0f", precioAnterior).replace(",", ".");
    }

    @Override
    public String toString() {
        return "Producto{id=" + id + ", nombre='" + nombre + "', precio=" + precio + "}";
    }
}
