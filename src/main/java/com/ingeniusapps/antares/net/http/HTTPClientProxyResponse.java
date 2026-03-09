/*
 * Este código y su lógica son propiedad intelectual de InGenius Apps.
 * Está prohibida su reproducción parcial o total así como cualquier publicación del mismo
 * sin la debida autorización de los propietarios legales de la compañía.
 */
package com.ingeniusapps.antares.net.http;

/**
 * @author Harold Ortega Pérez.
 * @version 1.0
 *
 */
public class HTTPClientProxyResponse<T> {

    public int responseCode = 500;
    public T responseValue;
    public String tag;

}
