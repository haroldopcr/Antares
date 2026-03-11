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

/**
 * Clase base para servicios web JAX-RS con utilidades integradas para manejo de
 * tokens JWT y validación básica de clientes HTTP.
 *
 * <p>Esta clase extiende {@link Application} y proporciona métodos protegidos
 * para generar, refrescar, decodificar y validar JSON Web Tokens (JWT), así
 * como para extraer tokens desde encabezados HTTP y validar clientes a partir
 * del encabezado {@code User-Agent}.</p>
 *
 * <p>Está pensada como clase base reutilizable para ser extendida por servicios
 * web concretos dentro de la librería o por aplicaciones consumidoras.</p>
 */
public class WebService extends Application {

    /**
     * Tiempo de expiración del token JWT en milisegundos.
     *
     * <p>El valor por defecto es de 5 minutos ({@code 300_000} ms).</p>
     */
    private long EXPIRATION_TIME = 300_000;

    /**
     * Clave secreta usada para firmar y verificar tokens JWT.
     *
     * <p>Se inicializa con un valor básico por defecto que puede ser
     * reemplazado mediante constructores o métodos protegidos de configuración.</p>
     */
    private String SECRET_KEY = "=Rl?lGucuspu0?#troz!Drec-";

    /**
     * Crea una instancia con la configuración predeterminada de expiración y clave secreta.
     */
    public WebService() {
    }

    /**
     * Crea una instancia con configuración personalizada para tokens JWT.
     *
     * @param EXPIRATION_TIME tiempo de expiración del token en milisegundos
     * @param SECRET_KEY clave secreta usada para firmar y validar los tokens
     */
    public WebService(long EXPIRATION_TIME, String SECRET_KEY) {
        this.EXPIRATION_TIME = EXPIRATION_TIME;
        this.SECRET_KEY = SECRET_KEY;
    }

    /**
     * Actualiza simultáneamente la configuración de expiración y clave secreta
     * utilizada para la generación y validación de tokens JWT.
     *
     * @param expirationTime nuevo tiempo de expiración en milisegundos
     * @param secretkey nueva clave secreta
     */
    protected void setJWTokenSettings(long expirationTime, String secretkey) {
        this.EXPIRATION_TIME = expirationTime;
        this.SECRET_KEY = secretkey;
    }

    /**
     * Actualiza únicamente el tiempo de expiración de los tokens JWT.
     *
     * @param expirationTime nuevo tiempo de expiración en milisegundos
     */
    protected void setJWTokenSettings(long expirationTime) {
        this.EXPIRATION_TIME = expirationTime;
    }

    /**
     * Actualiza únicamente la clave secreta usada para tokens JWT.
     *
     * @param secretkey nueva clave secreta
     */
    protected void setJWTokenSettings(String secretkey) {
        this.SECRET_KEY = secretkey;
    }

    /**
     * Genera un nuevo token JWT firmado con HMAC SHA-512.
     *
     * <p>El token incluye audiencia, emisor, identificador único, reclamos
     * personalizados, sujeto, fecha de emisión y fecha de expiración.</p>
     *
     * @param audience audiencia del token
     * @param claims mapa de reclamos personalizados a incluir en el token
     * @param issuer emisor del token
     * @param username sujeto del token, típicamente el nombre de usuario
     * @return token JWT firmado
     */
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

    /**
     * Genera un nuevo token JWT a partir de uno existente, conservando su
     * información principal y asignando una nueva fecha de emisión y expiración.
     *
     * @param token token original a refrescar
     * @return nuevo token JWT firmado con la configuración actual
     */
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

    /**
     * Decodifica un token JWT sin verificar su firma ni validar su expiración.
     *
     * @param token token JWT a decodificar
     * @return representación decodificada del token
     */
    protected DecodedJWT getJWTokenInfo(String token) {
        return JWT.decode(token);
    }

    /**
     * Verifica si un token JWT es válido según la clave secreta configurada.
     *
     * <p>Este método comprueba la firma y la vigencia temporal del token.</p>
     *
     * @param token token JWT a validar
     * @return {@code true} si el token es válido; de lo contrario, {@code false}
     */
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

    /**
     * Extrae un token JWT desde el encabezado HTTP {@code Authorization}.
     *
     * <p>El método espera un valor con el formato
     * {@code Bearer <token>}.</p>
     *
     * @param httpHeaders encabezados HTTP de la solicitud
     * @return token JWT extraído o {@code null} si el encabezado no existe
     *         o no cumple el formato esperado
     */
    protected String getJWTokenFromHttpHeaders(HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtoken = authorizationHeader.substring("Bearer ".length());

            return jwtoken;
        } else {
            return null;
        }
    }

    /**
     * Determina si el cliente que realiza la solicitud HTTP se encuentra dentro
     * del conjunto de clientes soportados.
     *
     * <p>La validación se realiza comparando el encabezado {@code User-Agent}
     * con los valores permitidos.</p>
     *
     * @param httpHeaders encabezados HTTP de la solicitud
     * @param supported_clients arreglo de clientes permitidos
     * @return {@code true} si el cliente está soportado; en caso contrario,
     *         {@code false}
     */
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