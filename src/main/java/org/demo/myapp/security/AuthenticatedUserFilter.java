package org.demo.myapp.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter implementation for user authentication with 0..N roles (= 0..N granted authorities)
 * 
 * @author Laurent Guerin
 *
 */
@Component
public class AuthenticatedUserFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(AuthenticatedUserFilter.class);
	
	private final UserDetailsProvider userDetailsProvider ; 
	
	public AuthenticatedUserFilter(@Value("${app.jwt.secretKey}") String jwtSecretKey) {
		super();
		log.info("[SECURITY] AuthenticatedUserFilter constructor, jwtSecretKey : '{}' ", jwtSecretKey);
		
		// UserDetailsProvider implementation based on JWT
		userDetailsProvider = new UserDetailsProviderJwt(jwtSecretKey);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		log.info("[SECURITY] AuthenticatedUserFilter : doFilterInternal : {}", request.getRequestURI() );
		UserDetails userDetails = userDetailsProvider.getAuthenticatedUserDetails(request);
		log(userDetails);
		if ( userDetails != null ) {
			setAuthenticationInSecurityContext(userDetails);
		}
		
		filterChain.doFilter(request, response);
	}

	/**
	 * Populate Spring SecurityContext according the given UserDetails <br>
	 * After calling this method the SecurityContext can be <br>
	 * - null if no user is authenticated <br>
	 * - not null if a user is authenticated (with 0 to N roles) <br> 
	 * 
	 * @param userDetails
	 */
	private void setAuthenticationInSecurityContext(UserDetails userDetails) {
		if ( userDetails != null ) {
			//--- User is authenticated
			log.info("[SECURITY] AuthenticatedUserFilter : AUTHENTICATED -> Init SecurityContext");
			// Create "Authentication" with "UsernamePasswordAuthenticationToken" implementation
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			// Set "Authentication" in "SecurityContext" for the current thread
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		else {
			//--- User is NOT authenticated
			log.info("[SECURITY] AuthenticatedUserFilter : NOT AUTHENTICATED -> Clear SecurityContext");
			// Remove "Authentication" in "SecurityContext" for the current thread
			SecurityContextHolder.clearContext();
		}
	}

	private void log(UserDetails userDetails) {
		if ( userDetails != null ) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			int n = 0 ;
			for ( GrantedAuthority a : userDetails.getAuthorities() ) {
				if ( n > 0 ) sb.append(",");
				sb.append(a.getAuthority());
				n++;
			}
			sb.append("]");
			String roles = sb.toString();
			log.info("[SECURITY] AuthenticatedUserFilter : UserDetails: '{}' role(s): {}", userDetails.getUsername(), roles);
		}
		else {
			log.info("[SECURITY] AuthenticatedUserFilter : no UserDetails (no user authenticated)");
		}
	}
}
