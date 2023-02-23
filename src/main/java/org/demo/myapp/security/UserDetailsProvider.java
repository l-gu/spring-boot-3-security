package org.demo.myapp.security;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public interface UserDetailsProvider {
	
	UserDetails getAuthenticatedUserDetails(HttpServletRequest request);

}
