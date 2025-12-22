module com.mycompany.serverxogame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires derbyclient;
    requires org.json;
    opens com.mycompany.serverxogame to javafx.fxml;
    exports com.mycompany.serverxogame;
}
