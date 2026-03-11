package com.ingeniusapps.antares.net.tcpip;

import com.ingeniusapps.antares.security.Security;
import com.ingeniusapps.antares.security.Security.SecretKeyMode;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Encapsula un {@link Socket} de servidor TCP/IP y proporciona operaciones
 * simplificadas para envío, recepción y cierre de comunicación con un cliente.
 *
 * <p>Esta clase abstrae el uso de {@link PrintWriter} y {@link Scanner} para
 * facilitar el intercambio de mensajes de texto con el cliente conectado.
 * Además, ofrece variantes de envío y recepción con soporte opcional de
 * cifrado mediante la clase {@link Security}.</p>
 *
 * <p>La instancia mantiene la referencia al último error de tipo
 * {@link IOException} producido durante operaciones de cierre.</p>
 */
public class IPServerSocket {

    /**
     * Socket subyacente utilizado para la comunicación con el cliente.
     */
    private Socket socket = null;

    /**
     * Último error de entrada/salida registrado durante operaciones de cierre.
     */
    private IOException error = null;

    /**
     * Flujo de salida textual hacia el cliente.
     */
    private PrintWriter output = null;

    /**
     * Flujo de entrada textual proveniente del cliente.
     */
    private Scanner input = null;

    /**
     * Crea una nueva instancia asociada a un socket ya aceptado por el servidor.
     *
     * <p>Durante la construcción se inicializan los mecanismos de lectura y
     * escritura del socket. Si ocurre un error de entrada/salida durante esta
     * inicialización, los canales de entrada y salida quedan en {@code null}.</p>
     *
     * @param socket socket del cliente conectado
     */
    public IPServerSocket(Socket socket) {
        this.socket = socket;

        try {
            this.output = new PrintWriter(this.socket.getOutputStream(), true);
            this.input = new Scanner(this.socket.getInputStream());
        } catch (IOException ex) {
            this.output = null;
            this.input = null;
        }
    }

    /**
     * Cierra los recursos asociados a esta conexión.
     *
     * <p>Este método intenta cerrar en orden el flujo de salida, el flujo de
     * entrada y el socket principal. Si ocurre una excepción durante el cierre
     * del socket, esta se almacena en {@link #error} y el método retorna
     * {@code false}.</p>
     *
     * @return {@code true} si el cierre se completó correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean close() {
        this.error = null;

        try {
            if (this.output != null) {
                this.output.close();
            }

            if (this.input != null) {
                this.input.close();
            }

            if (this.socket != null) {
                this.socket.close();
            }

            return true;
        } catch (IOException ex) {
            this.error = ex;
            return false;
        }
    }

    /**
     * Obtiene el último error de entrada/salida registrado por la instancia.
     *
     * @return excepción registrada o {@code null} si no existe error
     */
    public IOException getError() {
        return this.error;
    }

    /**
     * Obtiene la dirección IP del cliente conectado.
     *
     * @return dirección IP del cliente en formato texto, o {@code null}
     *         si no existe un socket asociado
     */
    public String getClientIPAddress() {
        if (this.socket == null) {
            return null;
        }

        return this.socket.getInetAddress().getHostAddress();
    }

    /**
     * Envía un mensaje al cliente aplicando cifrado previo.
     *
     * <p>El mensaje se cifra mediante {@link Security#encrypt(String, String, SecretKeyMode)}
     * antes de ser enviado por el canal de salida.</p>
     *
     * @param message mensaje en texto plano a enviar
     * @param encryptionKey clave de cifrado utilizada para proteger el mensaje
     * @param secretKeyMode modo de derivación de clave usado en el proceso de cifrado
     * @return {@code true} si el mensaje fue cifrado y enviado correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean speakToClient(String message, String encryptionKey, SecretKeyMode secretKeyMode) {
        if (this.output == null) {
            return false;
        }

        String outMessage;
        try {
            outMessage = Security.encrypt(message, encryptionKey, secretKeyMode);
        } catch (Exception ex) {
            return false;
        }

        output.println(outMessage);
        output.flush();

        return true;
    }

    /**
     * Envía un mensaje de texto plano al cliente.
     *
     * @param message mensaje a enviar
     * @return {@code true} si el mensaje fue enviado correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean speakToClient(String message) {
        if (this.output == null) {
            return false;
        }

        output.println(message);
        output.flush();

        return true;
    }

    /**
     * Escucha un mensaje proveniente del cliente y lo descifra antes de retornarlo.
     *
     * <p>El mensaje recibido debe haber sido cifrado previamente con una clave y
     * modo compatibles con los proporcionados en este método.</p>
     *
     * @param encryptionKey clave utilizada para descifrar el mensaje
     * @param secretKeyMode modo de derivación de clave utilizado en el descifrado
     * @return mensaje descifrado, o {@code null} si no existe entrada disponible
     *         o si ocurre un error durante el descifrado
     */
    public String listenToClient(String encryptionKey, SecretKeyMode secretKeyMode) {
        if (this.input == null) {
            return null;
        }

        String inMessage = this.input.nextLine();

        try {
            return Security.decrypt(inMessage, encryptionKey, secretKeyMode);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Escucha un mensaje de texto plano proveniente del cliente.
     *
     * @return mensaje recibido, o {@code null} si no existe canal de entrada disponible
     */
    public String listenToClient() {
        if (this.input == null) {
            return null;
        }

        String inMessage = this.input.nextLine();

        return inMessage;
    }
}