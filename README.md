# ☕ Yireh Store Backend

Backend de Yireh Store, una plataforma de cafetería digital y comercio electrónico diseñada para gestionar productos, pedidos, billetera virtual y procesos de despacho dentro de entornos académicos.

## 🚀 Características

- Registro e inicio de sesión de usuarios.
- Autenticación y autorización mediante JWT.
- Gestión de usuarios y roles.
- Gestión de categorías y productos.
- Carrito de compras.
- Billetera virtual.
- Sistema de recargas.
- Gestión de pedidos.
- Actualización de estados de pedidos.
- Auditoría y registros del sistema.
- API REST segura y escalable.

---

## 🏗️ Arquitectura

El proyecto sigue los principios de:

- Hexagonal Architecture (Ports & Adapters)
- Clean Architecture
- SOLID Principles
- Domain Driven Design (DDD)

### Capas del Proyecto

```text
src/main/java
│
├── domain
│   ├── model
│   ├── port
│   └── usecase
│
├── application
│
├── infrastructure
│   ├── input
│   ├── output
│   ├── security
│   └── config
│
└── BackendYirehStoreApplication
```

---

## ⚙️ Tecnologías Utilizadas

### Backend

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Maven

### Seguridad

- Spring Security
- JWT (JSON Web Tokens)
- BCrypt Password Encoder

### Base de Datos

- MySQL 8
- H2 Database (Testing)

### Mapeo y Productividad

- Lombok
- MapStruct

### Configuración

- Dotenv Java

### Calidad de Código

- JUnit
- JaCoCo
- Spring Boot Test

---

## 📦 Dependencias Principales

- spring-boot-starter-webmvc
- spring-boot-starter-security
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- mysql-connector-j
- jjwt-api
- jjwt-impl
- jjwt-jackson
- lombok
- mapstruct
- dotenv-java
- h2database
- jacoco-maven-plugin

---

## 🗄️ Base de Datos

Entidades principales del sistema:

- Users
- User Roles
- Categories
- Products
- Carts
- Cart Items
- Orders
- Order Items
- Recharges
- System Logs

---

## 🔐 Seguridad

La aplicación implementa:

- Autenticación basada en JWT.
- Contraseñas cifradas mediante BCrypt.
- Roles de usuario.
- Protección de endpoints.
- Control de acceso basado en permisos.

---

## 👥 Roles

### USER

- Consultar productos.
- Gestionar carrito.
- Realizar pedidos.
- Consultar historial.
- Administrar billetera.

### ADMIN

- Administrar productos.
- Administrar categorías.
- Gestionar usuarios.
- Supervisar pedidos.
- Actualizar estados de despacho.
- Consultar registros de auditoría.

---

## 📡 API REST

### Auth

```http
POST /api/auth/register
POST /api/auth/login
```

### Categorías

```http
GET /api/categories
```

### Productos

```http
GET /api/products/search
```

### Carrito

```http
GET /api/cart
POST /api/cart/items
```

### Billetera

```http
GET /api/wallet/balance
POST /api/wallet/recharge
```

### Pedidos

```http
POST /api/orders/checkout
GET /api/orders/my-orders
```

### Administración

```http
GET /api/orders/admin
PATCH /api/orders/{id}/status
```

### Usuarios

```http
GET /api/users/me
PUT /api/users/me
```

### Logs

```http
POST /api/logs
```

---

## 🔧 Configuración

### Requisitos

- JDK 17
- Maven 3.9+
- MySQL 8+
- Git

### Crear Base de Datos

```sql
CREATE DATABASE yireh_store;
```

### Variables de Entorno

Crear archivo `.env`

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=yireh_store
DB_USER=root
DB_PASSWORD=password

JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000
```

### application.properties

```properties
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## ▶️ Ejecución

Clonar repositorio:

```bash
git clone https://github.com/cesaralvarezv/backend_proyecto-_aula.git
```

Ingresar al proyecto:

```bash
cd backend_proyecto-_aula
```

Compilar:

```bash
mvn clean install
```

Ejecutar:

```bash
mvn spring-boot:run
```

---

## ✅ Testing

Ejecutar pruebas:

```bash
mvn test
```

Generar reporte JaCoCo:

```bash
mvn verify
```

El reporte se generará en:

```text
target/site/jacoco/index.html
```

---

## 📌 Estado del Proyecto

✅ Arquitectura Hexagonal

✅ Spring Security + JWT

✅ Persistencia con JPA

✅ Base de Datos MySQL

✅ Gestión de Usuarios

✅ Gestión de Productos

✅ Carrito de Compras

✅ Billetera Virtual

✅ Gestión de Pedidos

✅ Auditoría y Logs

---

## 👨‍💻 Autor

**Cesar Augusto Alvarez**

Proyecto de Aula - Ingeniería de Software

**Yireh Store**
