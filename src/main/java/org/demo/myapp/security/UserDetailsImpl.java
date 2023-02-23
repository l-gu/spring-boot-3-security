package org.demo.myapp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final String userName ;
	private final String password ;
	private final Collection<? extends GrantedAuthority> authorities ;
	
	public UserDetailsImpl(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.userName = userName;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// Indicates whether the user's account has expired. An expired account cannot be authenticated.
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Indicates whether the user's credentials (password) has expired. Expired credentials prevent authentication.
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
		return true;
	}

}
