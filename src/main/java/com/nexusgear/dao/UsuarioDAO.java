package com.nexusgear.dao;

import com.nexusgear.model.Usuario;
import com.nexusgear.util.DBConexion;
import com.nexusgear.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO — Data Access Object para la entidad Usuario.
 * Lección 4: Implementa CRUD con JDBC y patrón Singleton de conexión.
 */
public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO() {
        this.conn = DBConexion.getInstancia().getConexion();
    }

    // ================================================================
    // AUTENTICACIÓN
    // ================================================================

    /**
     * Autentica un usuario por email y contraseña (SHA-256).
     * @return Usuario si es válido, null si no existe o password incorrecto.
     */
    public Usuario autenticar(String email, String password) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND activo = TRUE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashBD = rs.getString("password");
                if (PasswordUtil.verificar(password, hashBD)) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.autenticar] " + e.getMessage());
        }
        return null;
    }

    // ================================================================
    // CRUD
    // ================================================================

    /** Retorna todos los usuarios */
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY created_at DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.listarTodos] " + e.getMessage());
        }
        return lista;
    }

    /** Busca un usuario por su ID */
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.buscarPorId] " + e.getMessage());
        }
        return null;
    }

    /** Verifica si ya existe un email en la base de datos */
    public boolean existeEmail(String email) {
        String sql = "SELECT id FROM usuarios WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.existeEmail] " + e.getMessage());
        }
        return false;
    }

    /** Inserta un nuevo usuario (registro) */
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, email, password, rol) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getEmail());
            ps.setString(4, PasswordUtil.hash(u.getPassword()));
            ps.setString(5, u.getRol() != null ? u.getRol() : "CLIENTE");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.insertar] " + e.getMessage());
        }
        return false;
    }

    /** Actualiza datos de un usuario */
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, email=?, rol=?, activo=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getRol());
            ps.setBoolean(5, u.isActivo());
            ps.setInt(6, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.actualizar] " + e.getMessage());
        }
        return false;
    }

    /** Elimina (desactiva) un usuario por ID */
    public boolean eliminar(int id) {
        String sql = "UPDATE usuarios SET activo = FALSE WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.eliminar] " + e.getMessage());
        }
        return false;
    }

    // ================================================================
    // MAPPER — Convierte ResultSet a objeto Usuario
    // ================================================================
    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setEmail(rs.getString("email"));
        u.setRol(rs.getString("rol"));
        u.setActivo(rs.getBoolean("activo"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}
