<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Carrito — NEXUS GEAR</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<%@ include file="/views/navbar.jsp" %>

<div class="container" style="max-width:750px">

  <div class="page-header">
    <h1 class="page-title">🛒 Mi Carrito</h1>
    <a class="btn btn-outline btn-sm" href="<c:url value='/productos'/>">← Seguir Comprando</a>
  </div>

  <%-- Carrito vacío --%>
  <c:if test="${empty sessionScope.carrito}">
    <div class="card" style="text-align:center;padding:3rem">
      <div style="font-size:4rem;margin-bottom:1rem">🎮</div>
      <p style="color:var(--muted)">Tu carrito está vacío</p>
      <a class="btn btn-primary" href="<c:url value='/productos'/>" style="margin-top:1rem">
        Ver Catálogo
      </a>
    </div>
  </c:if>

  <%-- Items del carrito --%>
  <c:if test="${not empty sessionScope.carrito}">
    <div class="card">

      <%-- c:forEach para iterar los items del carrito (Map de sesión) --%>
      <c:set var="totalGeneral" value="0"/>
      <c:forEach var="entry" items="${sessionScope.carrito}">
        <c:set var="item" value="${entry.value}"/>

        <div class="carrito-item">
          <div class="carrito-item-img"><c:out value="${item.productoImagen}"/></div>

          <div class="carrito-item-info">
            <div class="carrito-item-name"><c:out value="${item.productoNombre}"/></div>
            <div class="carrito-item-price">
              Precio unit: $<fmt:formatNumber value="${item.precioUnit}" pattern="#,###" groupingSeparator="."/>
            </div>
            <div style="color:var(--muted);font-size:0.78rem">
              Cantidad: <c:out value="${item.cantidad}"/>
            </div>
          </div>

          <div style="text-align:right">
            <div class="carrito-item-price" style="font-size:1rem">
              $<fmt:formatNumber value="${item.subtotal}" pattern="#,###" groupingSeparator="."/>
            </div>
            <a class="btn btn-danger btn-sm" style="margin-top:0.4rem"
               href="<c:url value='/ordenes?accion=quitarCarrito&productoId=${item.productoId}'/>">
              🗑️ Quitar
            </a>
          </div>
        </div>

        <%-- Acumulador del total con JSTL --%>
        <c:set var="totalGeneral" value="${totalGeneral + item.subtotal}"/>
      </c:forEach>

      <%-- Total --%>
      <div class="carrito-total">
        <span class="carrito-total-label">Total a pagar</span>
        <span class="carrito-total-val">
          $<fmt:formatNumber value="${totalGeneral}" pattern="#,###" groupingSeparator="."/>
        </span>
      </div>

      <a class="btn btn-primary btn-full"
         href="<c:url value='/ordenes?accion=checkout'/>">
        Proceder al Checkout →
      </a>
    </div>
  </c:if>

</div>

<footer>
  <span class="footer-brand">⚡ NEXUS GEAR</span> &nbsp;|&nbsp; Módulo 5 Java EE
</footer>
</body>
</html>
