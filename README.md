# mercado-local-api

Backend para gestionar productores, ofertas, stock y pedidos de un mercado local.

## Integrantes
- Juan Camilo Orozco Londoño
- Miguel Ángel López Betancur

## Tecnologías
Java · Spring Boot · Maven

## Cómo ejecutar
Windows: `.\mvnw.cmd spring-boot:run`
Linux / macOS: `./mvnw spring-boot:run`

## Base de datos

Requiere PostgreSQL 18.

1. Como superusuario (`psql -U postgres`), crear el usuario y la base:

   CREATE USER mercado_app WITH PASSWORD 'tu-contraseña';
   CREATE DATABASE mercado_local OWNER mercado_app;

2. Desde la carpeta del proyecto, crear esquemas y tablas:

   psql -U mercado_app -d mercado_local -v ON_ERROR_STOP=1 -f database/01_esquema.sql

3. Definir la variable de entorno `DB_PASSWORD` con la contraseña de `mercado_app`.

La API queda disponible en `http://localhost:8080`.