package dao;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.*;

public class JwtManager {
	// pour SHA256 : 256 bits mini
	private static final String SECRET_KEY = "bachibouzoukbachibouzoukbachibouzoukbachibouzouk";

	public static String createJWT(String name) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		JwtBuilder token = Jwts.builder().setId(UUID.randomUUID().toString().replace("-", "")).setIssuedAt(now)
				.setSubject("Authentification pour SAE").setIssuer(name).signWith(signingKey, signatureAlgorithm);

		// if it has been specified, let's add the expiration
		long ttlMillis = 1000 * 60 * 30; // 4mn
		if (ttlMillis > 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			token.setExpiration(exp); // 20mn par defaut
		}
		// Builds the JWT and serializes it to a compact, URL-safe string
		return token.compact();
	}

	public static Claims decodeJWT(String jwt) throws Exception {
		// This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(SECRET_KEY)).build()
				// verifie la signature et l'iat
				.parseClaimsJws(jwt).getBody();
		return claims;
	}

	public static boolean autorizedToken(String authTokenHeader) {
		String token = authTokenHeader.substring("Bearer".length()).trim();
		try {
			JwtManager.decodeJWT(token);
			System.out.println("CA MARCHE OMG");
			return true;
		} catch (Exception e) {
			System.out.println("Token cassé");
			return false;
		}
	}
}