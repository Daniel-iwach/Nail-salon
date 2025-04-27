package com.chamisnails.nailsalon.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "ChamiNails API",
                description = "Este proyecto es una API REST para la gestión de usuarios y turnos en el salón de manicura **ChamiNails**. "
                        + "Incluye autenticación y autorización mediante JWT, control de acceso basado en roles y operaciones CRUD seguras.\n\n"
                        + "💅 **Características principales:**\n"
                        + "- **Spring Boot & Java**: Backend moderno y escalable.\n"
                        + "- **Spring Security & JWT**: Sistema seguro de autenticación y autorización.\n"
                        + "- **Control de acceso por roles (RBAC)**: Permisos diferenciados para clientes y administradores.\n"
                        + "- **MongoDB**: Almacenamiento de datos NoSQL para usuarios y turnos.\n"
                        + "- **Swagger**: Documentación interactiva para testear los endpoints.\n"
                        + "- **Pruebas unitarias con JUnit & Mockito**: Garantía de calidad y estabilidad del sistema.\n\n"
                        + "Esta API es parte del sistema web de ChamiNails, diseñada para ofrecer reservas de turnos, administración de usuarios "
                        + "y envío de notificaciones por correo.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Daniel Iwach",
                        url = "https://www.linkedin.com/in/daniel-iwach",
                        email = "daniel.g.iwach@gmail.com"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor de desarrollo",
                        url = "http://localhost:8080"
                )
        },
        security = @SecurityRequirement(name = "Security Token")
)
@SecurityScheme(
        name = "Security Token",
        description = "Token de acceso (JWT) para la API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
