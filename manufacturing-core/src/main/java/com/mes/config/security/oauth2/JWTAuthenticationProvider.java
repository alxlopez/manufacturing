package com.mes.config.security.oauth2;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/*
 * Class to provide an authentication method, to verify an authentication request,
 * and return the valid Authentication, or throw an exception if its no valid.
 * The supports method allows us to only receive JWTAuthInformation requests.
 */
@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		authentication.setAuthenticated(true);
		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(JWTAuthInformation.class);
	}
}
