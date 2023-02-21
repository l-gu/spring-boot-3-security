package org.demo.myapp.httpclient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.junit.jupiter.api.Test;

class HttpClientIT {
	
	private static final int OK_200 = 200 ;
	private static final int FORBIDDEN_403 = 403 ;
	
	private static final String ROOT_URI = "http://localhost:8080" ;

	private static final HttpClient httpClient = HttpClient.newBuilder()
		      .version(Version.HTTP_2)
		      .followRedirects(Redirect.NEVER) // NEVER, NORMAL, ALWAYS
		      .build();
	
	private int httpGet(String uriPart) {
		
		String uri = ROOT_URI + uriPart ;
		HttpRequest request;
		try {
			request = HttpRequest.newBuilder()
				     .uri(new URI(uri))
				     .GET()
				     //.header(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
				     .timeout(Duration.ofSeconds(2))
				     .build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("HTTP request : " + request.toString() );
		HttpResponse<String> response;
		try {
			response = httpClient.send(request, BodyHandlers.ofString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("-> status code : " + response.statusCode() );
		return response.statusCode();
	}

	@Test
	void testGetDoc() {
		assertEquals(OK_200, httpGet("/doc/page1"));
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
	
}
