package com.mycompany.serverxogame;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/serverxogame/server_run.fxml"));
        //F:\traning\iti\Java\Final Project\ServerXOGame\src\main\resources/com/mycompany/serverxogame/server_run.fxml
        Scene scene = new Scene(root, 650, 650);
        primaryStage.setTitle("My Server");
        primaryStage.setScene(scene);
        
         primaryStage.setOnCloseRequest(event -> {
        System.out.println("Window closing...");
        if (Server_runController.getServer() != null) {
    Server_runController.getServer().stopServer();
}
        Platform.exit();
        System.exit(0);
    });

 
        primaryStage.show();
    }

    public static void main(String[] args) {
 
        launch(args);
     }
    
  
}
