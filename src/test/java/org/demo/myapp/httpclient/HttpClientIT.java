package org.demo.myapp.httpclient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.demo.myapp.security.Claim;
import org.demo.myapp.security.jwt.JwsAlgorithm;
import org.demo.myapp.security.jwt.JwtManager;
import org.junit.jupiter.api.Test;

class HttpClientIT {
	
	private static final int OK_200 = 200 ;
	private static final int FORBIDDEN_403 = 403 ;
	
	private static final String ROOT_URI = "http://localhost:8080" ;
	private static final String SHARED_KEY_STRING = "abcdefgABCDEFG";
			
	private static final HttpClient httpClient = HttpClient.newBuilder()
		      .version(Version.HTTP_2)
		      .followRedirects(Redirect.NEVER) // NEVER, NORMAL, ALWAYS
		      .build();
	

	private HttpRequest buildGetRequest(String uriPart, String jwt) {
		String uri = ROOT_URI + uriPart ;
		HttpRequest request;
		try {
			Builder b = HttpRequest.newBuilder()
				     .uri(new URI(uri))
				     .GET()
				     //.header(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
				     .timeout(Duration.ofSeconds(2));
			if ( jwt != null ) {
				// Authorization: Bearer {token}
				b.header("Authorization", "Bearer " + jwt);
			}
			request = b.build();
			System.out.println("HTTP request : " + request.toString() );
			return request;
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Map<String, Object> buildClaims(String user, String roles)  {
		Map<String, Object> claims = new HashMap<>();
		claims.put(Claim.USER, user);
		claims.put(Claim.ROLES, roles);
		return claims;
	}
	
	private String buildJWT()  {
		return buildJWT("Bob", "");
	}
	private String buildJWT(String user, String roles)  {
		Map<String, Object> claims = buildClaims(user, roles);
		JwtManager jwtManager = new JwtManager("test-issuer", JwsAlgorithm.HS256, SHARED_KEY_STRING );
		String jwt = jwtManager.buildJWT(claims);
		System.out.println("JWT : " + jwt );
		return jwt;
	}
	
	private int httpGet(String uriPart) {
		return httpGet(uriPart, null); 
	}
	private int httpGet(String uriPart, String jwt) {
		HttpRequest request = buildGetRequest(uriPart, jwt);
		HttpResponse<String> response;
		try {
			response = httpClient.send(request, BodyHandlers.ofString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("-> status code : " + response.statusCode() );
		System.out.println("---" );
		return response.statusCode();
	}

	@Test
	void testGetDoc() {
		assertEquals(OK_200, httpGet("/doc/page1"));
	}
	
	@Test
	void testGetDocWithJWT() {
		assertEquals(OK_200,  httpGet("/doc/page1", buildJWT()) );
	}
	
	@Test
	void testGetMetrics() {
		assertEquals(OK_200, httpGet("/metrics/aaa") );
	}
	
	@Test
	void testGetInex() {
		assertEquals(FORBIDDEN_403, httpGet("/foo/bar"));
	}
	
	@Test
	void testGetApiHello() {
		assertEquals(FORBIDDEN_403, httpGet("/api/hello") );
	}
	
	@Test
	void testGetApiHelloWithJWT() {
		assertEquals(FORBIDDEN_403, httpGet("/api/hello", buildJWT("Bart","ZZZ/YYYY")) );
	}
	
	@Test
	void testGetApiHelloOkWithRoleAdmin() {
		assertEquals(OK_200, httpGet("/api/hello/admin", buildJWT("Bart","ADMIN")) );
		assertEquals(OK_200, httpGet("/api/hello/admin", buildJWT("Bart","FOO/ADMIN/XXX/ZZZ")) );
	}
	
	@Test
	void testGetApiHelloOkWithRoleManager() {
		assertEquals(OK_200, httpGet("/api/hello/manager", buildJWT("Bart","MANAGER")) );
		assertEquals(OK_200, httpGet("/api/hello/manager", buildJWT("Bart","STANDARD/MANAGER")) );
		assertEquals(OK_200, httpGet("/api/hello/manager", buildJWT("Bart","MANAGER/STANDARD/FOO")) );
	}
	
}
