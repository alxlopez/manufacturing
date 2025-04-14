package com.mes.config.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/*
 * Class to charge in memory the jwt.propertie values. This will be used each new token request arrive
 * to validate the it.
 */
@Component
@PropertySource("classpath:jwt.properties")
public class JWTValidationParameters {
	private final String url_keys;
	private final String clock_skew_seconds;
	private final String expected_issuer;
	private final String expected_audience;

    @Autowired
    public JWTValidationParameters(
    		@Value("${jwt.url_keys}") String url_keys,
    		@Value("${jwt.clock_skew_seconds}") String clock_skew_seconds,
    		@Value("${jwt.expected_issuer}") String expected_issuer,
    		@Value("${jwt.expected_audience}") String expected_audience
    ) {
        this.url_keys = url_keys;
        this.clock_skew_seconds = clock_skew_seconds;
        this.expected_issuer = expected_issuer;
        this.expected_audience = expected_audience;
    }

	public String getUrlKeys() {
		return url_keys;
	}

	public String getClockSkewSeconds() {
		return clock_skew_seconds;
	}

	public String getExpectedIssuer() {
		return expected_issuer;
	}

	public String getExpectedAudience() {
		return expected_audience;
	}
}
