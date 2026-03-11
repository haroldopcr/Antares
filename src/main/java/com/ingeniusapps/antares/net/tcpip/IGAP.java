package com.ingeniusapps.antares.net.tcpip;

/**
 * Representa una estructura simple de mensaje compuesta por encabezado y cuerpo.
 *
 * <p>Esta clase actúa como contenedor liviano de datos para intercambiar o
 * transportar información textual en dos partes diferenciadas:</p>
 *
 * <ul>
 *   <li><b>header</b>: sección de encabezado o metadatos del mensaje</li>
 *   <li><b>body</b>: contenido principal del mensaje</li>
 * </ul>
 *
 * <p>Su diseño deliberadamente minimalista la hace apropiada como objeto de
 * transporte dentro de flujos de comunicación TCP/IP u otros mecanismos de
 * intercambio de datos de la librería.</p>
 */
public class IGAP {

    /**
     * Encabezado del mensaje.
     *
     * <p>Puede utilizarse para almacenar información de control, tipo de mensaje,
     * identificadores o cualquier otro metadato asociado.</p>
     */
    public String header;

    /**
     * Cuerpo principal del mensaje.
     *
     * <p>Contiene la carga útil o contenido funcional asociado al mensaje.</p>
     */
    public String body;
}