package org.demo.myapp.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

public class JwtManager {

	private final String issuer;
	private final JwsAlgorithm jwsAlgorithm;
	private final byte[] secretKeyBytes;
	private final Key key ;
	
	private JwtManager(String issuer, JwsAlgorithm jwsAlgorithm, String secretKey, Key key) {
		this.issuer = issuer ;
		this.jwsAlgorithm = jwsAlgorithm != null ? jwsAlgorithm : null;
		this.secretKeyBytes = secretKey != null ? secretKey.getBytes() : null ;
		this.key = key;
	}

	public JwtManager(String issuer, JwsAlgorithm jwsAlgorithm, String secretKey) {
		this(issuer, jwsAlgorithm, secretKey, null);
		if ( jwsAlgorithm.requiresKey() ) {
			throw new IllegalArgumentException("Algorithm " + jwsAlgorithm.name() + " requires a Key (not a String)");
		}
	}

	public JwtManager(String issuer, JwsAlgorithm jwsAlgorithm, Key key) {
		this(issuer, jwsAlgorithm, null, key);
	}

	/**
	 * Parses the given JWT with the given secret key
	 * @param jwtToken  
	 * @return
	 * @throws JwtException
	 */
	public JwtToken parseJWT(String jwtToken) throws JwtException {
		JwtParser jwtParser = Jwts.parser();
		if ( jwsAlgorithm != null ) { 
			if ( jwsAlgorithm.requiresKey() ) {
				if ( key != null ) {
					jwtParser.setSigningKey(key);
				}
			}
			else {
				if ( secretKeyBytes != null ) {
					jwtParser.setSigningKey(secretKeyBytes);
				}
			}
		}
		else {
			// Unsigned JWT : "AAAA.BBBB." 
			// Parsing will throw an Exception , signature is mandatory for library jjwt 
		}
		// try to parser the JWT 
		try {
			Jws<Claims> jwsClaims = jwtParser.parseClaimsJws(jwtToken);
			return new JwtToken(jwsClaims);
		} catch (RuntimeException  e) {
			throw new JwtException("Cannot parse JWT", e);
		}
	}
	
	/**
	 * Builds a JWT token 
	 * @param subject
	 * @param claims all claims to add in the "body" if not yet present (including "Registered Claims" like "iss", "sub", etc)
	 * @return
	 */
	public String buildJWT(Map<String, Object> claims) {
		JwtBuilder jwtBuilder = Jwts.builder();
		// Initialize some claims
		jwtBuilder.setIssuer(issuer);
		jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis()));
		// Add other claims 
		if ( claims != null ) {
			// 'setClaims' replaces all claims => use 'addClaims' instead
			jwtBuilder.addClaims(claims); // "add" : add only if not yet exist
		}
		// Define signature algorithm and key
		if ( jwsAlgorithm != null ) { 
			if ( jwsAlgorithm.requiresKey() ) {
				if ( key != null ) {
					jwtBuilder.signWith(jwsAlgorithm.getSignatureAlgorithm(), key);
				}
			}
			else {
				if ( secretKeyBytes != null ) {
					jwtBuilder.signWith(jwsAlgorithm.getSignatureAlgorithm(), secretKeyBytes);
				}
			}
		}
		return jwtBuilder.compact();
	}

}
