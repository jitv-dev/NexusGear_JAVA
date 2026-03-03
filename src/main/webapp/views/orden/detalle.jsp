<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Detalle Orden — NEXUS GEAR</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<%@ include file="/views/navbar.jsp" %>

<div class="container" style="max-width:700px">
  <div class="page-header">
    <h1 class="page-title">📋 Detalle de Orden</h1>
    <a class="btn btn-outline btn-sm" href="<c:url value='/ordenes'/>">← Mis Órdenes</a>
  </div>

  <c:if test="${not empty orden}">
    <div class="card" style="margin-bottom:1rem">
      <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;font-size:0.88rem">
        <div>
          <div style="color:var(--muted);font-size:0.72rem;letter-spacing:1px;margin-bottom:0.2rem">N° ORDEN</div>
          <div style="font-family:monospace;color:var(--accent2)"><c:out value="${orden.numeroOrden}"/></div>
        </div>
        <div>
          <div style="color:var(--muted);font-size:0.72rem;letter-spacing:1px;margin-bottom:0.2rem">ESTADO</div>
          <span class="estado estado-<c:out value='${orden.estado}'/>"><c:out value="${orden.estado}"/></span>
        </div>
        <div>
          <div style="color:var(--muted);font-size:0.72rem;letter-spacing:1px;margin-bottom:0.2rem">FECHA</div>
          <fmt:formatDate value="${orden.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
        </div>
        <div>
          <div style="color:var(--muted);font-size:0.72rem;letter-spacing:1px;margin-bottom:0.2rem">CIUDAD</div>
          <c:out value="${orden.ciudad}"/>
        </div>
        <div style="grid-column:1/-1">
          <div style="color:var(--muted);font-size:0.72rem;letter-spacing:1px;margin-bottom:0.2rem">DIRECCIÓN</div>
          <c:out value="${orden.direccion}"/>
        </div>
      </div>
    </div>

    <div class="card">
      <h3 style="font-size:0.82rem;letter-spacing:1px;text-transform:uppercase;color:var(--muted);margin-bottom:1rem">Productos</h3>
      <c:forEach var="item" items="${orden.items}">
        <div class="carrito-item">
          <div class="carrito-item-img"><c:out value="${item.productoImagen}"/></div>
          <div class="carrito-item-info">
            <div class="carrito-item-name"><c:out value="${item.productoNombre}"/></div>
            <div class="carrito-item-price">
              $<fmt:formatNumber value="${item.precioUnit}" pattern="#,###" groupingSeparator="."/>
              × <c:out value="${item.cantidad}"/>
            </div>
          </div>
          <div style="color:var(--accent);font-weight:800">
            $<fmt:formatNumber value="${item.subtotal}" pattern="#,###" groupingSeparator="."/>
          </div>
        </div>
      </c:forEach>
      <div class="carrito-total">
        <span class="carrito-total-label">Total pagado</span>
        <span class="carrito-total-val"><c:out value="${orden.totalFormateado}"/></span>
      </div>
    </div>
  </c:if>
</div>

<footer>
  <span class="footer-brand">⚡ NEXUS GEAR</span> &nbsp;|&nbsp; Módulo 5 Java EE
</footer>
</body>
</html>
