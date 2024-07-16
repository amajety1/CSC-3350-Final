import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowAllEmployeesPage {

    public static Scene getScene(Stage primaryStage) {
        VBox layout = new VBox(10);
        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(SearchPage.getScene(primaryStage)));

        layout.getChildren().addAll(new Label("All Employees:"), resultsArea, backButton);

        try (Connection connection = DatabaseConnection.getConnection()) {
            List<String> columns = getColumns(connection);

            String sql = "SELECT * FROM Employees";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            StringBuilder results = new StringBuilder();
            while (resultSet.next()) {
                for (String column : columns) {
                    results.append(column).append(": ").append(resultSet.getString(column)).append("\n");
                }
                results.append("----------------------------------------\n");
            }

            resultsArea.setText(results.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to retrieve employee data.");
        }

        return new Scene(layout, 400, 400);
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

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
