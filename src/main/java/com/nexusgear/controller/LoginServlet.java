package com.nexusgear.controller;

import com.nexusgear.dao.UsuarioDAO;
import com.nexusgear.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * CONTROLLER — Servlet para gestionar el Login y Logout.
 *
 * Lección 3: Conociendo los Servlets
 * - Procesa peticiones GET (mostrar formulario) y POST (validar credenciales)
 * - Usa HttpSession para mantener la sesión del usuario autenticado
 *
 * URL mapeada: /login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * GET /login — Muestra el formulario de login (index.jsp).
     * Si ya hay sesión activa, redirige al catálogo.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            res.sendRedirect(req.getContextPath() + "/productos");
            return;
        }
        req.getRequestDispatcher("/index.jsp").forward(req, res);
    }

    /**
     * POST /login — Procesa el formulario de login.
     * Si las credenciales son válidas, guarda el usuario en sesión.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        // Validar campos vacíos
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Por favor completa todos los campos.");
            req.getRequestDispatcher("/index.jsp").forward(req, res);
            return;
        }

        // Autenticar contra la BD
        Usuario usuario = usuarioDAO.autenticar(email.trim(), password.trim());

        if (usuario != null) {
            // ✅ Credenciales válidas: crear sesión
            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("esAdmin", usuario.isAdmin());
            session.setMaxInactiveInterval(60 * 30); // 30 minutos

            System.out.println("[LoginServlet] Usuario autenticado: " + usuario.getEmail());
            res.sendRedirect(req.getContextPath() + "/productos");
        } else {
            // ❌ Credenciales inválidas
            req.setAttribute("error", "Email o contraseña incorrectos.");
            req.setAttribute("emailIngresado", email);
            req.getRequestDispatcher("/index.jsp").forward(req, res);
        }
    }
}
