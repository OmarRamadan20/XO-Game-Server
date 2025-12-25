/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.serverxogame;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
public class Server_runController implements Initializable {

    private static OnTurnOff server; 
    private static Thread serverThread;

    @FXML
    private Button ServerStateButton;
    @FXML
    private Button PlayerStatusButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ServerStateButton.setText(server == null ? "Start Server" : "Stop Server");
    }

    @FXML
    private void runTheServer(ActionEvent event) {

        if (server != null) {
            server.stopServer();
            server = null;
            serverThread = null;
            ServerStateButton.setText("Start Server");
            System.out.println("Server stopped by user.");
            return;
        }

        server = new OnTurnOff();
        if (server.startServer()) {
            serverThread = new Thread(server);
            serverThread.setDaemon(true);
            serverThread.start();
            ServerStateButton.setText("Stop Server");
            System.out.println("Server started by user.");
        } else {
            server = null;
        }
    }

    @FXML
    private void showPlayerStatus(ActionEvent event) {
            NavigationBetweenScreens.goToPieChart(event);

    
    }
    
    public static OnTurnOff getServer() {
    return server;
}

}
