<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Órdenes — NEXUS GEAR</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<%@ include file="/views/navbar.jsp" %>

<div class="container">
  <div class="page-header">
    <h1 class="page-title">
      📦 <c:choose>
        <c:when test="${sessionScope.esAdmin}">Todas las Órdenes</c:when>
        <c:otherwise>Mis Órdenes</c:otherwise>
      </c:choose>
    </h1>
  </div>

  <c:if test="${empty ordenes}">
    <div class="alert">No hay órdenes registradas aún.</div>
  </c:if>

  <c:if test="${not empty ordenes}">
    <div class="card" style="padding:0">
      <table>
        <thead>
          <tr>
            <th>N° Orden</th>
            <c:if test="${sessionScope.esAdmin}"><th>Cliente</th></c:if>
            <th>Total</th>
            <th>Estado</th>
            <th>Fecha</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="o" items="${ordenes}">
            <tr>
              <td style="font-family:monospace;color:var(--accent2)">
                <c:out value="${o.numeroOrden}"/>
              </td>
              <c:if test="${sessionScope.esAdmin}">
                <td><c:out value="${o.usuarioNombre}"/></td>
              </c:if>
              <td><c:out value="${o.totalFormateado}"/></td>
              <td>
                <span class="estado estado-<c:out value='${o.estado}'/>">
                  <c:out value="${o.estado}"/>
                </span>
              </td>
              <td style="color:var(--muted);font-size:0.8rem">
                <fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
              </td>
              <td>
                <a class="btn btn-outline btn-sm"
                   href="<c:url value='/ordenes?accion=detalle&id=${o.id}'/>">
                  Ver
                </a>
                <%-- Solo admin puede cambiar estado --%>
                <c:if test="${sessionScope.esAdmin}">
                  <form method="POST" action="<c:url value='/ordenes'/>" style="display:inline">
                    <input type="hidden" name="accion" value="cambiarEstado">
                    <input type="hidden" name="id" value="${o.id}">

                    <select name="estado"
                            class="form-control btn-sm"
                            style="width: auto; display: inline; padding: 0.3rem 0.5rem"
                            onchange="this.form.submit()">
                      <option value="PENDIENTE"  <c:if test="${o.estado eq 'PENDIENTE'}"> selected > PENDIENTE</c:if></option>
                      <option value="PAGADO"     <c:if test="${o.estado eq 'PAGADO'"> selected > PAGADO</c:if></option>
                      <option value="ENVIADO"    <c:if test="${o.estado eq 'ENVIADO'"> selected > ENVIADO</c:if></option>
                      <option value="ENTREGADO"  <c:if test="${o.estado eq 'ENTREGADO'"> selected > ENTREGADO</c:if></option>
                      <option value="CANCELADO"  <c:if test="${o.estado eq 'CANCELADO'"> selected > CANCELADO</c:if></option>
                    </select>
                  </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>
</div>

<footer>
  <span class="footer-brand">⚡ NEXUS GEAR</span> &nbsp;|&nbsp; Módulo 5 Java EE
</footer>
</body>
</html>
