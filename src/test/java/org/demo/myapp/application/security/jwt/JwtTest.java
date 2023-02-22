package org.demo.myapp.application.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class JwtTest {
	
	private static final String SECRET_KEY_STRING = "my-secret-key";
	
	private JwtToken buildAndParse(JwtManager jwtManager, Map<String, Object> claims) throws JwtException {
		String jwt = jwtManager.buildJWT(claims);
		
		System.out.println("JWT : " + jwt );
		
		JwtToken jwtToken = jwtManager.parseJWT(jwt);
		assertEquals("myTest", jwtToken.getIssuer() );
		assertNotNull(jwtToken.getIssuedAt());
		assertNotNull(jwtToken.getSignature());
//		// other body claims are null
//		assertNull(jwtToken.getAudience());
//		assertNull(jwtToken.getId());
		return jwtToken ;
	}
	
	@Test 
	void testHS256() throws JwtException {
		JwtToken jwtToken = buildAndParse( new JwtManager("myTest", JwsAlgorithm.HS256, SECRET_KEY_STRING ), null );
//		assertEquals("mySubject", jwtToken.getSubject() );
		assertNull(jwtToken.getAudience());
		assertNull(jwtToken.getAudience());
		assertNull(jwtToken.getId());
	}	
	@Test 
	void testHS256withClaims() throws JwtException {
		Map<String, Object> claims = new HashMap<>();
		claims.put("jti", "007");
		claims.put("user", "Bob Marley");
		JwtToken jwtToken = buildAndParse( new JwtManager("myTest", JwsAlgorithm.HS256, SECRET_KEY_STRING ), claims );
		assertEquals("007", jwtToken.getId() );
		assertEquals("Bob Marley", jwtToken.get("user") );
	}
	@Test 
	void testHS384() throws JwtException {
		buildAndParse( new JwtManager("myTest", JwsAlgorithm.HS384, SECRET_KEY_STRING ), null );
	}
	@Test 
	void testHS512() throws JwtException {
		buildAndParse( new JwtManager("myTest", JwsAlgorithm.HS512, SECRET_KEY_STRING ), null );
	}

	@Test 
	void testRS256() throws JwtException {
		Key key = null; 
		// Exception : Unsigned Claims not supported
		buildAndParse( new JwtManager("myTest", JwsAlgorithm.RS256, key ), null );
	}	
	
	@Test 
	void testES512() throws JwtException {
		// Exception : Algorithm ES512 requires Key
		buildAndParse( new JwtManager("myTest", JwsAlgorithm.ES512, SECRET_KEY_STRING ), null );
	}
}
