package controleurs;

import java.io.IOException;

// code pompé ici : https://developer.okta.com/blog/2018/10/31/jwts-with-java
// lui-même inspiré par : https://www.baeldung.com/java-json-web-tokens-jjwt
// et sinon la doc : https://github.com/jwtk/jjwt/blob/master/README.md

import java.security.Key;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DS;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtManager {
	// pour SHA256 : 256 bits mini
	private static final String SECRET_KEY = "bachibouzoukbachibouzoukbachibouzoukbachibouzouk";

	public static String createJWT(int id, String login) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = Base64.getDecoder().decode(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");

		// Let's set the JWT Claims
		JwtBuilder token = Jwts.builder().setHeaderParams(headers).setSubject("Authentification").setId("" + id)
				.setIssuer(login).setIssuedAt(now).signWith(signingKey, signatureAlgorithm);

		// if it has been specified, let's add the expiration
		long ttlMillis = 1000 * 60 * 20; // 20mn
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

	public static int getIdByToken(String token) {
		String[] section = token.split("\\.");

		Base64.Decoder decoder = Base64.getUrlDecoder();
		String utile = new String(decoder.decode(section[1]));

		String attribut = utile.split(",")[1];

		String value = attribut.split(":")[1];

		int id = Integer.parseInt(value.split("\"")[1]);

		return id;
	}

	public static boolean tokenValid(String token) {
		// Si le token est valide
		try {
			JwtManager.decodeJWT(token);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public static void verifToken(HttpServletRequest req, HttpServletResponse res){
		try {
			String authorization = req.getHeader("Authorization");
			if (authorization == null) {
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			String token = "";
			String[] tokenSlices = authorization.split(" ");
			if (tokenSlices.length == 2 && tokenSlices[0].equals("Bearer")) {
				token = tokenSlices[1];
			}
			try {
				JwtManager.decodeJWT(token); // Throws an exception if it's invalid
			} catch (Exception e) {
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		} catch(Exception e ) {
			try {
				res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
}
