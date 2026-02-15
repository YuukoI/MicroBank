🏦 MicroBank - Sistema de Microservicios Financieros
MicroBank es una plataforma backend de banca digital diseñada bajo una arquitectura de microservicios, enfocada en la escalabilidad, la trazabilidad y la consistencia de datos. El sistema permite la gestión de usuarios, billeteras virtuales, transacciones entre cuentas y comercios, todo bajo un esquema de auditoría centralizado.

🏗️ Arquitectura del Sistema
La solución se divide en los siguientes componentes:

API Gateway (Spring Cloud Gateway): Punto de entrada único que gestiona el enrutamiento estático hacia los servicios internos y la configuración CORS.
Auth Service: Gestión de autenticación y autorización mediante JWT (JSON Web Tokens).
User Service: Administración de perfiles de usuario y datos personales.
Wallet Service: Core financiero que gestiona balances, recargas y pagos.
Merchant Service: Gestión de comercios y procesamiento de pagos de ventas.
Transaction Service: Registro y procesamiento de movimientos monetarios.
Audit Service: Sistema de log centralizado que consume eventos de Kafka para registrar cada acción crítica del sistema.

🚀 Tecnologías Utilizadas
Lenguaje: Java

Framework: Spring Boot 3.5.4, Spring Cloud (Gateway, Eureka)

Seguridad: Spring Security & JWT

Mensajería: Apache Kafka (Event-driven architecture)

Bases de Datos: MySQL (Instancias independientes por servicio)

Observabilidad:

Micrometer Tracing & Zipkin: Trazabilidad distribuida de peticiones.

Interceptor Pattern: Auditoría automatizada de peticiones HTTP.

Documentación: SpringDoc OpenAPI (Swagger UI)

Contenedores: Docker & Docker Compose

🛠️ Características Destacadas
Arquitectura Orientada a Eventos: Uso de Kafka para desacoplar el servicio de auditoría y procesar pagos de comercios de forma asíncrona.

Trazabilidad Distribuida: Implementación de traceId compartido entre microservicios para seguir el flujo de una transacción desde el Gateway hasta el último servicio de la cadena.

Sistema de Auditoría Híbrido:

Interceptor: Captura automáticamente operaciones administrativas (POST, PUT, DELETE).

Manual: Auditoría de lógica de negocio compleja (transferencias, pagos).

Seguridad Robusta: Control de acceso basado en roles (ADMIN/USER) y validación de tokens centralizada.

📦 Instalación y Despliegue
Requisitos previos
Docker y Docker Compose instalados.

Pasos para ejecutar
Clonar el repositorio:

Agregar un archivo .env con JWT_SECRET_KEY=tu_jwt_key

Bash
git clone https://github.com/YuukoI/MicroBank.git
cd MicroBank
Construir y levantar los contenedores:

Bash
docker-compose up --build -d
Acceder al Dashboard de Eureka para verificar los servicios: http://localhost:8761

Acceder a la documentación Swagger (vía Gateway): http://localhost:8080/swagger-ui.html

📊 Endpoints Principales (vía Gateway - Puerto 8080)
POST /microbank/auth/login - Autenticación de usuarios.

POST /microbank/wallets/{id}/recharge - Recarga de saldo.

POST /microbank/merchant - Creación de comercios (Solo ADMIN).

GET /microbank/audit - Consulta de logs de auditoría (Solo ADMIN).

📝 Nota sobre el Despliegue
El proyecto está diseñado para ser desplegado en una instancia AWS EC2 mediante Docker Compose. Debido a limitaciones de acceso a infraestructura Cloud, el proyecto ha sido testeado y validado en un entorno local simulando las condiciones de red y recursos de una instancia t3.medium. A futuro se desplegará en AWS EC2.
