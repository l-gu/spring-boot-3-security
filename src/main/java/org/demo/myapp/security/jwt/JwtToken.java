package org.demo.myapp.security.jwt;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JwtToken {
	
	private final Jws<Claims> claims;

	protected JwtToken(Jws<Claims> claims) {
		super();
		this.claims = claims;
	}
	
	//---------------------------------------------------------------------
	// JWT HEADER
	//---------------------------------------------------------------------
	
	/**
	 * Returns the "alg" (algorithm) header value or null if not present.
	 * @return
	 */
	public String getAlgorithm() {
		return claims.getHeader().getAlgorithm();
	}
	
	/**
	 * Returns the "calg" (Compression Algorithm) header value or null if not present. 
	 * @return
	 */
	public String getCompressionAlgorithm() {
		return claims.getHeader().getCompressionAlgorithm();
	}
	
	/**
	 * Returns the "cty" (Content Type) header value or null if not present. 
	 * @return
	 */
	public String getContentType() {
		return claims.getHeader().getContentType();
	}

	/**
	 * Returns the "kid" (Key ID) header value or null if not present. 
	 * @return
	 */
	public String getKeyId() {
		return claims.getHeader().getKeyId();
	}

	/**
	 * Returns the "typ" (type) header value or null if not present.
	 * @return
	 */
	public String getType() {
		return claims.getHeader().getType();
	}

	//---------------------------------------------------------------------
	// JWT BODY
	//---------------------------------------------------------------------

	/**
	 * Returns the "jti" body value (Registered Claim Name) or null if not present. 
	 * @return
	 */
	public String getId() {
		return claims.getBody().getId();
	}

	/**
	 * Returns the "sub" body value (Registered Claim Name) or null if not present. 
	 * @return
	 */
	public String getSubject() {
		return claims.getBody().getSubject();
	}
	
	/**
	 * Returns the "aud" body value (Registered Claim Name) or null if not present. 
	 * @return
	 */
	public String getAudience() {
		return claims.getBody().getAudience();
	}
	
	/**
	 * Returns the "exp" body value (Registered Claim Name) or null if not present. 
	 * @return
	 */
	public Date getExpiration() {
		return claims.getBody().getExpiration();
	}
	
	/**
	 * Returns the "nbf" body value (Registered Claim Name) or null if not present. 
	 * @return
	 */
	public Date getNotBefore() {
		return claims.getBody().getNotBefore();
	}
	
	/**
	 * Returns the "iat" body value (Registered Claim Name) or null if not present. 
	 * @return
	 */
	public Date getIssuedAt() {
		return claims.getBody().getIssuedAt();
	}

	/**
	 * Returns the "iss" body value (Registered Claim Name) or null if not present. 
	 * @return
	 */
	public String getIssuer() {
		return claims.getBody().getIssuer();
	}
	
	/**
	 * Returns the specific claim value (Private Claim/not registered) or null if not present. 
	 * @param claimName
	 * @return
	 */
	public String get(String claimName) {
		return (String) claims.getBody().get(claimName);
	}
	
	//---------------------------------------------------------------------
	// JWT SIGNATURE
	//---------------------------------------------------------------------

	/**
	 * Returns the signature
	 * @return
	 */
	public String getSignature() {
		return claims.getSignature();
	}

}
