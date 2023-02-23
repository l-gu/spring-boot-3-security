package org.demo.myapp.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.demo.myapp.security.jwt.JwsAlgorithm;
import org.demo.myapp.security.jwt.JwtException;
import org.demo.myapp.security.jwt.JwtManager;
import org.demo.myapp.security.jwt.JwtToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public class UserDetailsProviderJwt implements UserDetailsProvider {
	
	private final JwtManager jwtManager;
	
	/**
	 * Constructor with shared key
	 * @param secretKey
	 */
	public UserDetailsProviderJwt(String secretKey) {
		super();
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

	private String getAuthorizationBearerToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if ( authorizationHeader != null && authorizationHeader.startsWith("Bearer") ) {
			return authorizationHeader.split(" ")[1].trim();
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
			String user  = jwt.get(Claim.USER);  // get specific claim "user"
			String roles = jwt.get(Claim.ROLES); // get specific claim "roles"
			
			return buildUserDetails(user, null, splitRoles(roles));
		} catch (JwtException e) {
			return null;
		}
	}

	private List<String> splitRoles(String s) {
		List<String> roles = new LinkedList<>();
		if ( s != null ) {
			for ( String r : s.split("/") ) {
				roles.add(r.trim());
			}
		}
		return roles;
	}
	
	/**
	 * Builds UserDetails implementation with the given name, password and roles
	 * @param userName user name 
	 * @param password user password (can be null if useless)  
	 * @param roles    list of roles (can be null if no role)
	 * @return
	 */
	private UserDetails buildUserDetails(String userName, String password, List<String> roles) {
		if ( roles != null ) {
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
			for ( String role : roles ) {
				if ( role.length() > 0 ) {
					// create SimpleGrantedAuthority with "ROLE_" prefix
					authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
				}
			}
			return new UserDetailsImpl(userName, password, authorities );
		}
		else {
			return null;
		}
	}

}
