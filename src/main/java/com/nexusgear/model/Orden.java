package com.nexusgear.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * MODEL — Clase de modelo para la entidad Orden (pedido de compra).
 * Patrón MVC: encapsula los datos de una orden del cliente.
 */
public class Orden {

    private int        id;
    private String     numeroOrden;
    private int        usuarioId;
    private String     usuarioNombre;    // para vistas
    private BigDecimal total;
    private String     estado;           // PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    private String     direccion;
    private String     ciudad;
    private String     telefono;
    private Timestamp  createdAt;
    private List<OrdenItem> items;       // detalle de productos

    // ===== Constructores =====
    public Orden() {}

    // ===== Getters y Setters =====
    public int        getId()                     { return id; }
    public void       setId(int id)               { this.id = id; }

    public String     getNumeroOrden()            { return numeroOrden; }
    public void       setNumeroOrden(String n)    { this.numeroOrden = n; }

    public int        getUsuarioId()              { return usuarioId; }
    public void       setUsuarioId(int uid)       { this.usuarioId = uid; }

    public String     getUsuarioNombre()          { return usuarioNombre; }
    public void       setUsuarioNombre(String n)  { this.usuarioNombre = n; }

    public BigDecimal getTotal()                  { return total; }
    public void       setTotal(BigDecimal t)      { this.total = t; }

    public String     getEstado()                 { return estado; }
    public void       setEstado(String e)         { this.estado = e; }

    public String     getDireccion()              { return direccion; }
    public void       setDireccion(String d)      { this.direccion = d; }

    public String     getCiudad()                 { return ciudad; }
    public void       setCiudad(String c)         { this.ciudad = c; }

    public String     getTelefono()               { return telefono; }
    public void       setTelefono(String t)       { this.telefono = t; }

    public Timestamp  getCreatedAt()              { return createdAt; }
    public void       setCreatedAt(Timestamp t)   { this.createdAt = t; }

    public List<OrdenItem> getItems()             { return items; }
    public void setItems(List<OrdenItem> items)   { this.items = items; }

    public String getTotalFormateado() {
        if (total == null) return "$0";
        return "$" + String.format("%,.0f", total).replace(",", ".");
    }

    @Override
    public String toString() {
        return "Orden{id=" + id + ", numero='" + numeroOrden + "', estado='" + estado + "'}";
    }

    // =========================================================
    // Clase interna: OrdenItem (detalle de productos por orden)
    // =========================================================
    public static class OrdenItem {
        private int        id;
        private int        ordenId;
        private int        productoId;
        private String     productoNombre;  // para vistas
        private String     productoImagen;
        private int        cantidad;
        private BigDecimal precioUnit;

        public OrdenItem() {}

        public int        getId()                       { return id; }
        public void       setId(int id)                 { this.id = id; }

        public int        getOrdenId()                  { return ordenId; }
        public void       setOrdenId(int o)             { this.ordenId = o; }

        public int        getProductoId()               { return productoId; }
        public void       setProductoId(int p)          { this.productoId = p; }

        public String     getProductoNombre()           { return productoNombre; }
        public void       setProductoNombre(String n)   { this.productoNombre = n; }

        public String     getProductoImagen()           { return productoImagen; }
        public void       setProductoImagen(String i)   { this.productoImagen = i; }

        public int        getCantidad()                 { return cantidad; }
        public void       setCantidad(int c)            { this.cantidad = c; }

        public BigDecimal getPrecioUnit()               { return precioUnit; }
        public void       setPrecioUnit(BigDecimal p)   { this.precioUnit = p; }

        public BigDecimal getSubtotal() {
            if (precioUnit == null) return BigDecimal.ZERO;
            return precioUnit.multiply(BigDecimal.valueOf(cantidad));
        }

        public String getSubtotalFormateado() {
            return "$" + String.format("%,.0f", getSubtotal()).replace(",", ".");
        }
    }
}
