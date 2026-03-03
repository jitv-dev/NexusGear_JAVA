package com.nexusgear.dao;

import com.nexusgear.model.Orden;
import com.nexusgear.model.Orden.OrdenItem;
import com.nexusgear.util.DBConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO — Data Access Object para Orden y OrdenItem.
 * Lección 4: Implementa CRUD con JDBC y patrón Singleton de conexión.
 */
public class OrdenDAO {

    private Connection conn;

    public OrdenDAO() {
        this.conn = DBConexion.getInstancia().getConexion();
    }

    // ================================================================
    // CONSULTAS (READ)
    // ================================================================

    /** Lista todas las órdenes (para ADMIN) */
    public List<Orden> listarTodas() {
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(u.nombre, ' ', u.apellido) AS usuario_nombre " +
                     "FROM ordenes o JOIN usuarios u ON o.usuario_id = u.id " +
                     "ORDER BY o.created_at DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearOrden(rs));
        } catch (SQLException e) {
            System.err.println("[OrdenDAO.listarTodas] " + e.getMessage());
        }
        return lista;
    }

    /** Lista las órdenes de un usuario específico */
    public List<Orden> listarPorUsuario(int usuarioId) {
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(u.nombre, ' ', u.apellido) AS usuario_nombre " +
                     "FROM ordenes o JOIN usuarios u ON o.usuario_id = u.id " +
                     "WHERE o.usuario_id = ? ORDER BY o.created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearOrden(rs));
        } catch (SQLException e) {
            System.err.println("[OrdenDAO.listarPorUsuario] " + e.getMessage());
        }
        return lista;
    }

    /** Busca una orden por su ID incluyendo sus items */
    public Orden buscarPorId(int id) {
        String sql = "SELECT o.*, CONCAT(u.nombre, ' ', u.apellido) AS usuario_nombre " +
                     "FROM ordenes o JOIN usuarios u ON o.usuario_id = u.id WHERE o.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Orden o = mapearOrden(rs);
                o.setItems(listarItems(id));
                return o;
            }
        } catch (SQLException e) {
            System.err.println("[OrdenDAO.buscarPorId] " + e.getMessage());
        }
        return null;
    }

    /** Lista los items de una orden */
    public List<OrdenItem> listarItems(int ordenId) {
        List<OrdenItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.nombre AS prod_nombre, p.imagen AS prod_imagen " +
                     "FROM orden_items oi JOIN productos p ON oi.producto_id = p.id " +
                     "WHERE oi.orden_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ordenId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrdenItem item = new OrdenItem();
                item.setId(rs.getInt("id"));
                item.setOrdenId(rs.getInt("orden_id"));
                item.setProductoId(rs.getInt("producto_id"));
                item.setProductoNombre(rs.getString("prod_nombre"));
                item.setProductoImagen(rs.getString("prod_imagen"));
                item.setCantidad(rs.getInt("cantidad"));
                item.setPrecioUnit(rs.getBigDecimal("precio_unit"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("[OrdenDAO.listarItems] " + e.getMessage());
        }
        return items;
    }

    // ================================================================
    // CREATE / UPDATE
    // ================================================================

    /**
     * Inserta una nueva orden junto con sus items en una transacción.
     * @return el ID de la orden creada, o -1 si falla
     */
    public int insertar(Orden orden) {
        String sqlOrden = "INSERT INTO ordenes (numero_orden, usuario_id, total, estado, " +
                          "direccion, ciudad, telefono) VALUES (?,?,?,?,?,?,?)";
        String sqlItem  = "INSERT INTO orden_items (orden_id, producto_id, cantidad, precio_unit) " +
                          "VALUES (?,?,?,?)";
        try {
            conn.setAutoCommit(false); // inicio de transacción

            // 1. Insertar la orden
            int ordenId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlOrden, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,     orden.getNumeroOrden());
                ps.setInt(2,        orden.getUsuarioId());
                ps.setBigDecimal(3, orden.getTotal());
                ps.setString(4,     "PENDIENTE");
                ps.setString(5,     orden.getDireccion());
                ps.setString(6,     orden.getCiudad());
                ps.setString(7,     orden.getTelefono());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) ordenId = keys.getInt(1);
            }

            // 2. Insertar items
            if (ordenId > 0 && orden.getItems() != null) {
                for (OrdenItem item : orden.getItems()) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                        ps.setInt(1,        ordenId);
                        ps.setInt(2,        item.getProductoId());
                        ps.setInt(3,        item.getCantidad());
                        ps.setBigDecimal(4, item.getPrecioUnit());
                        ps.executeUpdate();
                    }
                }
            }

            conn.commit(); // confirmar transacción
            return ordenId;

        } catch (SQLException e) {
            System.err.println("[OrdenDAO.insertar] Error en transacción: " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { /* ignorar */ }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { /* ignorar */ }
        }
        return -1;
    }

    /** Actualiza el estado de una orden (ADMIN) */
    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE ordenes SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[OrdenDAO.actualizarEstado] " + e.getMessage());
        }
        return false;
    }

    // ================================================================
    // MAPPER
    // ================================================================
    private Orden mapearOrden(ResultSet rs) throws SQLException {
        Orden o = new Orden();
        o.setId(rs.getInt("id"));
        o.setNumeroOrden(rs.getString("numero_orden"));
        o.setUsuarioId(rs.getInt("usuario_id"));
        o.setTotal(rs.getBigDecimal("total"));
        o.setEstado(rs.getString("estado"));
        o.setDireccion(rs.getString("direccion"));
        o.setCiudad(rs.getString("ciudad"));
        o.setTelefono(rs.getString("telefono"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        try { o.setUsuarioNombre(rs.getString("usuario_nombre")); } catch (SQLException ignored) {}
        return o;
    }
}
