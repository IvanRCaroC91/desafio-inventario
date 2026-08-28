# Sistema de Gestión de Inventarios y Ventas Simuladas

**Formato Estándar:** FO-EP-1.19.LL  
**Metodología:** Spec-Driven Development (SDD)  
**Autor:** Ivan Rene Caro Cataño  
**Fecha:** 27 de Agosto de 2026  
**Versión:** 1.0.1

---

## Stack Tecnológico

- **Backend:** Java 21, Spring Boot 3, Spring Security (JWT), Spring Data JPA
- **Base de Datos:** PostgreSQL 16
- **Frontend:** React + Vite
- **Orquestación:** Docker & Docker Compose
- **Seguridad:** BCryptPasswordEncoder, JWT Stateless, CORS

---

## Arquitectura del Sistema & Patrones de Diseño

### Arquitectura Hexagonal (Puertos y Adaptadores)

El backend sigue estrictamente el patrón de Arquitectura Hexagonal, separando el dominio puro de la infraestructura:

```
┌─────────────────────────────────────────────────────────────────┐
│                        BACKEND (Spring Boot)                     │
├─────────────────────────────────────────────────────────────────┤
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                    DOMAIN (Dominio Puro)                   │  │
│  │  - Entidades: Usuario, Producto, Venta, DetalleVenta      │  │
│  │  - Puertos: UsuarioRepositoryPort, ProductoRepositoryPort │  │
│  │  - Casos de Uso: RealizarVentaUseCase, LoginUseCase       │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              ↕                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                  APPLICATION (Casos de Uso)               │  │
│  │  - Orquestación de lógica de negocio                       │  │
│  │  - Coordinación entre puertos y adaptadores                 │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              ↕                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              INFRASTRUCTURE (Adaptadores)                  │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │ IN (Entrada):                                         │  │  │
│  │  │ - AuthController (REST API)                           │  │  │
│  │  │ - ProductoController (REST API)                      │  │  │
│  │  │ - VentaController (REST API)                          │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │ OUT (Salida):                                        │  │  │
│  │  │ - UsuarioJpaRepository (Spring Data JPA)             │  │  │
│  │  │ - ProductoJpaRepository (Spring Data JPA)            │  │  │
│  │  │ - VentaJpaRepository (Spring Data JPA)               │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │ SECURITY:                                           │  │  │
│  │  │ - JwtAuthenticationFilter (JWT Stateless)           │  │  │
│  │  │ - SecurityConfig (Spring Security)                   │  │  │
│  │  │ - JwtService (Generación/Validación JWT)            │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                    FRONTEND (React + Vite)                       │
│  - Login.jsx (Autenticación JWT)                                │
│  - AuthContext.jsx (Gestión de sesión)                          │
│  - Componentes de catálogo y carrito                             │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                  POSTGRESQL 16 (Base de Datos)                  │
│  - Tablas: usuarios, productos, ventas, detalle_ventas           │
│  - Restricciones CHECK (stock >= 0, precio > 0)                 │
│  - Contraseñas encriptadas con BCrypt                            │
└─────────────────────────────────────────────────────────────────┘
```

### Orquestación con Docker Compose

El sistema se orquesta mediante `docker-compose.yml` que define tres servicios en la misma red:

1. **postgres-db:** Contenedor PostgreSQL 16 con volumen persistente
2. **backend-app:** Contenedor Spring Boot expuesto en puerto 8082
3. **frontend-app:** Contenedor React + Vite expuesto en puerto 3000

---

## Matriz de Cumplimiento de Reglas de Negocio (RN)

| Regla | Descripción | Implementación | Estado |
|-------|-------------|----------------|--------|
| **RN-01** | Autenticación Stateless con JWT | - `JwtAuthenticationFilter` intercepta requests<br>- `JwtService` genera tokens con expiración 1h<br>- `SecurityConfig` configura sesión STATELESS | ✅ Cumplido |
| **RN-02** | Control de Stock e Invariantes | - Validación en `RealizarVentaUseCase` antes de persistir<br>- Restricción CHECK `stock >= 0` en PostgreSQL<br>- Excepción HTTP 400 cuando stock insuficiente | ✅ Cumplido |
| **RN-03** | Descuento atómico de inventario | - Transacción `@Transactional` en `RealizarVentaUseCase`<br>- Actualización de stock dentro de la misma transacción<br>- Rollback automático si falla cualquier operación | ✅ Cumplido |
| **RN-04** | Recálculo de montos en Backend (Zero Trust) | - Backend recalcula totales desde precios en BD<br>- Frontend solo envía IDs y cantidades<br>- No se confía en montos enviados desde el cliente | ✅ Cumplido |

---

## Ciberseguridad & Protección de Datos

### Encriptado de Contraseñas

- **Algoritmo:** BCryptPasswordEncoder (factor de trabajo 10)
- **Implementación:** Bean `passwordEncoder()` en `SecurityConfig`
- **Hash en BD:** Contraseñas almacenadas como hashes BCrypt en `usuarios.password_hash`
- **Verificación:** `passwordEncoder.matches(rawPassword, encodedHash)` en `AuthController`

### Configuración CORS

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:3000"));
    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    // ...
}
```

- **Origen Permitido:** `http://localhost:3000` (frontend)
- **Métodos Permitidos:** GET, POST, OPTIONS
- **Headers Permitidos:** Authorization, Content-Type

### Filtro JWT y Manejo de Excepciones

- **JwtAuthenticationFilter:** Valida tokens Bearer en header `Authorization`
- **Sin Sesión:** `SessionCreationPolicy.STATELESS` (sin cookies ni sesiones server-side)
- **Códigos HTTP:**
  - **200:** Login exitoso / Operación completada
  - **400:** Error de validación / Stock insuficiente
  - **401:** Credenciales inválidas / Token expirado
  - **403:** Acceso denegado (sin token válido)

---

## Guía Paso a Paso de Despliegue Local

### Prerrequisitos

- Docker Desktop instalado y ejecutándose
- Git instalado (para clonar el repositorio)
- 8GB+ de RAM disponibles
- Puertos libres: 3000 (React), 8082 (Spring Boot), 5432 (PostgreSQL)

### Despliegue en un Solo Paso

```bash
# Clonar el repositorio
git clone <URL_DEL_REPOSITORIO>
cd desafio-inventario

# Construir y levantar todos los servicios
docker-compose up --build
```

### Credenciales de Acceso

- **Usuario:** `admin`
- **Contraseña:** `admin`
- **URL Frontend:** http://localhost:3000
- **URL Backend:** http://localhost:8082
- **URL PostgreSQL:** postgresql://localhost:5432/inventario

### Verificación de Servicios

```bash
# Verificar que los 3 contenedores estén corriendo
docker-compose ps

# Debería mostrar:
# postgres-db    Up (healthy)
# backend-app    Up
# frontend-app   Up
```

---

## Guía de Pruebas y Evidencias de Funcionamiento

### Prueba 1: Login JWT

1. Abrir http://localhost:3000 en el navegador
2. Ingresar credenciales: `admin` / `admin`
3. Verificar que se redirija al catálogo
4. Abrir DevTools → Network → Ver request `/api/auth/login`
5. Confirmar respuesta HTTP 200 con token JWT en body

### Prueba 2: Carga del Catálogo

1. Tras login exitoso, verificar que se muestren los productos
2. Abrir DevTools → Network → Ver request `/api/productos`
3. Confirmar respuesta HTTP 200 con lista de productos y stock actual

### Prueba 3: Venta Exitosa

1. Agregar productos al carrito (cantidad <= stock disponible)
2. Proceder al checkout
3. Verificar mensaje de éxito
4. Verificar en DevTools → Network → request `/api/ventas` con HTTP 201
5. Confirmar que el stock se actualizó en el catálogo

### Prueba 4: Validación de Stock Insuficiente

1. Intentar agregar más unidades de un producto que su stock disponible
2. Proceder al checkout
3. Verificar mensaje de error "Stock insuficiente"
4. Verificar en DevTools → Network → request `/api/ventas` con HTTP 400
5. Confirmar que el stock NO se modificó

### Consulta SQL para Verificar Persistencia

```bash
# Conectarse a PostgreSQL
docker exec -it postgres-db psql -U postgres -d inventario

# Consultar ventas realizadas
SELECT * FROM ventas;

# Consultar detalles de ventas
SELECT * FROM detalle_ventas;

# Verificar stock actualizado
SELECT id, nombre, stock FROM productos;
```

---

## Estructura del Repositorio

```
desafio-inventario/
├── backend/                    # Proyecto Spring Boot (Java 21)
│   ├── src/
│   │   └── main/
│   │       ├── java/com/lucasian/inventarioservice/
│   │       │   ├── domain/           # Dominio puro (entidades, puertos)
│   │       │   ├── application/      # Casos de uso
│   │       │   └── infrastructure/   # Adaptadores (REST, JPA, Security)
│   │       └── resources/
│   │           └── application.yml   # Configuración Spring Boot
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                   # Proyecto React + Vite
│   ├── src/
│   │   ├── components/
│   │   ├── contexts/
│   │   └── App.jsx
│   ├── package.json
│   └── vite.config.js
├── docker-compose.yml          # Orquestación de contenedores
├── init.sql                    # Script DDL/DML de inicialización
├── README.md                   # Este documento
└── .gitignore                  # Archivos ignorados por Git
```

---

## Notas Técnicas

- **Arquitectura Hexagonal Estricta:** El dominio NO depende de Spring, JPA ni ningún framework
- **Zero Trust Frontend:** El backend recalcula todos los montos y valida todas las reglas
- **Transaccionalidad:** Las ventas son atómicas; si falla cualquier paso, se hace rollback completo
- **Seguridad:** No se usan sesiones server-side; todo es stateless con JWT
- **Persistencia:** PostgreSQL con restricciones CHECK para garantizar integridad de datos

---

## Historial de Versiones

### v1.0.1 (27 de Agosto de 2026)
- **Corrección:** Interceptor JWT ahora lee token directamente de localStorage en lugar del estado de React
- **Corrección:** `ProductoPersistenceMapper` ahora establece `createdAt` al crear entidades
- **Mejora:** Validación de stock en frontend (CartContext)
- **Mejora:** Visualización de precios, subtotales y total en carrito
- **Mejora:** Refresh automático de productos después de checkout exitoso
- **Mejora:** Manejo de errores específicos con mensajes descriptivos

### v1.0.0 (27 de Agosto de 2026)
- Versión inicial con arquitectura hexagonal completa
- Autenticación JWT stateless
- Gestión de inventario y ventas
- Validación de stock en backend

---

## Documentación Adicional

- **INFORME_FINAL.md:** Informe de cumplimiento con estado de implementación y pruebas realizadas

---

**Fin del Documento de Entrega - FO-EP-1.19.LL**
