<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Usuarios — NEXUS GEAR Admin</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<%@ include file="/views/navbar.jsp" %>

<div class="container">
  <div class="page-header">
    <h1 class="page-title">👥 Gestión de Usuarios</h1>
    <a class="btn btn-primary btn-sm" href="<c:url value='/usuarios?accion=nuevo'/>">
      + Nuevo Usuario
    </a>
  </div>

  <div class="card" style="padding:0">
    <table>
      <thead>
        <tr>
          <th>ID</th><th>Nombre</th><th>Email</th><th>Rol</th><th>Estado</th><th>Registro</th><th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="u" items="${usuarios}">
          <tr>
            <td style="color:var(--muted)">#<c:out value="${u.id}"/></td>
            <td><c:out value="${u.nombreCompleto}"/></td>
            <td style="color:var(--muted)"><c:out value="${u.email}"/></td>
            <td>
              <c:choose>
                <c:when test="${u.rol eq 'ADMIN'}">
                  <span style="color:var(--amber);font-weight:700">👑 ADMIN</span>
                </c:when>
                <c:otherwise>
                  <span style="color:var(--muted)">🛒 CLIENTE</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${u.activo}">
                  <span style="color:var(--accent);font-size:0.8rem">● Activo</span>
                </c:when>
                <c:otherwise>
                  <span style="color:var(--red);font-size:0.8rem">● Inactivo</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td style="color:var(--muted);font-size:0.8rem">
              <fmt:formatDate value="${u.createdAt}" pattern="dd/MM/yyyy"/>
            </td>
            <td>
              <c:if test="${u.activo and u.rol ne 'ADMIN'}">
                <form method="GET" action="<c:url value='/usuarios'/>"
                      onsubmit="return confirm('¿Desactivar este usuario?')">
                  <input type="hidden" name="accion" value="eliminar">
                  <input type="hidden" name="id" value="${u.id}">
                  <button class="btn btn-danger btn-sm" type="submit">Desactivar</button>
                </form>
              </c:if>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<footer>
  <span class="footer-brand">⚡ NEXUS GEAR</span> &nbsp;|&nbsp; Módulo 5 Java EE
</footer>
</body>
</html>
