import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchBySalaryPage {

    public static Scene getScene(Stage primaryStage) {
        Label minSalaryLabel = new Label("Minimum Salary:");
        TextField minSalaryField = new TextField();
        Label maxSalaryLabel = new Label("Maximum Salary:");
        TextField maxSalaryField = new TextField();
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");

        searchButton.setOnAction(e -> {
            String minSalaryText = minSalaryField.getText();
            String maxSalaryText = maxSalaryField.getText();

            if (minSalaryText.isEmpty() || maxSalaryText.isEmpty()) {
                showAlert("Error", "Please enter both minimum and maximum salary.");
                return;
            }

            double minSalary = Double.parseDouble(minSalaryText);
            double maxSalary = Double.parseDouble(maxSalaryText);

            try (Connection connection = DatabaseConnection.getConnection()) {
                List<String> columns = getColumns(connection);

                String sql = "SELECT * FROM Employees WHERE salary >= ? AND salary < ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setDouble(1, minSalary);
                statement.setDouble(2, maxSalary);
                ResultSet resultSet = statement.executeQuery();

                StringBuilder results = new StringBuilder();
                while (resultSet.next()) {
                    for (String column : columns) {
                        results.append(column).append(": ").append(resultSet.getString(column)).append("\n");
                    }
                    results.append("----------------------------------------\n");
                }

                primaryStage.setScene(getResultsScene(primaryStage, results.toString()));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(SearchPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(minSalaryLabel, minSalaryField, maxSalaryLabel, maxSalaryField, searchButton, backButton);

        return new Scene(layout, 300, 300);
    }

    private static List<String> getColumns(Connection connection) throws SQLException {
        List<String> columns = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(null, null, "Employees", null);

        while (resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");
            columns.add(columnName);
        }

        return columns;
    }

    private static Scene getResultsScene(Stage primaryStage, String results) {
        Label resultsLabel = new Label("Results:");
        TextArea resultsArea = new TextArea(results);
        resultsArea.setEditable(false);
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(resultsLabel, resultsArea, backButton);

        return new Scene(layout, 300, 300);
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
