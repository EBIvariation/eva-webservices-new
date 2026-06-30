package uk.ac.ebi.eva.ws.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableJpaRepositories(basePackages = "uk.ac.ebi.eva.ws.repository.jpa")
@EntityScan(basePackages = "uk.ac.ebi.eva.ws.entity.jpa")
public class JpaConfig {
}