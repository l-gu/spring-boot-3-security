package org.demo.myapp.application.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public abstract class UserDetailsProvider {
	
	public abstract UserDetails getAuthenticatedUserDetails(HttpServletRequest request);

	/**
	 * Builds UserDetails implementation with the given name, password and roles
	 * @param userName user name 
	 * @param password user password (can be null if useless)  
	 * @param roles    list of roles (can be null if no role)
	 * @return
	 */
	protected UserDetails buildUserDetails(String userName, String password, List<String> roles) {
		if ( roles != null ) {
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
			for ( String role : roles ) {
				// create SimpleGrantedAuthority with "ROLE_" prefix
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
			return new UserDetailsImpl(userName, password, authorities );
		}
		else {
			return null;
		}
	}
	
	protected String getAuthorizationBearerToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if ( authorizationHeader != null && authorizationHeader.startsWith("Bearer") ) {
			return authorizationHeader.split(" ")[1].trim();
		}
		return null;
	}
	
	protected List<String> splitRoles(String s) {
		List<String> roles = new LinkedList<>();
		if ( s != null ) {
			for ( String r : s.split(",") ) {
				roles.add(r.trim());
			}
		}
		return roles;
	}

}
