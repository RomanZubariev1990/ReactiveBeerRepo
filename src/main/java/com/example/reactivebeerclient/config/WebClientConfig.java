package com.example.reactivebeerclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() throws SSLException {

        return WebClient.builder()
                .baseUrl("http://api.springframework.guru/")
                .build();
    }
}
