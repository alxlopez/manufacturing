package com.mes.config.processEngine;

import java.io.IOException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.flowable.spring.SpringAsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration

@PropertySource({ "classpath:persistence.properties" })
public class BaseProcessEngineConfiguration extends AbstractProcessEngineAutoConfiguration {
	@Autowired
	private Environment env;

	@Bean	
	public DataSource activitiDataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(env.getProperty("activiti.jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("activiti.jdbc.url"));
		dataSource.setUsername(env.getProperty("activiti.jdbc.user"));
		dataSource.setPassword(env.getProperty("activiti.jdbc.user.pass"));
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

	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(
			PlatformTransactionManager transactionManager,
			SpringAsyncExecutor springAsyncExecutor
	) throws IOException {
		return baseSpringProcessEngineConfiguration(activitiDataSource(), transactionManager, springAsyncExecutor);
	}
}