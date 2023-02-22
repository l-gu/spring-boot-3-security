package org.demo.myapp.application.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Signature algorithm enumeration (just for JWT library isolation)
 *
 */
public enum JwsAlgorithm {

	HS256(SignatureAlgorithm.HS256, false),  // No Key HMAC only
	HS384(SignatureAlgorithm.HS384, false),  // No Key HMAC only
	HS512(SignatureAlgorithm.HS512, false),  // No Key HMAC only

	RS256(SignatureAlgorithm.RS256, true), 
	RS384(SignatureAlgorithm.RS384, true),
	RS512(SignatureAlgorithm.RS512, true),

	ES256(SignatureAlgorithm.ES256, true), 
	ES384(SignatureAlgorithm.ES384, true), 
	ES512(SignatureAlgorithm.ES512, true),

	PS256(SignatureAlgorithm.PS256, true), 
	PS384(SignatureAlgorithm.PS384, true),
	PS512(SignatureAlgorithm.PS512, true);

	private final SignatureAlgorithm signatureAlgorithm;
	private final boolean requiresKey;

	private JwsAlgorithm(SignatureAlgorithm signatureAlgorithm, boolean requiresKey) {
		this.signatureAlgorithm = signatureAlgorithm;
		this.requiresKey = requiresKey;
	}

	public SignatureAlgorithm getSignatureAlgorithm() {
		return this.signatureAlgorithm;
	}

	public boolean requiresKey() {
		return this.requiresKey;
	}
}
