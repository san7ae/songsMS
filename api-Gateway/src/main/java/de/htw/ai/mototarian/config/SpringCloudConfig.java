package de.htw.ai.mototarian.config;

import org.apache.http.Header;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/songsWS-momotarian/rest/songs/**")
                        .uri("lb://SONGS-SERVICE")
                        .id("songsModule"))

                .route(r -> r.path("/songsWS-momotarian/rest/auth/**")
                        .uri("lb://AUTH-SERVICE")
                        .id("authModule"))
                .route(r -> r.path("/songsWS-momotarian/rest/new/**")
                        .uri("lb://NEW-SERVICE")
                        .id("authModule"))
                .build();
    }

}
