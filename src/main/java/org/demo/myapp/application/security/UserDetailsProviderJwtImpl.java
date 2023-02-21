package org.demo.myapp.application.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

public class UserDetailsProviderJwtImpl extends UserDetailsProvider {
	
	private final String secretKey;
	
	public UserDetailsProviderJwtImpl(String secretKey) {
		super();
		this.secretKey = secretKey;
	}

	@Override
	public UserDetails getAuthenticatedUserDetails(HttpServletRequest request) {
		
		String bearerToken = getAuthorizationBearerToken(request);
		if ( bearerToken != null ) {
			return buildUserDetails(bearerToken, secretKey);
		}
		return null;
	}

	/**
	 * Build UserDetails from the given JWT token signed with the given secret key 
	 * @param jwtToken 
	 * @param secretKey the secret key (or null if not signed)
	 * @return
	 */
	private UserDetails buildUserDetails(String jwtToken, String secretKey) {
		try {
			Jws<Claims> claims = parseClaims(jwtToken, secretKey);
			Claims body = claims.getBody();
			return buildUserDetails(body);
		} catch (JwtException e) {
			return null;
		}
	}
	
	/**
	 * Parse the given JWT 
	 * @param jwtToken
	 * @param secretKey
	 * @return
	 * @throws JwtException
	 */
	private Jws<Claims> parseClaims(String jwtToken, String secretKey) throws JwtException {
		JwtParser jwtParser = Jwts.parser();
		if ( secretKey != null ) {
			// Algo in JWT header 
			jwtParser.setSigningKey(secretKey);
		}
		try {
			return jwtParser.parseClaimsJws(jwtToken);
		} catch (RuntimeException  e) {
			throw new JwtException("Cannot parse JWT", e);
		}
	}
	
	private UserDetails buildUserDetails(Claims body) {
		// Standard JWT claims :  
		String subject   = body.getSubject();    // get standard claim "sub", same as body.get(Claims.SUBJECT)
		String audience  = body.getAudience();   // get standard claim "aud", same as body.get(Claims.AUDIENCE)
		Date   expir     = body.getExpiration(); // get standard claim "exp", same as body.get(Claims.EXPIRATION)
		String issuer    = body.getIssuer();     // get standard claim "iss", same as body.get(Claims.ISSUER)
		Date   issuedAt  = body.getIssuedAt();   // get standard claim "iat"
		Date   notBefore = body.getNotBefore();  // get standard claim "nbf"
		String id        = body.getId();         // get standard claim "jti"

		// Specific claims :
		String user  = (String) body.get("user");  // get specific claim "user"
		String roles = (String) body.get("roles"); // get specific claim "roles"
		
		return buildUserDetails(user, null, splitRoles(roles));
	}
	
}
