package com.kissansathi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables automatic population of created_at / updated_at via {@link org.springframework.data.annotation.CreatedDate}
 * and {@link org.springframework.data.annotation.LastModifiedDate}.
 * All timestamps are stored/handled in UTC (see application.yml: hibernate.jdbc.time_zone=UTC).
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}