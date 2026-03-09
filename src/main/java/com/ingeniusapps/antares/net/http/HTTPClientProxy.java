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
 * @author Harold Ortega Pérez.
 * @version 1.0
 *
 */
public class HTTPClientProxy {

    public enum HTTP_RESPONSE_TYPE {
        STRING,
        BINARY
    }

    public enum HTTP_VERSION {
        HTTP1_1,
        HTTP2
    }

    private HttpClient _httpClient = null;
    private HttpRequest.Builder _httpRequestBuilder = null;
    private String _httpParameters = "";
    private String _url = "";
    private HTTP_RESPONSE_TYPE _httpResponseType;

    /**
     *
     * @param URL URL to Access.
     * @param parameters Parameters to send via Query String.
     * @param httpResponseType Expected data format of the response.
     * @param HTTPVersion HTTP Version to use (2 or 1.1).
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

    public void addHeader(String key, String value) {
        this._httpRequestBuilder.header(key, value);
    }

    public void setHeader(String key, String value) {
        this._httpRequestBuilder.setHeader(key, value);
    }

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
