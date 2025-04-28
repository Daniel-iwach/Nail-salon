# Nail Salon 💅

**Nail Salon** es un proyecto backend desarrollado con **Spring Boot** y **MongoDB** para la **gestión de turnos** en un salón de uñas.  
Incluye **login seguro** con **Spring Security**, control de acceso por **roles** (usuarios y administradores), y una arquitectura lista para **escalar** y seguir creciendo.

Este proyecto fue desarrollado **de forma personal** por **Daniel Iwach**.

## 🚀 ¿Cómo probar el proyecto?

Tienes dos formas de probar la aplicación:

- **Producción** (desplegado en Render - puede tardar unos segundos en iniciar por ser un servicio gratuito):
  - [Acceder a la API en producción](https://nail-salon-rae7.onrender.com)

- **Swagger UI** (documentación interactiva para probar los endpoints):
  - [Acceder a Swagger](https://nail-salon-rae7.onrender.com/swagger-ui/index.html)

## 🛠️ Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Security
- MongoDB Atlas
- Docker
- Swagger (OpenAPI)
- Render (hosting gratuito)
- JUnit 5 (testing)
- Mockito (testing)

## 🔐 Cuentas de prueba

Te recomendamos **no usar credenciales reales**.  
Puedes loguearte con los siguientes usuarios de prueba:

| Rol | Username | Password |
|:---|:---|:---|
| Administrador | `admin` | `admin123` |
| Usuario | `user1` | `user123` |
| Usuario | `user2` | `user123` |

> ⚠️ **Nota de seguridad**:  
> Al loguearte con las cuentas de prueba, **puede saltar una advertencia de seguridad en Google** debido a que las contraseñas son muy simples.  
> Sin embargo, las **contraseñas están encriptadas** en la base de datos usando algoritmos seguros.

## 👥 Funcionalidades por rol

### Usuarios
- Agendar turnos.
- Ver sus propios turnos.
- Cancelar turnos.

### Administradores
- Ver todos los usuarios registrados.
- Ver todos los turnos con opciones de **filtrado** por estado, fecha, etc.
- Crear nuevas fechas de turnos disponibles.
- Cambiar el estado de los turnos a **disponible** o **cancelado**.

## 🐳 Docker

El proyecto está **dockerizado** para facilitar su despliegue local.

Para correrlo localmente usando Docker:

```bash
docker-compose up --build
```

Esto levantará el backend y lo conectará automáticamente a la base de datos MongoDB Atlas.

## 🔧 Notas importantes para correr en local

- Si vas a correr el proyecto **de forma local** con tu **propio servidor MongoDB**, debes:
  - **Comentar** la configuración de conexión a **MongoDB Atlas**.
  - **Descomentar** la configuración para conexión a **MongoDB local** (`mongodb://localhost:27017/`).
- Las configuraciones suelen estar en `application.properties` o `application.yml`.

## 🧪 Testing

El proyecto incluye **tests unitarios** utilizando:

- **JUnit 5**: para validar la lógica de negocio.
- **Mockito**: para simular el comportamiento de servicios y repositorios en pruebas unitarias.

Puedes ejecutar los tests desde tu IDE o usando Maven:

```bash
./mvnw test
```

## ⚙️ Validaciones importantes

- No se permite registrar usuarios con **mismo nombre de usuario** o **mismo email**.
- Las contraseñas se almacenan **encriptadas** para proteger la información del usuario.

## 📈 Estado del proyecto

Este es un proyecto en constante crecimiento.  
La idea es convertir **Nail Salon** en un **software completo** para la **gestión integral de turnos y usuarios**, con más funcionalidades administrativas, reportes, recordatorios automáticos, entre otras mejoras.

## 🛤️ Próximos pasos

- Implementar **login con OAuth2** usando **Google**.
- Validación de **correo real** mediante envío de email al registrarse.
- **Recuperación de contraseña** a través de email seguro.
- Envío de **confirmación de turno** por correo electrónico al reservar.
- Envío de **recordatorio 24 horas antes** del turno reservado.
- **Personalización del perfil** de usuario (avatar, nombre, etc.).
- Creación de un **panel de ganancias** para el administrador.
- Mejoras en la gestión de reportes y estadísticas.

## 📋 Cómo clonar y correr el proyecto localmente

1. Clona el repositorio:

```bash
git clone https://github.com/tu-usuario/nail-salon.git
cd nail-salon
```

2. Si vas a usar una base de datos local, recuerda comentar Atlas y descomentar conexión local en el archivo de configuración.

3. Levanta los servicios usando Docker:

```bash
docker-compose up --build
```

4. Accede a Swagger para interactuar con la API en `http://localhost:8080/swagger-ui/index.html`

---
