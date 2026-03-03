package com.nexusgear.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * UTIL — Utilidad para cifrado de contraseñas con SHA-256.
 */
public class PasswordUtil {

    /**
     * Genera el hash SHA-256 de una contraseña en texto plano.
     * @param password contraseña sin cifrar
     * @return hash hexadecimal en minúsculas (64 caracteres)
     */
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar hash SHA-256: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado.
     * @param passwordPlano contraseña ingresada por el usuario
     * @param hashAlmacenado hash guardado en la base de datos
     * @return true si coinciden
     */
    public static boolean verificar(String passwordPlano, String hashAlmacenado) {
        return hash(passwordPlano).equals(hashAlmacenado);
    }
}
