package com.ingeniusapps.antares.net.tcpip;

import com.ingeniusapps.antares.security.Security;
import com.ingeniusapps.antares.security.Security.SecretKeyMode;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author haroldop
 */
public class IPServerSocket {

    private Socket socket = null;
    private IOException error = null;
    private PrintWriter output = null;
    private Scanner input = null;

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

    public IOException getError() {
        return this.error;
    }

    public String getClientIPAddress() {
        if (this.socket == null) {
            return null;
        }

        return this.socket.getInetAddress().getHostAddress();
    }

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

    public boolean speakToClient(String message) {
        if (this.output == null) {
            return false;
        }

        output.println(message);
        output.flush();

        return true;
    }

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

    public String listenToClient() {
        if (this.input == null) {
            return null;
        }

        String inMessage = this.input.nextLine();

        return inMessage;
    }

}
