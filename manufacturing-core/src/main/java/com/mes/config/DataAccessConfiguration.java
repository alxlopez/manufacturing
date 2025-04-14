package com.mes.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource({ "classpath:persistence.properties" })
public class DataAccessConfiguration {
	@Autowired
    private Environment env;
	
	@Bean
	@Primary
	public DataSource database() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));
		dataSource.setInitialSize( Integer.parseInt(env.getProperty("tomcat.pool.initial-size")));
		dataSource.setMaxActive(Integer.parseInt(env.getProperty("tomcat.pool.max-active")));
		dataSource.setMaxIdle(Integer.parseInt(env.getProperty("tomcat.pool.max-idle")));
		dataSource.setMinIdle(Integer.parseInt(env.getProperty("tomcat.pool.min-idle")));
		dataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(env.getProperty("tomcat.pool.time-between-eviction-runs-millis")));
		dataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(env.getProperty("tomcat.pool.min-evictable-idle-time-millis")));
		dataSource.setValidationQuery(env.getProperty("tomcat.pool.validation-query"));
		dataSource.setValidationInterval(Integer.parseInt(env.getProperty("tomcat.pool.validation-interval")));
		dataSource.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("tomcat.pool.test-on-borrow")));
		dataSource.setRemoveAbandoned(Boolean.parseBoolean(env.getProperty("tomcat.pool.remove-abandoned")));
		dataSource.setRemoveAbandonedTimeout(Integer.parseInt(env.getProperty("tomcat.pool.remove-abandoned-timeout")));
		return dataSource;
	}
}
