package com.mes.config.security;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.mes.config.security.oauth2.JWTAuthenticationProvider;
import com.mes.config.security.oauth2.JWTValidationParameters;
import com.mes.config.security.oauth2.SpringSecurityJWTAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1) //this is to override the embbeded activiti security configuration.
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private JWTValidationParameters jwtValidationParameters;
	@Autowired
	private JWTAuthenticationProvider authenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated().antMatchers("/milanes/integration/**").permitAll()
		.anyRequest().authenticated().antMatchers("/milanes/upload/**").permitAll().anyRequest().authenticated()
		.antMatchers("/milanes/portal/client/**").permitAll().anyRequest().authenticated()
		.and().csrf().disable();
		http.addFilterBefore(
				new SpringSecurityJWTAuthenticationFilter(
						super.authenticationManagerBean(),
						jwtValidationParameters.getUrlKeys(), jwtValidationParameters.getClockSkewSeconds(),
						jwtValidationParameters.getExpectedIssuer(), jwtValidationParameters.getExpectedAudience()
				), BasicAuthenticationFilter.class
		);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/milanes/integration/**").antMatchers("/milanes/upload/**").antMatchers("/milanes/portal/client/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.authenticationProvider(authenticationProvider);
	}
}
