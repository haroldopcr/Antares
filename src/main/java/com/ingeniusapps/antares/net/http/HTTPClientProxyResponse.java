/*
 * Este código y su lógica son propiedad intelectual de InGenius Apps.
 * Está prohibida su reproducción parcial o total así como cualquier publicación del mismo
 * sin la debida autorización de los propietarios legales de la compañía.
 */
package com.ingeniusapps.antares.net.http;

/**
 * Representa una respuesta genérica de una operación proxy HTTP.
 *
 * <p>Esta clase actúa como contenedor de datos para transportar el resultado
 * de una invocación HTTP, incluyendo:</p>
 *
 * <ul>
 *   <li>el código de respuesta HTTP,</li>
 *   <li>el valor o contenido retornado,</li>
 *   <li>y una etiqueta auxiliar de contexto.</li>
 * </ul>
 *
 * <p>Su diseño genérico permite reutilizarla con distintos tipos de respuesta
 * según las necesidades del consumidor.</p>
 *
 * @param <T> tipo del valor contenido en la respuesta
 */
public class HTTPClientProxyResponse<T> {

    /**
     * Código de respuesta HTTP asociado a la operación.
     *
     * <p>Se inicializa con {@code 500} como valor por defecto, representando
     * un estado de error interno hasta que se asigne un resultado concreto.</p>
     */
    public int responseCode = 500;

    /**
     * Valor de respuesta devuelto por la operación HTTP.
     *
     * <p>Puede contener el cuerpo procesado de la respuesta, un objeto de dominio
     * o cualquier otra representación tipada del resultado.</p>
     */
    public T responseValue;

    /**
     * Etiqueta auxiliar asociada a la respuesta.
     *
     * <p>Puede utilizarse para identificar contexto adicional, clasificaciones,
     * estados complementarios o metadatos definidos por la aplicación.</p>
     */
    public String tag;
}