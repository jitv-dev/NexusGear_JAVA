<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--Vista parcial: barra de navegacion reutilizable en todas las paginas--%>
<%--Se incluye con <%@ include file="/views/navbar.jsp"%>--%>

<nav class="navbar">
    <div class="navbar-inner">
        <%--BRAND--%>
        <a href="<c:url value='/productos/'/>" class="navbar-brand">NEXUS GEAR</a>

        <%--Links principales--%>
        <div class="navbar-nav">
            <a href="<c:url value='/productos/'/>" class="nav-link">Catálogo</a>
            <a href="<c:url value='/ordenes?accion=carrito'/>" class="nav-link">Carrito
                <c:if test="${not empty sessionScope.carritoCount and sessionScope.carritoCount > 0}">
                    <span style="background: var(--accent);
                                        color: #000;
                                        border-radius: 50%;
                                        width: 16px;
                                        height: 16px;
                                        font-size: 0.6rem;
                                        font-weight: 900;
                                        display: inline-flex;
                                        align-items: center;
                                        justify-content: center;
                                        margin-left: 0.3rem;">
                        <c:out value="${sessionScope.carritoCount}"/>
                    </span>
                </c:if>
            </a>
            <a href="<c:url value='/ordenes/'/>" class="nav-link">Mis Órdenes</a>

            <%--Solo visible para ADMIN--%>
            <c:if test="${sessionScope.esAdmin}">
                <a href="<c:url value='/usuarios/'/>" class="nav-link">Usuarios</a>
            </c:if>

        </div>
    </div>
</nav>