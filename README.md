# 🏦 Banco App — Full Stack (Spring Boot + Angular)

Proyecto full stack desarrollado como prueba técnica, implementando un sistema bancario básico con gestión de clientes, cuentas, movimientos y reportes.

---

# 🚀 Tecnologías

## Backend
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Maven

## Frontend
- Angular (Standalone Components)
- Signals & Computed (Zoneless compatible)
- Reactive Forms
- Jest (testing)

---

# 🧱 Arquitectura

## Backend
- Controller → Endpoints REST
- Service → Lógica de negocio
- Repository → Acceso a datos (JPA)
- Entity → Modelos persistentes

## Frontend
- Standalone Components
- Estado reactivo con Signals
- Servicios HTTP desacoplados

---

# 📦 Funcionalidades

## Clientes
- Crear, editar, eliminar
- Búsqueda rápida

## Cuentas
- Asociación con cliente
- CRUD completo

## Movimientos
- Créditos (+)
- Débitos (-)
- Validaciones:
  - Saldo no disponible
  - Cupo diario excedido

## Reportes
- Filtro por cliente
- Rango de fechas
- Totales de crédito/débito
- Descarga PDF

---

# ⚙️ Requisitos

- Node.js 18+
- Java 17
- Docker
- Maven

---

# 🛠️ Instalación

## Backend

```bash
cd backend
mvn clean package -DskipTests
```

## Docker

```bash
docker compose up --build
```

Backend disponible en:

```
http://localhost:8080/api
```

---

## Frontend

```bash
cd frontend/banco-web
npm install
ng serve
```

Frontend disponible en:

```
http://localhost:4200
```

---

# 🔌 Configuración

## Angular environment

```ts
apiUrl: 'http://<IP_BACKEND>:8080/api'
```

## CORS Backend

```yaml
allowed-origins: http://localhost:4200
```

---

# 🧪 Tests

## Backend

```bash
mvn test
```

## Frontend

```bash
npm test
```

---

# 📡 Endpoints principales

## Clientes
- GET /api/clientes
- POST /api/clientes
- PUT /api/clientes/{id}
- DELETE /api/clientes/{id}

## Cuentas
- GET /api/cuentas
- POST /api/cuentas
- PUT /api/cuentas/{id}
- DELETE /api/cuentas/{id}

## Movimientos
- GET /api/movimientos
- POST /api/movimientos

## Reportes
- GET /api/reportes
- GET /api/reportes/pdf

---

# 🧠 Decisiones técnicas

- Uso de Signals para compatibilidad con Angular zoneless
- Separación clara de capas en backend
- Validaciones de negocio en Service
- Manejo centralizado de errores
- Dockerización para entorno reproducible

---

# 📁 Entregables

- Repositorio GitHub
- Archivo BaseDatos.sql
- Colección Postman
- Archivo comprimido (.zip)

---

# 👨‍💻 Autor

Santiago Da Silva Santos

---

# ✅ Estado

✔ Backend completo
✔ Frontend completo
✔ Tests backend
✔ Tests frontend
✔ Docker listo

---

# 🎯 Resultado

Aplicación funcional end-to-end lista para evaluación técnica.

