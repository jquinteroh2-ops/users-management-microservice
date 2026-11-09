# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Verificación

`GET /health` es publico y responde HTTP 200 con los metadatos generados desde
`pom.xml` durante el build:

```json
{"status":"UP","name":"users-management","version":"2.2.0"}
```

Indica que la API responde; no comprueba MySQL ni el proveedor de correo.

```bash
./mvnw clean test
./mvnw clean package
```

En Windows se puede utilizar `mvnw.cmd`.

## Ejercicio 25: CRUDL de Emisora

Además de la entidad `Usuario`, este fork implementa la entidad **Emisora** en todas las capas
de la arquitectura hexagonal:

| Capa | Componentes |
|---|---|
| Dominio | `EmisoraModel`, `GeneroEmisora`, value objects `EmisoraId`, `EmisoraNombre`, `BandaFm`, `BandaAm`, excepciones `InvalidEmisoraException`, `EmisoraNotFoundException`, `EmisoraAlreadyExistsException` |
| Aplicación | Puertos de entrada (`CreateEmisoraUseCase`...), puertos de salida (`SaveEmisoraPort`...), comandos/consultas CQRS, `EmisoraApplicationMapper` y 5 servicios |
| Adaptadores | `EmisoraRepositoryMySQL` (JDBC), DTO, entidad, mapper y excepción de persistencia |
| Entrypoints | `EmisoraRestController` + `EmisoraRestControllerDocs` (Swagger), request/response, mapper y manejo de errores en `GlobalExceptionHandler` |

Atributos: `nombre, canal, bandaFm, bandaAm, numLocutores, genero, horario, patrocinador, pais,
descripcion, numProgramas, numCiudades`.

Reglas de negocio: id y nombre únicos; debe transmitir en FM (87.5–108.0 MHz), en AM
(530–1710 kHz) o en ambas; al menos 1 locutor y 1 ciudad; programas no negativos.

| Método | Ruta | Rol |
|---|---|---|
| GET | `/api/emisoras` | cualquier usuario autenticado |
| GET | `/api/emisoras/{id}` | cualquier usuario autenticado |
| POST | `/api/emisoras` | ADMIN |
| PUT | `/api/emisoras/{id}` | ADMIN |
| DELETE | `/api/emisoras/{id}` | ADMIN |

Primero se obtiene el token con `POST /api/auth/login` (`admin@example.com` / `Admin1234!`).

### Ejecutar con Docker Compose

```bash
docker compose up --build
```

Levanta MySQL 8.4 (con `schema.sql`: tablas `users` y `emisoras` y datos iniciales) y la API en
`http://localhost:8080`. Swagger: `http://localhost:8080/swagger-ui.html`.

### Postman

Importar `postman/Emisoras.postman_collection.json` y ejecutarla en orden con el Collection
Runner (login, listar, crear, duplicado, validaciones, consultar, actualizar, eliminar).
Para probar en la nube basta con cambiar la variable `baseUrl`.

### GitHub Actions

`.github/workflows/ci-cd.yml` compila y ejecuta las pruebas en cada push y pull request,
construye la imagen Docker y, en `main`, llama al Deploy Hook de Render si existe el secreto
`RENDER_DEPLOY_HOOK_URL`.

## Despliegue en Render

El archivo `render.yaml` define el servicio web, el build con Docker y el despliegue
automático de cada commit que llegue a la rama `main`.

1. Crear un Blueprint en Render y seleccionar este repositorio.
2. Completar en el panel los secretos marcados como requeridos: `DB_HOST`,
   `DB_USERNAME`, `DB_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD` y
   `SMTP_FROM_ADDRESS`.
3. Usar una instancia MySQL accesible desde Internet o desde la red privada de
   Render y ejecutar `src/main/resources/schema.sql` una vez para crear el esquema.
4. Desplegar el Blueprint. La API quedará disponible en el subdominio
   `onrender.com` asignado por Render y Swagger UI en `/swagger-ui.html`.

Las credenciales nunca deben guardarse en `application.properties` ni en
`render.yaml`. Para desarrollo local, deben proporcionarse como variables de entorno.
