# ⚡ NEXUS GEAR — IntelliJ IDEA + Maven
### Proyecto Módulo 5 — Java EE / MVC / JSP / Servlets / JDBC / DAO

---

## 🚀 Cómo abrir y ejecutar en IntelliJ IDEA

### PASO 1 — Abrir el proyecto

1. Abrir **IntelliJ IDEA**
2. `File → Open` → Seleccionar la carpeta **NexusGearApp**
3. IntelliJ detecta el `pom.xml` automáticamente → clic en **"Load Maven Project"**
4. Esperar que Maven descargue las dependencias (barra inferior)

---

### PASO 2 — Configurar la base de datos MySQL

```sql
-- Abrir MySQL Workbench o terminal y ejecutar:
source database/schema.sql;
```

Editar la contraseña en:
`src/main/java/com/nexusgear/util/DBConexion.java`

```java
private static final String PASSWORD = "TU_PASSWORD_AQUI";
```

---

### PASO 3 — Configurar Tomcat en IntelliJ

#### 3.1 Instalar Tomcat (si no lo tienes)
- Descargar **Apache Tomcat 10.x** de https://tomcat.apache.org/download-10.cgi
- Extraer en una carpeta (ej: `C:\tomcat10` o `/opt/tomcat10`)

#### 3.2 Agregar Tomcat a IntelliJ
1. `Run → Edit Configurations`
2. Clic en **`+`** → **`Tomcat Server → Local`**
3. En la pestaña **Server**:
    - Clic en **Configure...** → Seleccionar la carpeta de Tomcat
4. En la pestaña **Deployment**:
    - Clic en **`+`** → **`Artifact`** → seleccionar `NexusGearApp:war exploded`
5. En **Application context**: escribir `/NexusGearApp`
6. Clic **OK**

#### 3.3 Ejecutar
- Clic en el botón ▶️ **Run** (o `Shift+F10`)
- Se abrirá el navegador en: `http://localhost:8080/NexusGearApp/`

---

### PASO 4 — Generar el archivo WAR (para entrega)

Desde IntelliJ:
1. `Build → Build Artifacts → NexusGearApp:war → Build`
2. El `.war` se genera en: `target/NexusGearApp.war`

O desde la terminal del proyecto:
```bash
mvn clean package
# → target/NexusGearApp.war
```

Para desplegar en Tomcat manualmente:
```bash
cp target/NexusGearApp.war /ruta/tomcat/webapps/
# Iniciar Tomcat:
/ruta/tomcat/bin/startup.sh   # Linux/Mac
/ruta/tomcat/bin/startup.bat  # Windows
```

---

## 🔑 Credenciales de prueba

| Email | Password | Rol |
|-------|----------|-----|
| admin@nexusgear.cl | 123456 | 👑 ADMIN |
| juan@gmail.com | 123456 | 🛒 CLIENTE |
| maria@gmail.com | 123456 | 🛒 CLIENTE |

---

## 📁 Estructura Maven del proyecto

```
NexusGearApp/
├── pom.xml                          ← Dependencias Maven
├── database/
│   └── schema.sql                   ← Ejecutar en MySQL primero
└── src/
    └── main/
        ├── java/com/nexusgear/
        │   ├── model/               ← M — Entidades
        │   │   ├── Usuario.java
        │   │   ├── Producto.java
        │   │   └── Orden.java
        │   ├── dao/                 ← M — Acceso a datos (JDBC)
        │   │   ├── UsuarioDAO.java
        │   │   ├── ProductoDAO.java
        │   │   └── OrdenDAO.java
        │   ├── controller/          ← C — Servlets
        │   │   ├── LoginServlet.java
        │   │   ├── LogoutServlet.java
        │   │   ├── ProductoServlet.java
        │   │   ├── OrdenServlet.java
        │   │   └── UsuarioServlet.java
        │   └── util/
        │       ├── DBConexion.java  ← Singleton JDBC
        │       └── PasswordUtil.java← SHA-256
        └── webapp/
            ├── WEB-INF/
            │   └── web.xml          ← Descriptor de despliegue
            ├── views/               ← V — Vistas JSP + JSTL
            │   ├── navbar.jsp
            │   ├── producto/
            │   │   ├── lista.jsp
            │   │   └── form.jsp
            │   ├── orden/
            │   │   ├── carrito.jsp
            │   │   ├── checkout.jsp
            │   │   ├── lista.jsp
            │   │   └── detalle.jsp
            │   └── usuario/
            │       ├── lista.jsp
            │       └── form.jsp
            ├── css/
            │   └── style.css
            └── index.jsp            ← Login
```

---

## ⚠️ Solución de problemas comunes en IntelliJ

### Error: "javax vs jakarta"
Si usas **Tomcat 9**, los imports en los Servlets deben ser:
```java
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
```
Si usas **Tomcat 10**, dejan de ser `javax` y pasan a ser:
```java
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
```
Los archivos del proyecto usan `jakarta` (Tomcat 10).
Para **Tomcat 9**, también activa la sección comentada en `pom.xml`.

### Error: JSTL tags no funcionan
Verificar que en `pom.xml` estén las dependencias de JSTL sin `<scope>provided</scope>` (deben empaquetarse en el WAR).

### Error: MySQL connection refused
- Verificar que MySQL esté corriendo
- Revisar usuario/password en `DBConexion.java`
- Confirmar que la base de datos `nexusgear_db` existe

### Error: 404 en rutas
- En IntelliJ, verificar que el **Application context** sea `/NexusGearApp`
- Confirmar que el Artifact seleccionado sea `:war exploded`

---

## 📋 Checklist de entrega (Módulo 5)

- [x] Proyecto Maven importable en IntelliJ IDEA
- [x] Patrón MVC: `model` / `dao` / `controller` / `views`
- [x] Servlets con `@WebServlet` (GET y POST)
- [x] `HttpSession` para login, carrito y roles
- [x] JSTL: `c:forEach`, `c:if`, `c:choose`, `c:out`, `c:url`, `c:remove`
- [x] DAO con JDBC y Singleton (`DBConexion`)
- [x] CRUD completo: Productos, Usuarios, Órdenes
- [x] `web.xml` configurado
- [x] WAR generado con `mvn package`
- [x] Contraseñas cifradas con SHA-256

