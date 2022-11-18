package br.com.newtonpaiva.fastpass.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Value("${api.base.url}")
    private String apiUrl;

    public String getApiUrl() {
        return apiUrl;
    }
}
