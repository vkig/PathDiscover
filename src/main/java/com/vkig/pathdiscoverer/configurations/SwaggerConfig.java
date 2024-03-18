package com.vkig.pathdiscoverer.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for swagger ui.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Generate GroupedOpenApi object to configure the shown endpoints on swagger ui
     * @return The built GroupedOpenApi object with the set group name and path matching
     */
    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }

    /**
     * Generate the OpenAPI object to configure the shown properties of the API on the swagger ui
     * @return The built OpenAPI object with the set Info properties
     */
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info().title("PathDiscoverer API").version("0.0.1"));
    }

}
