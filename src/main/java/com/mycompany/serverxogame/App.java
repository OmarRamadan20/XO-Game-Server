package com.mycompany.serverxogame;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/serverxogame/server_run.fxml"));
        //F:\traning\iti\Java\Final Project\ServerXOGame\src\main\resources/com/mycompany/serverxogame/server_run.fxml
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Server!!!!!!!!!!!!!!!!!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
 
        launch(args);
     }
}
