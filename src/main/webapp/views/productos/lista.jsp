<%--
  Created by IntelliJ IDEA.
  User: sabin
  Date: 02-03-2026
  Time: 20:49
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Catálogo — NEXUS GEAR</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>

<%-- NAVBAR reutilizable --%>
<%@ include file="/views/navbar.jsp" %>

<div class="container">

    <!-- Encabezado de página -->
    <div class="page-header">
        <h1 class="page-title">
            Catálogo Gaming
            <span style="font-size:0.75rem;color:var(--muted);font-weight:400;margin-left:0.5rem">
        (<c:out value="${totalProductos}"/> productos)
      </span>
        </h1>

        <div style="display:flex;gap:0.8rem;align-items:center">
            <%-- Resultado de búsqueda --%>
            <c:if test="${not empty busqueda}">
        <span style="color:var(--muted);font-size:0.82rem">
          Resultados para: <strong style="color:var(--accent)"><c:out value="${busqueda}"/></strong>
        </span>
            </c:if>

            <%-- Botón Nuevo Producto solo para ADMIN --%>
            <c:if test="${sessionScope.esAdmin}">
                <a class="btn btn-primary btn-sm" href="<c:url value='/productos?accion=nuevo'/>">
                    + Nuevo Producto
                </a>
            </c:if>
        </div>
    </div>

    <%-- Mensaje de alerta si no hay productos --%>
    <c:if test="${empty productos}">
        <div class="alert alert-warn">
            No se encontraron productos.
            <c:if test="${not empty busqueda}">
                <a href="<c:url value='/productos'/>">Ver todos</a>
            </c:if>
        </div>
    </c:if>

    <%-- Grid de productos — JSTL c:forEach --%>
    <div class="product-grid">

        <c:forEach var="p" items="${productos}">
            <div class="product-card">

                    <%-- Badge (new, hot, sale) — JSTL c:if --%>
                <c:if test="${not empty p.badge}">
          <span class="product-badge badge-${p.badge}">
            <c:choose>
                <c:when test="${p.badge eq 'new'}">● NEW</c:when>
                <c:when test="${p.badge eq 'hot'}">🔥 HOT</c:when>
                <c:when test="${p.badge eq 'sale'}">% SALE</c:when>
            </c:choose>
          </span>
                </c:if>

                    <%-- Imagen (emoji) --%>
                <div class="product-img">
                    <c:out value="${p.imagen}"/>
                </div>

                <div class="product-body">
                    <div class="product-brand"><c:out value="${p.marcaNombre}"/></div>
                    <div class="product-name"><c:out value="${p.nombre}"/></div>
                    <div class="product-specs"><c:out value="${p.especificaciones}"/></div>

                    <div class="product-footer">
                        <div>
                                <%-- Precio anterior tachado si existe --%>
                            <c:if test="${not empty p.precioAnterior}">
                                <div class="price-old"><c:out value="${p.precioAnteriorFormateado}"/></div>
                            </c:if>
                            <div class="price-current"><c:out value="${p.precioFormateado}"/></div>
                        </div>

                            <%-- Stock disponible --%>
                        <c:choose>
                            <c:when test="${p.disponible}">
                                <a class="btn btn-primary btn-sm"
                                   href="<c:url value='/ordenes?accion=agregarCarrito&productoId=${p.id}&cantidad=1'/>">
                                    + Agregar
                                </a>
                            </c:when>
                            <c:otherwise>
                <span class="product-badge stock-out" style="position:static;font-size:0.65rem">
                  Sin Stock
                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                        <%-- Acciones admin --%>
                    <c:if test="${sessionScope.esAdmin}">
                        <div style="display:flex;gap:0.5rem;margin-top:0.7rem;padding-top:0.7rem;
                        border-top:1px solid var(--border)">
                            <a class="btn btn-outline btn-sm"
                               href="<c:url value='/productos?accion=editar&id=${p.id}'/>">
                                ✏️ Editar
                            </a>
                            <form method="POST" action="<c:url value='/productos'/>"
                                  onsubmit="return confirm('¿Eliminar este producto?')">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="${p.id}">
                                <button class="btn btn-danger btn-sm" type="submit">🗑️ Eliminar</button>
                            </form>
                        </div>
                    </c:if>

                </div>
            </div>
        </c:forEach>

    </div><%-- fin product-grid --%>
</div>

<footer>
    <span class="footer-brand">⚡ NEXUS GEAR</span> &nbsp;|&nbsp; Módulo 5 Java EE — MVC / JSP / Servlets / JDBC
</footer>

</body>
</html>
