/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;
public class OnTurnOff implements Runnable {

    private ServerSocket serverSocket;
    private volatile boolean isOn = false;
    private final int port = 5555;

    public static Vector<ClientHandler> clientsVector = new Vector<>();

    public boolean startServer() {
        if (isOn) return false;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);
            isOn = true;
            System.out.println("Server Started on port " + port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void run() {
        while (isOn) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clientsVector.add(handler);
                handler.start();

            } catch (SocketTimeoutException e) {
                // ignore
            } catch (IOException e) {
                if (isOn) e.printStackTrace();
                break;
            }
        }
    }

    public synchronized void stopServer() {
        if (!isOn) return;

        isOn = false;
        try {
            for (ClientHandler client : clientsVector) {
                client.closeConnection();
            }
            clientsVector.clear();

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            System.out.println("Server stopped.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
