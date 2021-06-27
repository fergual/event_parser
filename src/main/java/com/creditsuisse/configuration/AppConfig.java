package com.creditsuisse.configuration;

import com.creditsuisse.persistance.dao.DaoManager;
import com.creditsuisse.persistance.model.CompletedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(destroyMethod = "destroy")
    public DaoManager<CompletedEvent> getDaoManagerCompletedEvent() {
        return new DaoManager<>(CompletedEvent.class);
    }
}
