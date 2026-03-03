<%--
  Created by IntelliJ IDEA.
  User: sabin
  Date: 02-03-2026
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
  navbar.jsp — Barra de navegación reutilizable.

  CORRECCIONES aplicadas:
  1. '/productos/'  →  '/productos'   (sin barra final, causaba 404)
  2. '/ordenes/'    →  '/ordenes'     (sin barra final)
  3. '/usuarios/'   →  '/usuarios'    (sin barra final)
  4. Texto del brand corregido: "NEXUX GEAR" → "NEXUS GEAR"
  5. Agregado bloque de mensaje flash de sesión
--%>

<nav class="navbar">
    <div class="navbar-inner">

        <!-- Brand -->
        <a href="<c:url value='/productos'/>" class="navbar-brand">⚡ NEXUS GEAR</a>

        <!-- Links principales -->
        <div class="navbar-nav">


            <a class="nav-link" href="<c:url value='/productos'/>">Catálogo</a>

            <a class="nav-link" href="<c:url value='/ordenes?accion=carrito'/>">
                🛒 Carrito
                <c:if test="${not empty sessionScope.carritoCount and sessionScope.carritoCount > 0}">
          <span style="background:var(--accent);color:#000;border-radius:50%;
                       width:16px;height:16px;font-size:0.6rem;font-weight:900;
                       display:inline-flex;align-items:center;justify-content:center;
                       margin-left:0.3rem;">
            <c:out value="${sessionScope.carritoCount}"/>
          </span>
                </c:if>
            </a>


            <a class="nav-link" href="<c:url value='/ordenes'/>">Mis Órdenes</a>


            <c:if test="${sessionScope.esAdmin}">
                <a class="nav-link" href="<c:url value='/usuarios'/>">👥 Usuarios</a>
            </c:if>

        </div>

        <!-- Búsqueda rápida -->
        <form class="navbar-search" method="GET" action="<c:url value='/productos'/>">
            <input type="hidden" name="accion" value="buscar">
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none"
                 stroke="#6b8099" stroke-width="2">
                <circle cx="11" cy="11" r="8"/>
                <path d="m21 21-4.35-4.35"/>
            </svg>
            <input type="text" name="q" placeholder="Buscar productos...">
        </form>

        <!-- Info del usuario -->
        <div class="navbar-right">
            <c:if test="${not empty sessionScope.usuarioLogueado}">
        <span class="user-badge <c:if test='${sessionScope.esAdmin}'>badge-admin</c:if>">
          <c:if test="${sessionScope.esAdmin}">👑</c:if>
          <c:if test="${not sessionScope.esAdmin}">👤</c:if>
          <c:out value="${sessionScope.usuarioLogueado.nombre}"/>
        </span>
            </c:if>
            <a class="btn btn-outline btn-sm" href="<c:url value='/logout'/>">Salir</a>
        </div>

    </div>
</nav>


<c:if test="${not empty sessionScope.mensaje}">
    <div class="alert" style="margin:0;border-radius:0;border-left:none;
       border-bottom:1px solid var(--border);">
        <c:out value="${sessionScope.mensaje}"/>
    </div>
    <c:remove var="mensaje" scope="session"/>
</c:if>
