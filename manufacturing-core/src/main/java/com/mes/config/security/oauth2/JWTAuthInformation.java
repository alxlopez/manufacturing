package com.mes.config.security.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/*
 * Class to get and manage all JWT Token information.
 * This is the Authentication Object to plug into the Spring Security infrastructure
 */
public class JWTAuthInformation implements Authentication{
	private static final long serialVersionUID = 1L;
	private JwtClaims jwtClaims;
	private boolean authenticated = false;

	JWTAuthInformation(JwtClaims claims){
		this.jwtClaims = claims;
	}

	@Override
	public String getName() {
		try {
			return jwtClaims.getSubject();
		} catch (MalformedClaimException | NullPointerException e) {
			return null;
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		return Collections.unmodifiableList(roles);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		if (jwtClaims != null){
			return jwtClaims.toJson();
		}
		return null;
	}

	@Override
	public Object getPrincipal() {		
		return getName();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		authenticated = isAuthenticated;		
	}

	public String getUpn() throws MalformedClaimException {
		return this.jwtClaims.getStringClaimValue("upn");
	}
}
