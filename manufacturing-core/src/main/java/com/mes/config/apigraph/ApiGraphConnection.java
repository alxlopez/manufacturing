package com.mes.config.apigraph;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Component;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;

/*
 * Class to manage ApiGraph Connection and Tokens for it
 */
@Configuration
@PropertySource("classpath:apiGraph.properties")
public class ApiGraphConnection {
	private final String clientId;
	private final String clientSecret;
	private final String tenant;
	private final String authorityUrl;
	private final String apiGraphUrl;
	private final String apiVersion;
	private final String applicationOid;
	private AuthenticationContext authContext;
	private AuthenticationResult result;

	@Autowired
	public ApiGraphConnection(
			@Value("${client_id}") String clientId,
			@Value("${client_secret}") String clientSecret,
			@Value("${tenant}") String tenant,
			@Value("${authority_url}") String authorityUrl,
			@Value("${api_graph_url}") String apiGraphUrl,
			@Value("${api_version}") String apiVersion,
			@Value("${application_oid}") String applicationOid
	) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.tenant = tenant;
		this.authorityUrl = authorityUrl;
		this.apiGraphUrl = apiGraphUrl;
		this.apiVersion = apiVersion;
		this.applicationOid = applicationOid;
		this.newApiConnection();
	}

	/*
	 * New ApiGraph connection request. Get new access token.
	 */
	private void newApiConnection(){
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	String authority = this.authorityUrl.concat("/");
	    try {
	    	this.authContext = new AuthenticationContext(authority + tenant, true, executor);
			Future<AuthenticationResult> future = this.authContext.acquireToken(
				    this.apiGraphUrl,
				    new ClientCredential(clientId, clientSecret),
				    null
			);
			this.result = future.get();
	    } catch (Exception e) {
	    	e.printStackTrace();
        }
	}
	
	public String getAccessToken(){
		//Current date
		Date date = new Date();
		//If current date is before token expiration date, return current access token
		if(date.before(this.result.getExpiresOnDate())){
			System.out.println("The current token is valid yet");
			return this.result.getAccessToken();
		} else {
			//If token has expired, get new access token and return it
			newApiConnection();
			return this.result.getAccessToken();
		}
	}
	
	public String getAccessTokenType(){
		return this.result.getAccessTokenType();
	}
	
	public String getClientId(){
		return this.clientId;
	}
	
	public String getClientSecret(){
		return this.clientSecret;
	}
	
	public String getTenant(){
		return this.tenant;
	}
	
	public String getAuthorityUrl(){
		return this.authorityUrl;
	}
	
	public String getApiGraphUrl(){
		return this.apiGraphUrl;
	}
	
	public String getApiVersion(){
		return this.apiVersion;
	}
	
	public String getApplicationOid(){
		return this.applicationOid;
	}
}
