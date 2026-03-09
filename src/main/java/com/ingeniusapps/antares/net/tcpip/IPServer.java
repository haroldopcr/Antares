package com.ingeniusapps.antares.net.tcpip;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author haroldop
 */
public class IPServer {

    private ServerSocket serverSocket = null;
    private IOException error = null;
    private final ArrayList<IPServerSocket> socketList = new ArrayList<>();

    public boolean startUp(int port) {
        this.error = null;

        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }

            this.serverSocket = new ServerSocket(port);

            return true;
        } catch (IOException ex) {
            this.error = ex;
            return false;
        }
    }

    public boolean shutdown() {
        this.error = null;

        try {
            socketList.forEach(socket -> {
                socket.close();
            });

            this.serverSocket.close();

            return true;
        } catch (IOException ex) {
            this.error = ex;
            return false;
        }
    }

    public IOException getError() {
        return this.error;
    }

    public IPServerSocket waittingForClients() {
        this.error = null;

        if (this.serverSocket == null) {
            return null;
        }

        try {
            this.socketList.add(new IPServerSocket(this.serverSocket.accept()));
            return this.socketList.get(this.socketList.size() - 1);
        } catch (IOException ex) {
            this.error = ex;
            return null;
        }
    }

    public void cancelAnyWaitting() {
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
        }
    }
}
