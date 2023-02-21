package org.demo.main;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.demo.myapp.application.security.JwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Main1 {

	public static final long JWT_TOKEN_VALIDITY = 30 * 1000 ; // 30 secondes
	
	public static void main(String[] args) throws JwtException {
		String subject = "foo";
		Map<String, Object> inputClaims = new HashMap<>();
		inputClaims.put("user", "Bob Marley");
		String secretKey = "my-secret-key";		
		String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

		// Same result with Base64 or Bytes 
		String jwt = buildJWT(subject, inputClaims, base64EncodedSecretKey) ;
		System.out.println( jwt );
		System.out.println( buildJWT(subject, inputClaims, secretKey.getBytes()) );
		
		
		Jws<Claims> claims = parseClaims(jwt, secretKey.getBytes()) ;
		System.out.println( "subject : " + claims.getBody().getSubject()  );
		System.out.println( "user : " + claims.getBody().get("user")  );
	}
	
	public static String buildJWT(String subject, Map<String, Object> claims, String base64EncodedSecretKey ) {
		return Jwts.builder()
				   .setClaims(claims)
				   .setSubject(subject)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				   .signWith(SignatureAlgorithm.HS512, base64EncodedSecretKey)
				   .compact();
	}

	public static String buildJWT(String subject, Map<String, Object> claims, byte[] secretKeyBytes ) {
		return Jwts.builder()
				   .setClaims(claims)
				   .setSubject(subject)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				   .signWith(SignatureAlgorithm.HS512, secretKeyBytes)
				   .compact();
	}

	private static Jws<Claims> parseClaims(String jwtToken, byte[] secretKeyBytes) throws JwtException {
		JwtParser jwtParser = Jwts.parser();
		if ( secretKeyBytes != null ) {
			// Algo in JWT header 
			jwtParser.setSigningKey(secretKeyBytes);
		}
		try {
			return jwtParser.parseClaimsJws(jwtToken);
		} catch (RuntimeException  e) {
			throw new JwtException("Cannot parse JWT", e);
		}
	}

}
