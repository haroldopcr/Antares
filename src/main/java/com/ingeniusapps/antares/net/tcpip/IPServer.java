package com.ingeniusapps.antares.net.tcpip;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Implementa un servidor TCP/IP básico para aceptar conexiones entrantes y
 * administrarlas mediante instancias de {@link IPServerSocket}.
 *
 * <p>La clase encapsula el ciclo principal de un {@link ServerSocket}, permitiendo
 * iniciar el servidor, esperar clientes, detener el servicio y cancelar esperas
 * bloqueantes activas. Además, conserva una lista interna de sockets aceptados
 * para facilitar su cierre durante el apagado del servidor.</p>
 *
 * <p>La instancia mantiene la referencia al último error de tipo
 * {@link IOException} producido durante las operaciones de red.</p>
 */
public class IPServer {

    /**
     * Socket de servidor utilizado para aceptar conexiones entrantes.
     */
    private ServerSocket serverSocket = null;

    /**
     * Último error de entrada/salida registrado por la instancia.
     */
    private IOException error = null;

    /**
     * Lista interna de conexiones aceptadas por el servidor.
     */
    private final ArrayList<IPServerSocket> socketList = new ArrayList<>();

    /**
     * Inicia el servidor TCP/IP en el puerto indicado.
     *
     * <p>Si ya existe un {@link ServerSocket} previamente asociado, se intenta
     * cerrar antes de crear una nueva instancia enlazada al nuevo puerto.</p>
     *
     * @param port puerto en el que el servidor quedará a la escucha
     * @return {@code true} si el servidor se inició correctamente;
     *         en caso contrario, {@code false}
     */
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

    /**
     * Detiene el servidor y cierra todas las conexiones aceptadas registradas
     * internamente.
     *
     * <p>Primero se solicita el cierre de todos los {@link IPServerSocket}
     * almacenados y posteriormente se cierra el {@link ServerSocket} principal.</p>
     *
     * @return {@code true} si el proceso de apagado se completó correctamente;
     *         en caso contrario, {@code false}
     */
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

    /**
     * Obtiene el último error de entrada/salida registrado por la instancia.
     *
     * @return excepción registrada o {@code null} si no existe error
     */
    public IOException getError() {
        return this.error;
    }

    /**
     * Queda a la espera de una nueva conexión cliente y la encapsula en un
     * {@link IPServerSocket}.
     *
     * <p>Si el servidor no ha sido iniciado, el método retorna {@code null}.
     * Cuando una conexión es aceptada correctamente, se agrega a la lista interna
     * de conexiones y se retorna la última conexión agregada.</p>
     *
     * @return conexión aceptada encapsulada en un {@link IPServerSocket},
     *         o {@code null} si ocurre un error o si el servidor no está activo
     */
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

    /**
     * Cancela cualquier operación bloqueante de espera sobre el socket del servidor.
     *
     * <p>Este método intenta cerrar el {@link ServerSocket} actual, lo cual
     * normalmente interrumpe una llamada bloqueante a aceptación de clientes.</p>
     */
    public void cancelAnyWaitting() {
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
        }
    }
}