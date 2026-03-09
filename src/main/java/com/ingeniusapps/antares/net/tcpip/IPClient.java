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
 * @author Harold Ortega Pérez.
 * @version 1.0
 *
 */
public class IPClient {

    private Socket socket = null;
    private IOException error = null;
    private PrintWriter output = null;
    private Scanner input = null;

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

    public IOException getError() {
        return this.error;
    }

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

    public boolean speakToServer(String message) {
        if (this.output == null) {
            return false;
        }

        this.output.println(message);
        this.output.flush();

        return true;
    }

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

    public String listenToServer() {
        if (this.input == null) {
            return null;
        }

        String inMessage = this.input.nextLine();

        return inMessage;
    }

    public static String getIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            return "0.0.0.0";
        }
    }
}
