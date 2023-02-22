package org.demo.myapp.application.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtTest {
	
	private static final String ISSUER = "the-big-issuer";
	private static final String SECRET_KEY_STRING = "my-secret-key";
	
	private JwtToken buildAndParse(JwtManager jwtManager, Map<String, Object> claims) throws JwtException {
		String jwt = jwtManager.buildJWT(claims);
		System.out.println("JWT : " + jwt );
		JwtToken jwtToken = jwtManager.parseJWT(jwt);
		// check default claims :
		assertEquals(ISSUER, jwtToken.getIssuer() );
		assertNotNull(jwtToken.getIssuedAt());
		assertNotNull(jwtToken.getSignature());
		
		return jwtToken ;
	}
	
	//-----------  HSxxx  algorithm
	@Test 
	void testHS256() throws JwtException {
		JwtToken jwtToken = buildAndParse( new JwtManager(ISSUER, JwsAlgorithm.HS256, SECRET_KEY_STRING ), null );
		// Claims not defined => null
		assertNull(jwtToken.getAudience());
		assertNull(jwtToken.getAudience());
		assertNull(jwtToken.getId());
	}	
	@Test 
	void testHS256withClaims() throws JwtException {
		Map<String, Object> claims = new HashMap<>();
		claims.put("jti", "007");
		claims.put("user", "Bob Marley");
		JwtToken jwtToken = buildAndParse( new JwtManager(ISSUER, JwsAlgorithm.HS256, SECRET_KEY_STRING ), claims );
		assertEquals("007", jwtToken.getId() );
		assertEquals("Bob Marley", jwtToken.get("user") );
	}
	@Test 
	void testHS384() throws JwtException {
		buildAndParse( new JwtManager(ISSUER, JwsAlgorithm.HS384, SECRET_KEY_STRING ), null );
	}
	@Test 
	void testHS512() throws JwtException {
		buildAndParse( new JwtManager(ISSUER, JwsAlgorithm.HS512, SECRET_KEY_STRING ), null );
	}

	//-----------  RSxxx  algorithm
	@Test 
	void testRS256() throws JwtException {
		Key key = null; 
		// Exception : Unsigned Claims not supported
		JwtException e = Assertions.assertThrows(JwtException.class, () -> {
			buildAndParse( new JwtManager(ISSUER, JwsAlgorithm.RS256, key ), null );
		}, "Exception expected");
		System.out.println("Exception : " + e.getClass().getCanonicalName() );
	}	
	
	//-----------  ESxxx  algorithm
	@Test 
	void testES512() throws JwtException {
		// Exception : Algorithm ES512 requires Key
		IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			buildAndParse( new JwtManager(ISSUER, JwsAlgorithm.ES512, SECRET_KEY_STRING ), null );
		}, "Exception expected");
		System.out.println("Exception : " + e.getClass().getCanonicalName() );
	}
}
