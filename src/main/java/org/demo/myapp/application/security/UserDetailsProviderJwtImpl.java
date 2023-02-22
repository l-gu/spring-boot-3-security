package org.demo.myapp.application.security;

import org.demo.myapp.application.security.jwt.JwsAlgorithm;
import org.demo.myapp.application.security.jwt.JwtException;
import org.demo.myapp.application.security.jwt.JwtManager;
import org.demo.myapp.application.security.jwt.JwtToken;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public class UserDetailsProviderJwtImpl extends UserDetailsProvider {
	
//	private final String secretKey;
	private final JwtManager jwtManager;
	
	public UserDetailsProviderJwtImpl(String secretKey) {
		super();
//		this.secretKey = secretKey;
		this.jwtManager= new JwtManager("issuer", JwsAlgorithm.HS512, secretKey);
	}

	@Override
	public UserDetails getAuthenticatedUserDetails(HttpServletRequest request) {
		
		String bearerToken = getAuthorizationBearerToken(request);
		if ( bearerToken != null ) {
			return buildUserDetails(bearerToken);
		}
		return null;
	}

	/**
	 * Build UserDetails from the given JWT token signed with the given secret key 
	 * @param jwtToken 
	 * @param secretKey the secret key (or null if not signed)
	 * @return
	 */
	private UserDetails buildUserDetails(String jwtToken) {
		try {
			// Parse JWT token
			JwtToken jwt = jwtManager.parseJWT(jwtToken);

			// Get specific claims from JWT body
			String user  = jwt.get("user");  // get specific claim "user"
			String roles = jwt.get("roles"); // get specific claim "roles"
			
			return buildUserDetails(user, null, splitRoles(roles));
		} catch (JwtException e) {
			return null;
		}
	}
	
//	/**
//	 * Parse the given JWT 
//	 * @param jwtToken
//	 * @param secretKey
//	 * @return
//	 * @throws JwtException
//	 */
//	private Jws<Claims> parseClaims(String jwtToken, String secretKey) throws JwtException {
//		JwtParser jwtParser = Jwts.parser();
//		if ( secretKey != null ) {
//			// Algo in JWT header 
//			jwtParser.setSigningKey(secretKey);
//		}
//		try {
//			return jwtParser.parseClaimsJws(jwtToken);
//		} catch (RuntimeException  e) {
//			throw new JwtException("Cannot parse JWT", e);
//		}
//	}

	
}
