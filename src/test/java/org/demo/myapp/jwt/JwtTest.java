package org.demo.myapp.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;

class JwtTest {
	
	public static final long JWT_TOKEN_VALIDITY = 30 * 1000 ; // 30 secondes
	
	@Test
	void test1() {
		String subject = "foo";
		Map<String, Object> claims = new HashMap<>();
		String secretKey = "my-secret-key";
		
//		String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

		DatatypeConverter dc = null;
		
		String jwtString = Jwts.builder()
		   .setClaims(claims)
		   .setSubject(subject)
		   .setIssuedAt(new Date(System.currentTimeMillis()))
		   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
		   .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
		   .compact();		
		System.out.println(jwtString);
	}
}
