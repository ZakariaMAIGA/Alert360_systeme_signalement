package com.alert360.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MediaWebConfig implements WebMvcConfigurer {

    @Value("${alert360.media.storage-dir:uploads/alert360}")
    private String dossierStockage;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String chemin = Paths.get(dossierStockage)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry.addResourceHandler("/api/medias/**")
                .addResourceLocations(chemin);
    }
}