package org.demo.myapp.application.security;

import java.util.Arrays;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public class UserDetailsProviderFakeImpl extends UserDetailsProvider {
	
	@Override
	public UserDetails getAuthenticatedUserDetails(HttpServletRequest request) {
		String[] roles = request.getParameterValues("role");
		if ( roles != null ) {
			// user authenticated 
			return buildUserDetails("bob", null, Arrays.asList(roles));
		}
		else {
			// not authenticated 
			return null; 
		}
	}
	
}
