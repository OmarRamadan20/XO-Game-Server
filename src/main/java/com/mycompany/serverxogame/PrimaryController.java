package com.mycompany.serverxogame;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

public class PrimaryController implements Initializable {

    @FXML
    private PieChart pieChart;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnRefresh;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPieChart();
    }

    
    private void loadPieChart() {
        try {
            int[] counts = DAO.getPlayersStateCounts();
            System.out.println(Arrays.toString(counts));

            boolean allZero = counts[0] == 0 && counts[1] == 0 && counts[2] == 0;

            PieChart.Data slice1;
            PieChart.Data slice2;
            PieChart.Data slice3;

            if (allZero) {
                slice1 = new PieChart.Data("OnGame Players (0)", 1);
                slice2 = new PieChart.Data("Available Players (0)", 1);
                slice3 = new PieChart.Data("Offline Players (0)", 1);
            } else {
                slice1 = new PieChart.Data(
                        "OnGame Players (" + counts[0] + ")",
                        counts[0] == 0 ? 0.0001 : counts[0]
                );
                slice2 = new PieChart.Data(
                        "Available Players (" + counts[1] + ")",
                        counts[1] == 0 ? 0.0001 : counts[1]
                );
                slice3 = new PieChart.Data(
                        "Offline Players (" + counts[2] + ")",
                        counts[2] == 0 ? 0.0001 : counts[2]
                );
            }

            pieChart.setData(FXCollections.observableArrayList(
                    slice1, slice2, slice3
            ));

            Platform.runLater(() -> {
                slice1.getNode().setStyle("-fx-pie-color: #FF6347;");
                slice2.getNode().setStyle("-fx-pie-color: #90EE90;");
                slice3.getNode().setStyle("-fx-pie-color: #1E90FF;");

                PieChart.Data[] slices = {slice1, slice2, slice3};

                for (int i = 0; i < slices.length; i++) {
                    PieChart.Data data = slices[i];

                    Tooltip tooltip = new Tooltip(
                            data.getName().replaceAll("\\(.*\\)", "") + ": " + counts[i]
                    );
                    Tooltip.install(data.getNode(), tooltip);

                    data.getNode().setOnMouseEntered(e -> data.getNode().setScaleX(1.1));
                    data.getNode().setOnMouseExited(e -> data.getNode().setScaleX(1.0));
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onActionBack(ActionEvent event) {
        NavigationBetweenScreens.backToServer(event);
    }

    @FXML
    private void onActionRefresh(ActionEvent event) {
        loadPieChart(); 
    }
}
