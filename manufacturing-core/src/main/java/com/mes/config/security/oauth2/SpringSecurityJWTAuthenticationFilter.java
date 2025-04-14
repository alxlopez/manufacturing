package com.mes.config.security.oauth2;

import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Class that is activated each new request. This contain methods to validate the token signature
 * The signature is varying each period of time from Azure because OpenId implementation.
 * For this reason is necessary ask for the signature key each new request arrive.
 */
public class SpringSecurityJWTAuthenticationFilter extends GenericFilterBean {
    protected AuthenticationManager authenticationManager;
    protected String url_keys;
    protected String clock_skew_seconds;
    protected String expected_issuer;
    protected String expected_audience;
    
    public SpringSecurityJWTAuthenticationFilter(
    		AuthenticationManager  tAuthenticationManager,
    		String url_keys,
    		String clock_skew_seconds,
    		String expected_issuer,
    		String expected_audience
    ) {
        this.authenticationManager = tAuthenticationManager;
        this.url_keys = url_keys;
        this.clock_skew_seconds = clock_skew_seconds;
		this.expected_issuer = expected_issuer;
		this.expected_audience = expected_audience;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String authHeader = httpRequest.getHeader("Authorization");
        String[] authInfo = null;
        if (null != authHeader) {
            authInfo = authHeader.split(" ");
        }
        if (null != authInfo && authInfo.length == 2 && authInfo[0].toUpperCase().startsWith("BEARER")) {
        	String jwt = authInfo[1]; 
        	HttpsJwks httpsJkws = new HttpsJwks(this.url_keys);
            HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setRequireExpirationTime() // the JWT must have an expiration time
            .setAllowedClockSkewInSeconds(Integer.parseInt(this.clock_skew_seconds)) // allow some leeway in validating time based claims to account for clock skew
            .setRequireSubject() // the JWT must have a subject claim
            .setExpectedIssuer(this.expected_issuer) // whom the JWT needs to have been issued by
            .setExpectedAudience(this.expected_audience) // to whom the JWT is intended for
            .setVerificationKeyResolver(httpsJwksKeyResolver)
            .build();
            try {
            	JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            	JWTAuthInformation token = new JWTAuthInformation(jwtClaims);
                Authentication auth = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                httpResponse.setHeader("X-AuthToken", authInfo[1]);
            } catch (Exception ex) {
            	ex.printStackTrace();
                SecurityContextHolder.getContext().setAuthentication(null);
            }
            chain.doFilter(request, response);
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            chain.doFilter(request, response);
        }
    }
}
