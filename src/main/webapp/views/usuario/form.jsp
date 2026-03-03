<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Nuevo Usuario — NEXUS GEAR</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<%@ include file="/views/navbar.jsp" %>
<div class="container" style="max-width:500px">
  <div class="page-header">
    <h1 class="page-title">➕ Nuevo Usuario</h1>
    <a class="btn btn-outline btn-sm" href="<c:url value='/usuarios'/>">← Volver</a>
  </div>
  <div class="card">
    <form method="POST" action="<c:url value='/usuarios'/>">
      <input type="hidden" name="accion" value="crear">
      <div class="form-row">
        <div class="form-group">
          <label class="form-label">Nombre *</label>
          <input class="form-control" type="text" name="nombre" required>
        </div>
        <div class="form-group">
          <label class="form-label">Apellido *</label>
          <input class="form-control" type="text" name="apellido" required>
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">Email *</label>
        <input class="form-control" type="email" name="email" required>
      </div>
      <div class="form-group">
        <label class="form-label">Contraseña *</label>
        <input class="form-control" type="password" name="password" required minlength="6">
      </div>
      <div class="form-group">
        <label class="form-label">Rol</label>
        <select class="form-control" name="rol">
          <option value="CLIENTE">🛒 CLIENTE</option>
          <option value="ADMIN">👑 ADMIN</option>
        </select>
      </div>
      <button class="btn btn-primary" type="submit">Crear Usuario</button>
    </form>
  </div>
</div>
<footer><span class="footer-brand">⚡ NEXUS GEAR</span></footer>
</body>
</html>
