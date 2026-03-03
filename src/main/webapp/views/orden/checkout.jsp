<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Checkout — NEXUS GEAR</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<%@ include file="/views/navbar.jsp" %>

<div class="container" style="max-width:650px">
  <div class="page-header">
    <h1 class="page-title">💳 Confirmar Pedido</h1>
    <a class="btn btn-outline btn-sm" href="<c:url value='/ordenes?accion=carrito'/>">← Carrito</a>
  </div>

  <c:if test="${not empty sessionScope.error}">
    <div class="alert alert-error"><c:out value="${sessionScope.error}"/></div>
    <c:remove var="error" scope="session"/>
  </c:if>

  <%-- Resumen del carrito --%>
  <div class="card" style="margin-bottom:1rem">
    <h3 style="font-size:0.85rem;letter-spacing:1px;text-transform:uppercase;
               color:var(--muted);margin-bottom:1rem">Resumen del pedido</h3>
    <c:set var="total" value="0"/>
    <c:forEach var="entry" items="${sessionScope.carrito}">
      <c:set var="item" value="${entry.value}"/>
      <div style="display:flex;justify-content:space-between;
                  padding:0.4rem 0;font-size:0.85rem;border-bottom:1px solid rgba(255,255,255,0.04)">
        <span><c:out value="${item.productoImagen}"/> <c:out value="${item.productoNombre}"/> ×<c:out value="${item.cantidad}"/></span>
        <span style="color:var(--accent)">$<fmt:formatNumber value="${item.subtotal}" pattern="#,###" groupingSeparator="."/></span>
      </div>
      <c:set var="total" value="${total + item.subtotal}"/>
    </c:forEach>
    <div style="display:flex;justify-content:space-between;padding-top:0.8rem;
                font-size:1.1rem;font-weight:800">
      <span>Total</span>
      <span style="color:var(--accent)">$<fmt:formatNumber value="${total}" pattern="#,###" groupingSeparator="."/></span>
    </div>
  </div>

  <%-- Formulario de datos de envío --%>
  <div class="card">
    <h3 style="font-size:0.85rem;letter-spacing:1px;text-transform:uppercase;
               color:var(--muted);margin-bottom:1rem">Datos de envío</h3>
    <form method="POST" action="<c:url value='/ordenes'/>">
      <input type="hidden" name="accion" value="confirmar">

      <div class="form-row">
        <div class="form-group">
          <label class="form-label">Ciudad *</label>
          <input class="form-control" type="text" name="ciudad" required placeholder="Santiago">
        </div>
        <div class="form-group">
          <label class="form-label">Teléfono</label>
          <input class="form-control" type="text" name="telefono" placeholder="+56 9 XXXX XXXX">
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Dirección de envío *</label>
        <input class="form-control" type="text" name="direccion" required
               placeholder="Calle, número, depto, comuna">
      </div>

      <button class="btn btn-primary btn-full" type="submit" style="margin-top:0.5rem">
        ✅ Confirmar Pedido →
      </button>
    </form>
  </div>
</div>

<footer>
  <span class="footer-brand">⚡ NEXUS GEAR</span> &nbsp;|&nbsp; Módulo 5 Java EE
</footer>
</body>
</html>
