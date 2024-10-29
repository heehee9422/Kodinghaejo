package com.kodinghaejo;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class HikariCPConfig {
	//HikariConfig, HikariDataSource --> 스프링빈으로 만들어서 Controller 등에서 사용이 가능하도록 만듦.

	@Bean
	//spring.datasource.hikari라는 이름으로 application.properties 내에서 HikariCP의 설정이 가능
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}

}