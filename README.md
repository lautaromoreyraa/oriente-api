# Oriente Landing Page Backend

Este repositorio contiene el backend para la administración de la landing page de Oriente, un consultorio especializado en kinesiología y estética personal. La API está diseñada para gestionar secciones como "About", "Combos", "Hero Section" y "Services" de la página web.

## Tecnologías principales
- **Java** con **Spring Boot**
- **Maven** para la gestión de dependencias
- **MySQL** como base de datos

## Funcionalidades principales
- Gestión de secciones de contenido dinámico para la landing page.
- Endpoints RESTful para la administración de datos.
- Validación de datos y manejo de excepciones.

## Arquitectura y patrones
- Layered Architecture — separación en capas Controller → Service → Repository → Entity. Cada capa tiene una única responsabilidad y se comunica solo con la inmediata inferior.
- DTO Pattern — los datos nunca salen de la capa de persistencia como entidades crudas. Se usa un Request para entrada y un Response para salida, desacoplando la API del modelo de dominio.
- Repository Pattern — acceso a datos abstraído mediante interfaces JpaRepository. La lógica de negocio no sabe cómo se persisten los datos.
- JWT Auth Filter — seguridad stateless mediante filtro OncePerRequestFilter. Los GET son públicos; POST/PUT/DELETE requieren Bearer token.
- Builder Pattern — Lombok @Builder en todas las entidades y DTOs para construcción legible sin constructores manuales.

---
