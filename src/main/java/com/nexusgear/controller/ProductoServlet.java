package com.nexusgear.controller;

import com.nexusgear.dao.ProductoDAO;
import com.nexusgear.model.Producto;
import com.nexusgear.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * CONTROLLER — Servlet para gestionar el catálogo de productos.
 *
 * ✅ CORRECCIÓN: las rutas del RequestDispatcher apuntaban a
 *    /views/producto/  (singular — no existe)
 *    y deben apuntar a
 *    /views/productos/ (plural — carpeta real creada por IntelliJ)
 *
 * URL mapeada: /productos
 */
@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {

    private ProductoDAO productoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productoDAO = new ProductoDAO();
    }

    // ================================================================
    // GET
    // ================================================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!haySession(req)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String accion = req.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {

            case "listar":
                listar(req, res);
                break;

            case "buscar":
                buscar(req, res);
                break;

            case "detalle":
                detalle(req, res);
                break;

            case "nuevo":
                if (!esAdmin(req)) {
                    res.sendRedirect(req.getContextPath() + "/productos");
                    return;
                }
                // ✅ /views/productos/ (plural)
                req.getRequestDispatcher("/views/productos/form.jsp").forward(req, res);
                break;

            case "editar":
                if (!esAdmin(req)) {
                    res.sendRedirect(req.getContextPath() + "/productos");
                    return;
                }
                int idEditar = Integer.parseInt(req.getParameter("id"));
                Producto p = productoDAO.buscarPorId(idEditar);
                req.setAttribute("producto", p);
                // ✅ /views/productos/ (plural)
                req.getRequestDispatcher("/views/productos/form.jsp").forward(req, res);
                break;

            default:
                listar(req, res);
        }
    }

    // ================================================================
    // POST
    // ================================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        if (!haySession(req) || !esAdmin(req)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String accion = req.getParameter("accion");

        switch (accion != null ? accion : "") {

            case "crear":
                crearProducto(req, res);
                break;

            case "actualizar":
                actualizarProducto(req, res);
                break;

            case "eliminar":
                int idElim = Integer.parseInt(req.getParameter("id"));
                productoDAO.eliminar(idElim);
                req.getSession().setAttribute("mensaje", "✅ Producto eliminado correctamente.");
                res.sendRedirect(req.getContextPath() + "/productos");
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/productos");
        }
    }

    // ================================================================
    // Métodos privados
    // ================================================================

    private void listar(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<Producto> productos = productoDAO.listarTodos();
        req.setAttribute("productos", productos);
        req.setAttribute("totalProductos", productos.size());
        // ✅ /views/productos/ (plural)
        req.getRequestDispatcher("/views/productos/lista.jsp").forward(req, res);
    }

    private void buscar(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String query = req.getParameter("q");
        List<Producto> productos = (query != null && !query.isBlank())
                ? productoDAO.buscar(query)
                : productoDAO.listarTodos();
        req.setAttribute("productos", productos);
        req.setAttribute("busqueda", query);
        req.setAttribute("totalProductos", productos.size());
        // ✅ /views/productos/ (plural)
        req.getRequestDispatcher("/views/productos/lista.jsp").forward(req, res);
    }

    private void detalle(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Producto p = productoDAO.buscarPorId(id);
        if (p == null) {
            res.sendRedirect(req.getContextPath() + "/productos");
            return;
        }
        req.setAttribute("producto", p);
        // ✅ /views/productos/ (plural)
        req.getRequestDispatcher("/views/productos/detalle.jsp").forward(req, res);
    }

    private void crearProducto(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Producto p = buildProducto(req);
        boolean ok = productoDAO.insertar(p);
        req.getSession().setAttribute("mensaje",
                ok ? "✅ Producto creado correctamente." : "❌ Error al crear el producto.");
        res.sendRedirect(req.getContextPath() + "/productos");
    }

    private void actualizarProducto(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Producto p = buildProducto(req);
        p.setId(Integer.parseInt(req.getParameter("id")));
        boolean ok = productoDAO.actualizar(p);
        req.getSession().setAttribute("mensaje",
                ok ? "✅ Producto actualizado." : "❌ Error al actualizar.");
        res.sendRedirect(req.getContextPath() + "/productos");
    }

    private Producto buildProducto(HttpServletRequest req) {
        Producto p = new Producto();
        p.setNombre(req.getParameter("nombre"));
        p.setDescripcion(req.getParameter("descripcion"));
        p.setEspecificaciones(req.getParameter("especificaciones"));
        p.setPrecio(new BigDecimal(req.getParameter("precio")));
        String pa = req.getParameter("precioAnterior");
        if (pa != null && !pa.isBlank()) p.setPrecioAnterior(new BigDecimal(pa));
        p.setStock(Integer.parseInt(req.getParameter("stock")));
        p.setImagen(req.getParameter("imagen"));
        p.setCategoriaId(Integer.parseInt(req.getParameter("categoriaId")));
        p.setMarcaId(Integer.parseInt(req.getParameter("marcaId")));
        String badge = req.getParameter("badge");
        p.setBadge((badge != null && !badge.isBlank()) ? badge : null);
        return p;
    }

    // ================================================================
    // Helpers de sesión
    // ================================================================
    private boolean haySession(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("usuarioLogueado") != null;
    }

    private boolean esAdmin(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return false;
        Usuario u = (Usuario) s.getAttribute("usuarioLogueado");
        return u != null && u.isAdmin();
    }
}
