package org.demo.myapp.application.security;

public class JwtException extends Exception {

	private static final long serialVersionUID = 1L;

	public JwtException(String message, Throwable cause) {
		super(message, cause);
	}
}
