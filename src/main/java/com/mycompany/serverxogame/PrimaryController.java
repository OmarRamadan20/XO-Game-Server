package com.mycompany.serverxogame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

public class PrimaryController implements Initializable {

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PieChart.Data slice1 = new PieChart.Data("OnGame Players", 35);
        PieChart.Data slice2 = new PieChart.Data("Available Players", 40);
        PieChart.Data slice3 = new PieChart.Data("Offline Players", 25);

        pieChart.setData(FXCollections.observableArrayList(slice1, slice2, slice3));

         Platform.runLater(() -> {
            slice1.getNode().setStyle("-fx-pie-color: #FF6347; -fx-cursor: hand;");
            slice2.getNode().setStyle("-fx-pie-color: #90EE90; -fx-cursor: hand;");
            slice3.getNode().setStyle("-fx-pie-color: #1E90FF; -fx-cursor: hand;");

            for (PieChart.Data data : pieChart.getData()) {
                Tooltip tooltip = new Tooltip(data.getName() + ": " + (int)data.getPieValue());
                Tooltip.install(data.getNode(), tooltip);

                 data.getNode().setOnMouseEntered(e -> data.getNode().setScaleX(1.1));
                data.getNode().setOnMouseExited(e -> data.getNode().setScaleX(1.0));
            }
        });
    }
}
