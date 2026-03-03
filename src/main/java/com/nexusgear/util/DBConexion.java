package com.nexusgear.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * UTIL — Clase de conexión a MySQL con patrón Singleton.
 * Garantiza una única instancia de conexión durante la ejecución.
 *
 * Lección 4: Capa de acceso a datos — JDBC + Singleton
 */
public class DBConexion {

    // ===== Parámetros de conexión — ajustar antes de desplegar =====
    private static final String URL      =
            "jdbc:mysql://localhost:3306/nexusgear_db" +
            "?useSSL=false&serverTimezone=America/Santiago" +
            "&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "1313";

    /** Instancia única del Singleton */
    private static DBConexion instancia;
    private Connection conexion;

    /**
     * Constructor privado: carga el driver y establece la conexión JDBC.
     */
    private DBConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("[DBConexion] ✅ Conexión establecida con MySQL.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ Driver MySQL no encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error al conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Método estático sincronizado para obtener la instancia única.
     * Si la conexión está cerrada, la recrea automáticamente.
     *
     * @return instancia única de DBConexion
     */
    public static synchronized DBConexion getInstancia() {
        try {
            if (instancia == null || instancia.conexion == null
                    || instancia.conexion.isClosed()) {
                instancia = new DBConexion();
            }
        } catch (SQLException e) {
            instancia = new DBConexion();
        }
        return instancia;
    }

    /**
     * Retorna el objeto Connection para ejecutar consultas JDBC.
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra la conexión activa con la base de datos.
     */
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[DBConexion] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[DBConexion] Error al cerrar: " + e.getMessage());
        }
    }
}
