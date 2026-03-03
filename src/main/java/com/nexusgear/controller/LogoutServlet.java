package com.nexusgear.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * CONTROLLER — Servlet para cerrar sesión del usuario.
 * Invalida la HttpSession y redirige al login.
 *
 * URL mapeada: /logout
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            System.out.println("[LogoutServlet] Cerrando sesión: " +
                    session.getAttribute("usuarioLogueado"));
            session.invalidate(); // destruir la sesión
        }
        res.sendRedirect(req.getContextPath() + "/login");
    }
}
