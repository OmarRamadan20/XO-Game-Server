module com.mycompany.serverxogame {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.serverxogame to javafx.fxml;
    exports com.mycompany.serverxogame;
}
