package com.ggresillion.spring.mediamanager.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(value = {"com.ggresillion.spring.mediamanager.*"})
@EnableJpaRepositories("com.ggresillion.spring.mediamanager.*")
@EntityScan( basePackages = {"com.ggresillion.spring.mediamanager.*"} )
@ConfigurationProperties(prefix = "media")
public class MediaManagerConfiguration {

    private Map<String, Integer> sizes = new HashMap<>();

    public Map<String, Integer> getSizes() {
        return sizes;
    }
}
