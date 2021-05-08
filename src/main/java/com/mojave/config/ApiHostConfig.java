package com.mojave.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.stream.Stream;

/**
 * Configuration bean to determine URL environments from property files.
 */
@Configuration
public class ApiHostConfig {

    @Value("${api.host.client}")
    public String client;

    public String[] getAllowedCorsOrigins() {
        return Stream.of(client).toArray(String[]::new);
    }

    public String[] getAllowedCorsMethods() {
        return Stream
                .of(
                        HttpMethod.PATCH,
                        HttpMethod.GET,
                        HttpMethod.POST,
                        HttpMethod.DELETE,
                        HttpMethod.PUT
                )
                .map(HttpMethod::name)
                .toArray(String[]::new);
    }
}
