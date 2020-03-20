package com.nexware.aajapan.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.nexware.aajapan.services.impl.SpringSecurityAuditorAware;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class DataAccessConfiguration {
	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}

}
