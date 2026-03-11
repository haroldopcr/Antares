/*
 * Este código y su lógica son propiedad intelectual de InGenius Apps.
 * Está prohibida su reproducción parcial o total así como cualquier publicación del mismo
 * sin la debida autorización de los propietarios legales de la compañía.
 */
package com.ingeniusapps.antares.net.http;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

/**
 * Cliente proxy HTTP simplificado para ejecutar solicitudes {@code GET} y
 * {@code POST} utilizando la API {@link HttpClient} de Java.
 *
 * <p>Esta clase encapsula la configuración de un cliente HTTP, la construcción
 * de solicitudes y la recepción de respuestas en dos formatos posibles:
 * texto ({@link String}) o binario ({@code byte[]}).</p>
 *
 * <p>El comportamiento de la respuesta se controla mediante
 * {@link HTTP_RESPONSE_TYPE}, mientras que la versión del protocolo HTTP se
 * define a través de {@link HTTP_VERSION}.</p>
 *
 * <p>La clase está pensada como utilitario orientado a instancia: cada objeto
 * representa una solicitud configurada sobre una URL base, un conjunto de
 * parámetros y un tipo esperado de respuesta.</p>
 */
public class HTTPClientProxy {

    /**
     * Tipos de respuesta soportados por el cliente HTTP.
     */
    public enum HTTP_RESPONSE_TYPE {

        /**
         * Respuesta esperada como texto plano.
         */
        STRING,

        /**
         * Respuesta esperada como contenido binario.
         */
        BINARY
    }

    /**
     * Versiones del protocolo HTTP soportadas por el cliente.
     */
    public enum HTTP_VERSION {

        /**
         * Utiliza HTTP/1.1.
         */
        HTTP1_1,

        /**
         * Utiliza HTTP/2.
         */
        HTTP2
    }

    /**
     * Cliente HTTP subyacente utilizado para enviar solicitudes.
     */
    private HttpClient _httpClient = null;

    /**
     * Constructor acumulativo de la solicitud HTTP.
     */
    private HttpRequest.Builder _httpRequestBuilder = null;

    /**
     * Parámetros de consulta enviados en la URL como query string.
     */
    private String _httpParameters = "";

    /**
     * URL base de la solicitud.
     */
    private String _url = "";

    /**
     * Tipo de respuesta esperado para la solicitud.
     */
    private HTTP_RESPONSE_TYPE _httpResponseType;

    /**
     * Crea una nueva instancia configurada para comunicarse con una URL dada.
     *
     * <p>El constructor inicializa el cliente HTTP con la versión solicitada
     * y prepara un {@link HttpRequest.Builder} usando la URL base más los
     * parámetros recibidos como query string, cuando estos no están vacíos.</p>
     *
     * @param URL URL de destino a la cual acceder
     * @param parameters parámetros a enviar mediante query string
     * @param httpResponseType tipo esperado de la respuesta
     * @param HTTPVersion versión del protocolo HTTP a utilizar
     */
    public HTTPClientProxy(String URL, String parameters, HTTP_RESPONSE_TYPE httpResponseType, HTTP_VERSION HTTPVersion) {
        this._url = URL;
        this._httpParameters = parameters;
        this._httpResponseType = httpResponseType;

        if (HTTPVersion == HTTP_VERSION.HTTP2) {
            this._httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        } else {
            this._httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        }

        this._httpRequestBuilder = HttpRequest.newBuilder(URI.create(this._url + (!parameters.isBlank() ? "?" + parameters : "")));
    }

    /**
     * Agrega un encabezado HTTP a la solicitud.
     *
     * <p>Si el encabezado ya existe, este método añade un nuevo valor sin
     * reemplazar los previamente definidos.</p>
     *
     * @param key nombre del encabezado
     * @param value valor del encabezado
     */
    public void addHeader(String key, String value) {
        this._httpRequestBuilder.header(key, value);
    }

    /**
     * Define o reemplaza un encabezado HTTP en la solicitud.
     *
     * @param key nombre del encabezado
     * @param value valor del encabezado
     */
    public void setHeader(String key, String value) {
        this._httpRequestBuilder.setHeader(key, value);
    }

    /**
     * Ejecuta una solicitud HTTP {@code POST} enviando un cuerpo de texto.
     *
     * <p>El tipo del valor retornado en {@link HTTPClientProxyResponse#responseValue}
     * dependerá del {@link HTTP_RESPONSE_TYPE} configurado: {@link String} para
     * respuestas textuales o {@code byte[]} para respuestas binarias.</p>
     *
     * <p>Si ocurre un error de comunicación o la solicitud es interrumpida,
     * se retorna una respuesta con código {@code 500}, valor nulo y una
     * etiqueta descriptiva en {@link HTTPClientProxyResponse#tag}.</p>
     *
     * @param body contenido textual a enviar como cuerpo del {@code POST}
     * @return respuesta HTTP encapsulada en un {@link HTTPClientProxyResponse}
     */
    public HTTPClientProxyResponse POST(String body) {
        try {
            HttpRequest httpRequest = _httpRequestBuilder.POST(BodyPublishers.ofString(body)).build();

            if (_httpResponseType == HTTP_RESPONSE_TYPE.BINARY) {
                HttpResponse<byte[]> httpResponse = _httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());

                HTTPClientProxyResponse<byte[]> _httpClientProxyResponse = new HTTPClientProxyResponse<byte[]>();
                _httpClientProxyResponse.responseCode = httpResponse.statusCode();
                _httpClientProxyResponse.responseValue = httpResponse.body();
                _httpClientProxyResponse.tag = "";

                return _httpClientProxyResponse;
            } else {
                HttpResponse<String> httpResponse = _httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                HTTPClientProxyResponse<String> _httpClientProxyResponse = new HTTPClientProxyResponse<String>();
                _httpClientProxyResponse.responseCode = httpResponse.statusCode();
                _httpClientProxyResponse.responseValue = httpResponse.body();
                _httpClientProxyResponse.tag = "";

                return _httpClientProxyResponse;
            }
        } catch (IOException | InterruptedException ex) {
            HTTPClientProxyResponse<String> _httpClientProxyResponse = new HTTPClientProxyResponse<String>();

            _httpClientProxyResponse.responseCode = 500;
            _httpClientProxyResponse.responseValue = null;
            _httpClientProxyResponse.tag = "Could not have a valid response from the server.";

            return _httpClientProxyResponse;
        }
    }

    /**
     * Ejecuta una solicitud HTTP {@code POST} sin cuerpo.
     *
     * <p>Antes de enviar la solicitud, este método agrega el encabezado
     * {@code Content-Length: 0} y utiliza {@link BodyPublishers#noBody()}.</p>
     *
     * <p>El tipo del valor retornado en {@link HTTPClientProxyResponse#responseValue}
     * dependerá del {@link HTTP_RESPONSE_TYPE} configurado: {@link String} para
     * respuestas textuales o {@code byte[]} para respuestas binarias.</p>
     *
     * <p>Si ocurre un error de comunicación o la solicitud es interrumpida,
     * se retorna una respuesta con código {@code 500}, valor nulo y una
     * etiqueta descriptiva en {@link HTTPClientProxyResponse#tag}.</p>
     *
     * @return respuesta HTTP encapsulada en un {@link HTTPClientProxyResponse}
     */
    public HTTPClientProxyResponse POST() {
        try {
            this.addHeader("Content-Length", "0");
            HttpRequest httpRequest = _httpRequestBuilder.POST(BodyPublishers.noBody()).build();

            if (_httpResponseType == HTTP_RESPONSE_TYPE.BINARY) {
                HttpResponse<byte[]> httpResponse = _httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());

                HTTPClientProxyResponse<byte[]> _httpClientProxyResponse = new HTTPClientProxyResponse<byte[]>();
                _httpClientProxyResponse.responseCode = httpResponse.statusCode();
                _httpClientProxyResponse.responseValue = httpResponse.body();
                _httpClientProxyResponse.tag = "";

                return _httpClientProxyResponse;
            } else {
                HttpResponse<String> httpResponse = _httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                HTTPClientProxyResponse<String> _httpClientProxyResponse = new HTTPClientProxyResponse<String>();
                _httpClientProxyResponse.responseCode = httpResponse.statusCode();
                _httpClientProxyResponse.responseValue = httpResponse.body();
                _httpClientProxyResponse.tag = "";

                return _httpClientProxyResponse;
            }
        } catch (IOException | InterruptedException ex) {
            HTTPClientProxyResponse<String> _httpClientProxyResponse = new HTTPClientProxyResponse<String>();

            _httpClientProxyResponse.responseCode = 500;
            _httpClientProxyResponse.responseValue = null;
            _httpClientProxyResponse.tag = "Could not have a valid response from the server.";

            return _httpClientProxyResponse;
        }
    }

    /**
     * Ejecuta una solicitud HTTP {@code GET}.
     *
     * <p>El tipo del valor retornado en {@link HTTPClientProxyResponse#responseValue}
     * dependerá del {@link HTTP_RESPONSE_TYPE} configurado: {@link String} para
     * respuestas textuales o {@code byte[]} para respuestas binarias.</p>
     *
     * <p>Si ocurre un error de comunicación o la solicitud es interrumpida,
     * se retorna una respuesta con código {@code 500}, valor nulo y una
     * etiqueta descriptiva en {@link HTTPClientProxyResponse#tag}.</p>
     *
     * @return respuesta HTTP encapsulada en un {@link HTTPClientProxyResponse}
     */
    public HTTPClientProxyResponse GET() {
        try {
            HttpRequest httpRequest = _httpRequestBuilder.GET().build();

            if (_httpResponseType == HTTP_RESPONSE_TYPE.BINARY) {
                HttpResponse<byte[]> httpResponse = _httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());

                HTTPClientProxyResponse<byte[]> _httpClientProxyResponse = new HTTPClientProxyResponse<byte[]>();
                _httpClientProxyResponse.responseCode = httpResponse.statusCode();
                _httpClientProxyResponse.responseValue = httpResponse.body();
                _httpClientProxyResponse.tag = "";

                return _httpClientProxyResponse;
            } else {
                HttpResponse<String> httpResponse = _httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                HTTPClientProxyResponse<String> _httpClientProxyResponse = new HTTPClientProxyResponse<String>();
                _httpClientProxyResponse.responseCode = httpResponse.statusCode();
                _httpClientProxyResponse.responseValue = httpResponse.body();
                _httpClientProxyResponse.tag = "";

                return _httpClientProxyResponse;
            }
        } catch (IOException | InterruptedException ex) {
            HTTPClientProxyResponse<String> _httpClientProxyResponse = new HTTPClientProxyResponse<String>();

            _httpClientProxyResponse.responseCode = 500;
            _httpClientProxyResponse.responseValue = null;
            _httpClientProxyResponse.tag = "Could not have a valid response from the server.";

            return _httpClientProxyResponse;
        }
    }
}