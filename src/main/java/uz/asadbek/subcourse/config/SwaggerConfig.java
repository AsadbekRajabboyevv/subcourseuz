package uz.asadbek.subcourse.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiSpec() {
        return (new OpenAPI()).addSecurityItem((new SecurityRequirement()).addList("bearerAuth"))
            .components((new Components()).addSecuritySchemes("bearerAuth",
                (new SecurityScheme()).name("bearerAuth").type(
                    Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        // add error type to each operation
        return (operation, handlerMethod) -> {
            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                .description("Error")
                .content(new Content().addMediaType("*/*", new MediaType().schema(
                    new Schema<MediaType>().$ref("ApiErrorResponse")))));
            return operation;
        };
    }

}
