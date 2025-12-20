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
import javafx.scene.layout.AnchorPane;
/**
 * FXML Controller class
 *
 * @author user
 */
public class Server_runController implements Initializable {


    @FXML
    private Button ServerStateButton;
    @FXML
    private Button PlayerStatusButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void runTheServer(ActionEvent event) {
    }

    @FXML
    private void showPlayerStatus(ActionEvent event) {
        NavigationBetweenScreens.goToPieChart(event);
    }

}
