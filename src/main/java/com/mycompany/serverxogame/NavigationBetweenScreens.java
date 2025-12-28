package com.mycompany.serverxogame;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;  
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class NavigationBetweenScreens {

    private static void changeScene(ActionEvent event, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(NavigationBetweenScreens.class.getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            
             try {
                stage.getIcons().clear();
                stage.getIcons().add(new Image(NavigationBetweenScreens.class.getResourceAsStream("/com/mycompany/serverxogame/logo.png")));
            } catch (Exception e) {
                System.err.println("Could not load icon in Navigation: " + e.getMessage());
            }
 
            stage.show();
        } catch (IOException ex) {
             java.util.logging.Logger.getLogger(NavigationBetweenScreens.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public static void goToPieChart(ActionEvent event) {
        changeScene(event, "/com/mycompany/serverxogame/primary.fxml", "Player Status");
    }

    public static void backToServer(ActionEvent event) {
        changeScene(event, "/com/mycompany/serverxogame/server_run.fxml", "Server Control");
    }
}