package com.mycompany.serverxogame;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
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
        setupChartUI();
        loadPieChart();
    }

    private void setupChartUI() {

        pieChart.setLabelsVisible(false);

        pieChart.setLegendSide(Side.BOTTOM);

        pieChart.setAnimated(true);
    }

    private void loadPieChart() {
        try {
            int[] counts = DAO.getPlayersStateCounts();
            boolean allZero = counts[0] == 0 && counts[1] == 0 && counts[2] == 0;
        
            PieChart.Data slice1, slice2, slice3;
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            if (counts[0] > 0) {
                pieChartData.add(new PieChart.Data("OnGame ", counts[0]));
            }
            if (counts[1] > 0) {
                pieChartData.add(new PieChart.Data("Available ", counts[1]));
            }
            if (counts[2] > 0) {
                pieChartData.add(new PieChart.Data("Offline ", counts[2]));
            }

            if (pieChartData.isEmpty()) {
                PieChart.Data noData = new PieChart.Data("No Players", 1);
                pieChartData.add(noData);
                pieChart.setData(pieChartData);
                noData.getNode().setStyle("-fx-pie-color: #bdc3c7;");
                
            } else {
                pieChart.setData(pieChartData);
            }

              if(!allZero)
               {
            for (PieChart.Data data : pieChart.getData()) {
                Node node = data.getNode();
             
                   Tooltip.install(node, new Tooltip(data.getName() + ": " + (int) data.getPieValue()));
                node.setOnMouseEntered(e -> {
                    node.setScaleX(1.1); // 
                    node.setScaleY(1.1);
                    node.setEffect(new javafx.scene.effect.DropShadow(15, javafx.scene.paint.Color.BLACK));
                    node.toFront();
                });

                node.setOnMouseExited(e -> {
                    node.setScaleX(1.0);
                    node.setScaleY(1.0);
                    node.setEffect(null);
                });
               }
               }

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
