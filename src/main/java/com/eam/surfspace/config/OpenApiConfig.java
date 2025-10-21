package com.eam.surfspace.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "SurfSpace API",
                version = "v1.0",
                description = "API documentation for SurfSpace application",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "SurfSpace Support",
                        email = "reina.lorena.2458@eam.edu.co",
                        url = "https://www.eam.edu.co"
        ),
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {  // Define los servidores donde está disponible tu API
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de Desarrollo"  // Este aparece en el dropdown de Swagger
                ),
                @Server(
                        url = "http://localhost:8090",
                        description = "Servidor de Pruebas"
                ),
                @Server(
                        url = "http://localhost:8099",
                        description = "Servidor de Producción"
                )
        }
)

public class OpenApiConfig {
    @Bean // Crea un objeto que Spring va a usar para configurar OpenAPI
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        // ESTO ES PARA AUTENTICACIÓN JWT (si la usas más adelante)
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)  // Tipo de autenticación HTTP
                                        .scheme("bearer")                // Esquema Bearer
                                        .bearerFormat("JWT")             // Formato del token
                                        .description("Ingresa tu token JWT")  // Descripción para el usuario
                        )
                );
    }

}
