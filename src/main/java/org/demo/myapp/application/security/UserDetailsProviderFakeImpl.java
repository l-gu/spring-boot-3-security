package org.demo.myapp.application.security;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

public class UserDetailsProviderFakeImpl extends UserDetailsProvider {
	
	@Override
	public UserDetails getAuthenticatedUserDetails(HttpServletRequest request) {
		String[] roles = request.getParameterValues("role");
		List<String> list = roles != null ? Arrays.asList(roles) : new LinkedList<>() ;
		return buildUserDetails("bob", null, list);
	}
	
}
