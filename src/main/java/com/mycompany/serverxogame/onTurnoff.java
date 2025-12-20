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


public class  onTurnoff  implements Runnable {

    private ServerSocket serverSocket;
    private volatile boolean isOn = true;
    private final int port = 5555;
    public   static Vector<ClientHandler> clientsVector = new Vector<>();

    public onTurnoff () {
        try {
            serverSocket = new ServerSocket(port);
             serverSocket.setSoTimeout(1000); 
        } catch (IOException ex) {
 
            ex.printStackTrace();
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
                continue;
            } catch (IOException ex) {
                                 if (isOn) {
                    ex.printStackTrace();
                }
                break;

             }
            
        }
        stopServer();
    }

    public void stopServer() {
        isOn = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            // Close all client connections
            for (ClientHandler client : clientsVector) {
                client.closeConnection();
            }
            clientsVector.clear();
            System.out.println("Server stopped.");
        } catch (IOException ex) {
                ex.printStackTrace();
        }
    }

    // Inner class to handle individual client communication
    class ClientHandler extends Thread {
        private Socket socket;
        // Add DataInputStream and PrintStream here

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Logic for reading/writing moves goes here
            } catch (Exception e) {
                System.out.println("Client disconnected.");
            } finally {
                closeConnection();
            }
        }

        public void closeConnection() {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientsVector.remove(this);
        }
    }
}