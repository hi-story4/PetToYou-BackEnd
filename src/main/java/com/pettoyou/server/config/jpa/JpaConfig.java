package com.pettoyou.server.config.jpa;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.pettoyou.server", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.pettoyou.server.domains.reservation.repository.mongo.*"))
public class JpaConfig {
}
