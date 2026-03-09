package com.ingeniusapps.antares.net.webservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class WebService extends Application {

    private long EXPIRATION_TIME = 300_000; // Default value is 5 mins.
    private String SECRET_KEY = "=Rl?lGucuspu0?#troz!Drec-"; // Basic secret key default.

    public WebService() {
    }

    public WebService(long EXPIRATION_TIME, String SECRET_KEY) {
        this.EXPIRATION_TIME = EXPIRATION_TIME;
        this.SECRET_KEY = SECRET_KEY;
    }

    protected void setJWTokenSettings(long expirationTime, String secretkey) {
        this.EXPIRATION_TIME = expirationTime;
        this.SECRET_KEY = secretkey;
    }

    protected void setJWTokenSettings(long expirationTime) {
        this.EXPIRATION_TIME = expirationTime;
    }

    protected void setJWTokenSettings(String secretkey) {
        this.SECRET_KEY = secretkey;
    }

    protected String generateJWToken(String audience, Map<String, String> claims, String issuer, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("claims", claims)
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    protected String refreshJWToken(String token) {
        DecodedJWT jwtDecoded = JWT.decode(token);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withAudience(jwtDecoded.getAudience().getFirst())
                .withIssuer(jwtDecoded.getIssuer())
                .withJWTId(jwtDecoded.getId())
                .withClaim("claims", jwtDecoded.getClaim("claims").asMap())
                .withSubject(jwtDecoded.getSubject())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    protected DecodedJWT getJWTokenInfo(String token) {
        return JWT.decode(token);
    }

    protected boolean isValidJWToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);

            return true;
        } catch (TokenExpiredException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    protected String getJWTokenFromHttpHeaders(HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtoken = authorizationHeader.substring("Bearer ".length());

            return jwtoken;
        } else {
            return null;
        }
    }

    protected boolean isClientSupported(HttpHeaders httpHeaders, String[] supported_clients) {
        String userAgent = httpHeaders.getHeaderString(HttpHeaders.USER_AGENT);

        if (userAgent == null) {
            return false;
        }

        for (String client : supported_clients) {
            if (client.equals(userAgent)) {
                return true;
            }
        }

        return false;
    }

}
