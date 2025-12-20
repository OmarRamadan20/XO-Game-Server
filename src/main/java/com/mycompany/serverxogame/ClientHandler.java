/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.serverxogame;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author user
 */
class ClientHandler extends Thread {

    private static Vector<onTurnoff.ClientHandler> clientsVector = new Vector<>();
    private Socket socket;
   

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
         
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            closeConnection();
        }
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientsVector.remove(this);
    }
}
