package com.nexusgear.dao;

import com.nexusgear.model.Producto;
import com.nexusgear.util.DBConexion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO — Data Access Object para la entidad Producto.
 * Lección 4: Implementa CRUD con JDBC y patrón Singleton de conexión.
 */
public class ProductoDAO {

    private Connection conn;

    public ProductoDAO() {
        this.conn = DBConexion.getInstancia().getConexion();
    }

    // ================================================================
    // CONSULTAS (READ)
    // ================================================================

    /** Lista todos los productos activos con datos de categoría y marca */
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS cat_nombre, m.nombre AS marca_nombre " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.categoria_id = c.id " +
                     "LEFT JOIN marcas m ON p.marca_id = m.id " +
                     "WHERE p.activo = TRUE ORDER BY p.created_at DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.listarTodos] " + e.getMessage());
        }
        return lista;
    }

    /** Lista productos filtrados por categoría */
    public List<Producto> listarPorCategoria(int categoriaId) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS cat_nombre, m.nombre AS marca_nombre " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.categoria_id = c.id " +
                     "LEFT JOIN marcas m ON p.marca_id = m.id " +
                     "WHERE p.activo = TRUE AND p.categoria_id = ? " +
                     "ORDER BY p.nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.listarPorCategoria] " + e.getMessage());
        }
        return lista;
    }

    /** Busca productos por nombre o descripción (búsqueda libre) */
    public List<Producto> buscar(String query) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS cat_nombre, m.nombre AS marca_nombre " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.categoria_id = c.id " +
                     "LEFT JOIN marcas m ON p.marca_id = m.id " +
                     "WHERE p.activo = TRUE AND " +
                     "(LOWER(p.nombre) LIKE ? OR LOWER(m.nombre) LIKE ? OR LOWER(c.nombre) LIKE ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + query.toLowerCase() + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.buscar] " + e.getMessage());
        }
        return lista;
    }

    /** Lista los productos destacados (con badge) */
    public List<Producto> listarDestacados() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS cat_nombre, m.nombre AS marca_nombre " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.categoria_id = c.id " +
                     "LEFT JOIN marcas m ON p.marca_id = m.id " +
                     "WHERE p.activo = TRUE AND p.badge IS NOT NULL LIMIT 8";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.listarDestacados] " + e.getMessage());
        }
        return lista;
    }

    /** Busca un producto por su ID */
    public Producto buscarPorId(int id) {
        String sql = "SELECT p.*, c.nombre AS cat_nombre, m.nombre AS marca_nombre " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.categoria_id = c.id " +
                     "LEFT JOIN marcas m ON p.marca_id = m.id " +
                     "WHERE p.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.buscarPorId] " + e.getMessage());
        }
        return null;
    }

    // ================================================================
    // CRUD (CREATE, UPDATE, DELETE)
    // ================================================================

    /** Inserta un nuevo producto */
    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, especificaciones, " +
                     "precio, precio_anterior, stock, imagen, categoria_id, marca_id, badge) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,     p.getNombre());
            ps.setString(2,     p.getDescripcion());
            ps.setString(3,     p.getEspecificaciones());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setBigDecimal(5, p.getPrecioAnterior());
            ps.setInt(6,        p.getStock());
            ps.setString(7,     p.getImagen());
            ps.setInt(8,        p.getCategoriaId());
            ps.setInt(9,        p.getMarcaId());
            ps.setString(10,    p.getBadge());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.insertar] " + e.getMessage());
        }
        return false;
    }

    /** Actualiza un producto existente */
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, especificaciones=?, " +
                     "precio=?, precio_anterior=?, stock=?, imagen=?, " +
                     "categoria_id=?, marca_id=?, badge=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,     p.getNombre());
            ps.setString(2,     p.getDescripcion());
            ps.setString(3,     p.getEspecificaciones());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setBigDecimal(5, p.getPrecioAnterior());
            ps.setInt(6,        p.getStock());
            ps.setString(7,     p.getImagen());
            ps.setInt(8,        p.getCategoriaId());
            ps.setInt(9,        p.getMarcaId());
            ps.setString(10,    p.getBadge());
            ps.setInt(11,       p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.actualizar] " + e.getMessage());
        }
        return false;
    }

    /** Descuenta stock al registrar una venta */
    public boolean descontarStock(int productoId, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, productoId);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.descontarStock] " + e.getMessage());
        }
        return false;
    }

    /** Elimina (desactiva) un producto por ID */
    public boolean eliminar(int id) {
        String sql = "UPDATE productos SET activo = FALSE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO.eliminar] " + e.getMessage());
        }
        return false;
    }

    // ================================================================
    // MAPPER — Convierte ResultSet en objeto Producto
    // ================================================================
    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setEspecificaciones(rs.getString("especificaciones"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setPrecioAnterior(rs.getBigDecimal("precio_anterior"));
        p.setStock(rs.getInt("stock"));
        p.setImagen(rs.getString("imagen"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setMarcaId(rs.getInt("marca_id"));
        p.setBadge(rs.getString("badge"));
        p.setActivo(rs.getBoolean("activo"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        // Datos de joins
        try { p.setCategoriaNombre(rs.getString("cat_nombre")); } catch (SQLException ignored) {}
        try { p.setMarcaNombre(rs.getString("marca_nombre")); }    catch (SQLException ignored) {}
        return p;
    }
}
