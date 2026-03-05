<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NEXUS GEAR — Iniciar Sesión</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>

<div class="login-page">
    <div class="login-box">

        <!-- Logo -->
        <div class="login-logo">⚡ NEXUS GEAR</div>
        <p class="login-sub">Sistema de Gestión Gaming</p>

        <%-- Mensaje de error desde el servlet (c:if con JSTL) --%>
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <%-- Mensaje de sesión cerrada --%>
        <c:if test="${not empty param.msg}">
            <c:if test="${param.msg eq 'logout'}">
                <div class="alert">Sesión cerrada correctamente.</div>
            </c:if>
        </c:if>

        <!-- Formulario de Login -->
        <form method="POST" action="<c:url value='/login'/>" autocomplete="off">

            <div class="form-group">
                <label class="form-label" for="email">Email</label>
                <input class="form-control"
                       type="email"
                       id="email"
                       name="email"
                       placeholder="tu@nexusgear.cl"
                       value="<c:out value='${emailIngresado}'/>"
                       required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Contraseña</label>
                <input class="form-control"
                       type="password"
                       id="password"
                       name="password"
                       placeholder="••••••••"
                       required>
            </div>

            <button class="btn btn-primary btn-full" type="submit" style="margin-top:0.5rem">
                Ingresar →
            </button>
        </form>

        <%-- Credenciales de prueba --%>
        <div style="margin-top:1.5rem;padding:1rem;background:var(--bg3);border:1px solid var(--border);font-size:0.75rem;color:var(--muted)">
            <p style="color:var(--accent2);font-weight:700;margin-bottom:0.4rem">Cuentas de prueba:</p>
            <p>👑 Admin: admin@nexusgear.cl / 123456</p>
            <p>🛒 Cliente: juan@gmail.com / 123456</p>
        </div>

    </div>
</div>

</body>
</html>
