package com.ingeniusapps.antares.net.tcpip;

import com.ingeniusapps.antares.security.Security;
import com.ingeniusapps.antares.security.Security.SecretKeyMode;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Implementa un cliente TCP/IP básico con soporte para envío y recepción de
 * mensajes en texto plano o cifrados.
 *
 * <p>Esta clase encapsula la conexión con un servidor remoto mediante
 * {@link Socket}, simplificando las operaciones de conexión, desconexión,
 * envío y recepción de mensajes. También ofrece integración opcional con
 * la clase {@link Security} para proteger la comunicación mediante cifrado.</p>
 *
 * <p>La instancia conserva la referencia al último error de tipo
 * {@link IOException} producido durante las operaciones de conexión o cierre.</p>
 */
public class IPClient {

    /**
     * Socket subyacente utilizado para la conexión con el servidor.
     */
    private Socket socket = null;

    /**
     * Último error de entrada/salida registrado por la instancia.
     */
    private IOException error = null;

    /**
     * Flujo de salida textual hacia el servidor.
     */
    private PrintWriter output = null;

    /**
     * Flujo de entrada textual proveniente del servidor.
     */
    private Scanner input = null;

    /**
     * Establece una conexión TCP/IP con el host y puerto indicados.
     *
     * <p>Si la conexión se realiza correctamente, se inicializan los mecanismos
     * de entrada y salida asociados al socket. Si ocurre un error durante el
     * proceso, el socket se cierra en la medida de lo posible y el error queda
     * registrado en {@link #error}.</p>
     *
     * @param host dirección o nombre de host del servidor remoto
     * @param port puerto del servidor remoto
     * @return {@code true} si la conexión fue establecida correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean connect(String host, int port) {
        this.error = null;

        try {
            this.socket = new Socket(host, port);
            this.output = new PrintWriter(this.socket.getOutputStream(), true);
            this.input = new Scanner(this.socket.getInputStream());

            return true;
        } catch (IOException ex) {
            this.error = ex;

            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (IOException ex1) {
            }

            return false;
        }
    }

    /**
     * Cierra la conexión actual con el servidor y libera los recursos asociados.
     *
     * <p>Este método intenta cerrar en orden el flujo de salida, el flujo de
     * entrada y el socket principal. Si ocurre una excepción durante el cierre
     * del socket, esta se almacena en {@link #error} y el método retorna
     * {@code false}.</p>
     *
     * @return {@code true} si el cierre se completó correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean disconnect() {
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
     * Envía un mensaje al servidor aplicando cifrado previo.
     *
     * <p>El mensaje se cifra mediante {@link Security#encrypt(String, String, SecretKeyMode)}
     * antes de ser enviado por el canal de salida.</p>
     *
     * @param message mensaje en texto plano a enviar
     * @param encryptionKey clave utilizada para el cifrado
     * @param secretKeyMode modo de derivación de clave utilizado en el proceso de cifrado
     * @return {@code true} si el mensaje fue cifrado y enviado correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean speakToServer(String message, String encryptionKey, SecretKeyMode secretKeyMode) {
        if (this.output == null) {
            return false;
        }

        String outMessage;
        try {
            outMessage = Security.encrypt(message, encryptionKey, secretKeyMode);
        } catch (Exception ex) {
            return false;
        }

        this.output.println(outMessage);
        this.output.flush();

        return true;
    }

    /**
     * Envía un mensaje de texto plano al servidor.
     *
     * @param message mensaje a enviar
     * @return {@code true} si el mensaje fue enviado correctamente;
     *         en caso contrario, {@code false}
     */
    public boolean speakToServer(String message) {
        if (this.output == null) {
            return false;
        }

        this.output.println(message);
        this.output.flush();

        return true;
    }

    /**
     * Escucha un mensaje proveniente del servidor y lo descifra antes de retornarlo.
     *
     * <p>El mensaje recibido debe haber sido cifrado previamente con una clave y
     * modo compatibles con los proporcionados en este método.</p>
     *
     * @param encryptionKey clave utilizada para descifrar el mensaje
     * @param secretKeyMode modo de derivación de clave utilizado en el descifrado
     * @return mensaje descifrado, o {@code null} si no existe entrada disponible
     *         o si ocurre un error durante el descifrado
     */
    public String listenToServer(String encryptionKey, SecretKeyMode secretKeyMode) {
        if (this.input == null) {
            return null;
        }

        String inMessage = this.input.nextLine();

        try {
            inMessage = Security.decrypt(inMessage, encryptionKey, secretKeyMode);
        } catch (Exception ex) {
            return null;
        }

        return inMessage;
    }

    /**
     * Escucha un mensaje de texto plano proveniente del servidor.
     *
     * @return mensaje recibido, o {@code null} si no existe canal de entrada disponible
     */
    public String listenToServer() {
        if (this.input == null) {
            return null;
        }

        String inMessage = this.input.nextLine();

        return inMessage;
    }

    /**
     * Obtiene la dirección IP local reportada por el host actual.
     *
     * @return dirección IP local en formato texto; si no puede determinarse,
     *         retorna {@code "0.0.0.0"}
     */
    public static String getIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            return "0.0.0.0";
        }
    }
}