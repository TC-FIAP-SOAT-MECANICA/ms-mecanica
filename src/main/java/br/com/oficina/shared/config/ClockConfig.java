package br.com.oficina.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Relógio injetável. Os casos de uso pedem a hora ao {@link Clock} em vez de chamar {@code Instant.now()},
 * o que permite fixar a data nos testes.
 */
@Configuration
class ClockConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
