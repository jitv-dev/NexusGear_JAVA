package com.nexusgear.controller;

import com.nexusgear.dao.UsuarioDAO;
import com.nexusgear.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * CONTROLLER — Servlet para gestión de usuarios (solo ADMIN).
 * Lección 3 + 5: Patrón MVC, Servlets como controladores.
 *
 * URL mapeada: /usuarios
 */
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Solo ADMIN puede acceder
        if (!esAdmin(req)) {
            res.sendRedirect(req.getContextPath() + "/productos");
            return;
        }

        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "listar":
                List<Usuario> usuarios = usuarioDAO.listarTodos();
                req.setAttribute("usuarios", usuarios);
                req.getRequestDispatcher("/views/usuario/lista.jsp").forward(req, res);
                break;

            case "nuevo":
                req.getRequestDispatcher("/views/usuario/form.jsp").forward(req, res);
                break;

            case "eliminar":
                int id = Integer.parseInt(req.getParameter("id"));
                usuarioDAO.eliminar(id);
                req.getSession().setAttribute("mensaje", "Usuario desactivado.");
                res.sendRedirect(req.getContextPath() + "/usuarios");
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/usuarios");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        if (!esAdmin(req)) {
            res.sendRedirect(req.getContextPath() + "/productos");
            return;
        }

        String accion = req.getParameter("accion");
        if ("crear".equals(accion)) {
            if (usuarioDAO.existeEmail(req.getParameter("email"))) {
                req.getSession().setAttribute("mensaje", "❌ El email ya está registrado.");
            } else {
                Usuario u = new Usuario();
                u.setNombre(req.getParameter("nombre"));
                u.setApellido(req.getParameter("apellido"));
                u.setEmail(req.getParameter("email"));
                u.setPassword(req.getParameter("password"));
                u.setRol(req.getParameter("rol"));
                boolean ok = usuarioDAO.insertar(u);
                req.getSession().setAttribute("mensaje",
                        ok ? "✅ Usuario creado." : "❌ Error al crear usuario.");
            }
        }
        res.sendRedirect(req.getContextPath() + "/usuarios");
    }

    private boolean esAdmin(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return false;
        Usuario u = (Usuario) s.getAttribute("usuarioLogueado");
        return u != null && u.isAdmin();
    }
}
