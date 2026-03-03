package com.nexusgear.controller;

import com.nexusgear.dao.OrdenDAO;
import com.nexusgear.dao.ProductoDAO;
import com.nexusgear.model.Orden;
import com.nexusgear.model.Orden.OrdenItem;
import com.nexusgear.model.Producto;
import com.nexusgear.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * CONTROLLER — Servlet para gestionar el carrito y las órdenes de compra.
 *
 * URL mapeada: /ordenes
 */
@WebServlet("/ordenes")
public class OrdenServlet extends HttpServlet {

    private OrdenDAO    ordenDAO;
    private ProductoDAO productoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        ordenDAO    = new OrdenDAO();
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

            case "detalle":
                detalle(req, res);
                break;

            case "carrito":
                req.getRequestDispatcher("/views/orden/carrito.jsp").forward(req, res);
                break;

            case "agregarCarrito":
                agregarAlCarrito(req, res);
                break;

            case "quitarCarrito":
                quitarDelCarrito(req, res);
                break;

            case "checkout":
                req.getRequestDispatcher("/views/orden/checkout.jsp").forward(req, res);
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

        if (!haySession(req)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String accion = req.getParameter("accion");

        switch (accion != null ? accion : "") {

            case "confirmar":
                confirmarOrden(req, res);
                break;

            case "cambiarEstado":
                if (esAdmin(req)) {
                    int id     = Integer.parseInt(req.getParameter("id"));
                    String est = req.getParameter("estado");
                    ordenDAO.actualizarEstado(id, est);
                    req.getSession().setAttribute("mensaje", "Estado actualizado.");
                }
                res.sendRedirect(req.getContextPath() + "/ordenes");
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/ordenes");
        }
    }

    // ================================================================
    // Métodos privados
    // ================================================================

    private void listar(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Usuario u = getUsuario(req);
        List<Orden> ordenes = u.isAdmin()
                ? ordenDAO.listarTodas()
                : ordenDAO.listarPorUsuario(u.getId());
        req.setAttribute("ordenes", ordenes);
        req.getRequestDispatcher("/views/orden/lista.jsp").forward(req, res);
    }

    private void detalle(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Orden orden = ordenDAO.buscarPorId(id);
        req.setAttribute("orden", orden);
        req.getRequestDispatcher("/views/orden/detalle.jsp").forward(req, res);
    }

    @SuppressWarnings("unchecked")
    private void agregarAlCarrito(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int productoId = Integer.parseInt(req.getParameter("productoId"));
        int cantidad   = Integer.parseInt(req.getParameter("cantidad") != null
                         ? req.getParameter("cantidad") : "1");

        Producto p = productoDAO.buscarPorId(productoId);
        if (p == null || !p.isDisponible()) {
            res.sendRedirect(req.getContextPath() + "/productos");
            return;
        }

        // El carrito se guarda en la sesión como Map<Integer, OrdenItem>
        HttpSession session = req.getSession();
        Map<Integer, OrdenItem> carrito =
                (Map<Integer, OrdenItem>) session.getAttribute("carrito");
        if (carrito == null) carrito = new LinkedHashMap<>();

        if (carrito.containsKey(productoId)) {
            carrito.get(productoId).setCantidad(
                    carrito.get(productoId).getCantidad() + cantidad);
        } else {
            OrdenItem item = new OrdenItem();
            item.setProductoId(productoId);
            item.setProductoNombre(p.getNombre());
            item.setProductoImagen(p.getImagen());
            item.setCantidad(cantidad);
            item.setPrecioUnit(p.getPrecio());
            carrito.put(productoId, item);
        }
        session.setAttribute("carrito", carrito);
        session.setAttribute("carritoCount", carrito.values().stream()
                .mapToInt(OrdenItem::getCantidad).sum());

        res.sendRedirect(req.getContextPath() + "/ordenes?accion=carrito");
    }

    @SuppressWarnings("unchecked")
    private void quitarDelCarrito(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int productoId = Integer.parseInt(req.getParameter("productoId"));
        HttpSession session = req.getSession(false);
        if (session != null) {
            Map<Integer, OrdenItem> carrito =
                    (Map<Integer, OrdenItem>) session.getAttribute("carrito");
            if (carrito != null) {
                carrito.remove(productoId);
                session.setAttribute("carrito", carrito);
                session.setAttribute("carritoCount", carrito.values().stream()
                        .mapToInt(OrdenItem::getCantidad).sum());
            }
        }
        res.sendRedirect(req.getContextPath() + "/ordenes?accion=carrito");
    }

    @SuppressWarnings("unchecked")
    private void confirmarOrden(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        Map<Integer, OrdenItem> carrito =
                (Map<Integer, OrdenItem>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/ordenes?accion=carrito");
            return;
        }

        Usuario u = getUsuario(req);

        // Calcular total
        BigDecimal total = carrito.values().stream()
                .map(OrdenItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Crear orden
        Orden orden = new Orden();
        orden.setNumeroOrden("NG-" + System.currentTimeMillis());
        orden.setUsuarioId(u.getId());
        orden.setTotal(total);
        orden.setDireccion(req.getParameter("direccion"));
        orden.setCiudad(req.getParameter("ciudad"));
        orden.setTelefono(req.getParameter("telefono"));
        orden.setItems(new ArrayList<>(carrito.values()));

        int idOrden = ordenDAO.insertar(orden);

        if (idOrden > 0) {
            // Descontar stock de cada producto
            ProductoDAO pdao = new ProductoDAO();
            for (OrdenItem item : carrito.values()) {
                pdao.descontarStock(item.getProductoId(), item.getCantidad());
            }
            // Limpiar carrito
            session.removeAttribute("carrito");
            session.removeAttribute("carritoCount");
            session.setAttribute("mensaje", "✅ ¡Orden confirmada! N° NG-" + idOrden);
            res.sendRedirect(req.getContextPath() + "/ordenes?accion=detalle&id=" + idOrden);
        } else {
            session.setAttribute("error", "❌ Error al procesar la orden.");
            res.sendRedirect(req.getContextPath() + "/ordenes?accion=checkout");
        }
    }

    // ================================================================
    // Helpers
    // ================================================================
    private boolean haySession(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s != null && s.getAttribute("usuarioLogueado") != null;
    }

    private boolean esAdmin(HttpServletRequest req) {
        Usuario u = getUsuario(req);
        return u != null && u.isAdmin();
    }

    private Usuario getUsuario(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) return null;
        return (Usuario) s.getAttribute("usuarioLogueado");
    }
}
