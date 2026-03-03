<%--
  Created by IntelliJ IDEA.
  User: sabin
  Date: 02-03-2026
  Time: 20:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><c:choose><c:when test="${not empty producto}">Editar</c:when><c:otherwise>Nuevo</c:otherwise></c:choose> Producto — NEXUS GEAR</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<%@ include file="/views/navbar.jsp" %>

<div class="container" style="max-width:700px">
    <div class="page-header">
        <h1 class="page-title">
            <c:choose>
                <c:when test="${not empty producto}">✏️ Editar Producto</c:when>
                <c:otherwise>➕ Nuevo Producto</c:otherwise>
            </c:choose>
        </h1>
        <a class="btn btn-outline btn-sm" href="<c:url value='/productos'/>">← Volver</a>
    </div>

    <div class="card">
        <form method="POST" action="<c:url value='/productos'/>">

            <%-- Acción --%>
            <c:choose>
                <c:when test="${not empty producto}">
                    <input type="hidden" name="accion" value="actualizar">
                    <input type="hidden" name="id" value="${producto.id}">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="accion" value="crear">
                </c:otherwise>
            </c:choose>

            <div class="form-group">
                <label class="form-label">Nombre del Producto *</label>
                <input class="form-control" type="text" name="nombre" required
                       value="<c:out value='${producto.nombre}'/>" placeholder="Ej: DeathAdder V3 Pro">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Precio (CLP) *</label>
                    <input class="form-control" type="number" name="precio" required min="0" step="1"
                           value="<c:out value='${producto.precio}'/>" placeholder="89990">
                </div>
                <div class="form-group">
                    <label class="form-label">Precio Anterior (opcional)</label>
                    <input class="form-control" type="number" name="precioAnterior" min="0" step="1"
                           value="<c:out value='${producto.precioAnterior}'/>" placeholder="109990">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Stock *</label>
                    <input class="form-control" type="number" name="stock" required min="0"
                           value="<c:out value='${producto.stock}'/>" placeholder="50">
                </div>
                <div class="form-group">
                    <label class="form-label">Imagen (emoji)</label>
                    <input class="form-control" type="text" name="imagen" maxlength="10"
                           value="<c:out value='${producto.imagen}'/>" placeholder="🖱️">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">Categoría ID *</label>
                    <input class="form-control" type="number" name="categoriaId" required min="1"
                           value="<c:out value='${producto.categoriaId}'/>"
                           placeholder="1=Teclados 2=Mouses 3=Headsets">
                </div>
                <div class="form-group">
                    <label class="form-label">Marca ID *</label>
                    <input class="form-control" type="number" name="marcaId" required min="1"
                           value="<c:out value='${producto.marcaId}'/>"
                           placeholder="1=Razer 2=Logitech 3=SteelSeries">
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Especificaciones técnicas</label>
                <input class="form-control" type="text" name="especificaciones"
                       value="<c:out value='${producto.especificaciones}'/>"
                       placeholder="Ej: 30000 DPI · Wireless · 63g">
            </div>

            <div class="form-group">
                <label class="form-label">Descripción</label>
                <textarea class="form-control" name="descripcion" rows="3"
                          placeholder="Descripción del producto..."><c:out value='${producto.descripcion}'/></textarea>
            </div>

            <div class="form-group">
                <label class="form-label">Badge</label>
                <select class="form-control" name="badge">
                    <option value="">-- Sin badge --</option>
                    <option value="new"  <c:if test="${producto.badge eq 'new'}">selected</c:if>>● NEW</option>
                    <option value="hot"  <c:if test="${producto.badge eq 'hot'}">selected</c:if>>🔥 HOT</option>
                    <option value="sale" <c:if test="${producto.badge eq 'sale'}">selected</c:if>>% SALE</option>
                </select>
            </div>

            <div style="display:flex;gap:1rem;margin-top:0.5rem">
                <button class="btn btn-primary" type="submit">
                    <c:choose>
                        <c:when test="${not empty producto}">💾 Guardar Cambios</c:when>
                        <c:otherwise>➕ Crear Producto</c:otherwise>
                    </c:choose>
                </button>
                <a class="btn btn-outline" href="<c:url value='/productos'/>">Cancelar</a>
            </div>

        </form>
    </div>
</div>

<footer>
    <span class="footer-brand">⚡ NEXUS GEAR</span> &nbsp;|&nbsp; Módulo 5 Java EE
</footer>
</body>
</html>


