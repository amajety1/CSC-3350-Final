import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UpdateEmployeeDataPage {

    public static Scene getScene(Stage primaryStage) {
        Label empIdLabel = new Label("Employee ID:");
        TextField empIdField = new TextField();
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            int empId = Integer.parseInt(empIdField.getText());
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "SELECT * FROM Employees WHERE emp_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, empId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // If Employee ID exists, navigate to the update details page
                    primaryStage.setScene(getUpdateDetailsScene(primaryStage, empId));
                } else {
                    // If Employee ID doesn't exist, show invalid ID message
                    primaryStage.setScene(getInvalidIdScene(primaryStage));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(UpdateDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(empIdLabel, empIdField, submitButton, backButton);

        return new Scene(layout, 300, 200);
    }

    private static Scene getUpdateDetailsScene(Stage primaryStage, int empId) {
        VBox layout = new VBox(10);
        Map<String, TextField> textFields = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "Employees", null);

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if (!columnName.equals("emp_id")) {
                    Label label = new Label(columnName + ":");
                    TextField textField = new TextField();
                    textFields.put(columnName, textField);
                    layout.getChildren().addAll(label, textField);
                }
            }

            // Populate fields with existing data
            String sql = "SELECT * FROM Employees WHERE emp_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, empId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                for (Map.Entry<String, TextField> entry : textFields.entrySet()) {
                    String columnName = entry.getKey();
                    TextField textField = entry.getValue();
                    textField.setText(resultSet.getString(columnName));
                }
            }

            columns.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            try (Connection connection = DatabaseConnection.getConnection()) {
                StringBuilder sql = new StringBuilder("UPDATE Employees SET ");
                textFields.forEach((columnName, textField) -> {
                    sql.append(columnName).append(" = ?, ");
                });
                sql.setLength(sql.length() - 2); // Remove trailing comma and space
                sql.append(" WHERE emp_id = ?");

                PreparedStatement statement = connection.prepareStatement(sql.toString());
                int index = 1;
                for (TextField textField : textFields.values()) {
                    statement.setString(index++, textField.getText());
                }
                statement.setInt(index, empId);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    primaryStage.setScene(getSuccessScene(primaryStage));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(UpdateDataPage.getScene(primaryStage)));

        layout.getChildren().addAll(submitButton, backButton);

        return new Scene(layout, 300, 300);
    }

    private static Scene getInvalidIdScene(Stage primaryStage) {
        Label invalidIdLabel = new Label("Invalid Employee ID!");
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(UpdateEmployeeDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(invalidIdLabel, backButton);

        return new Scene(layout, 300, 200);
    }

    private static Scene getSuccessScene(Stage primaryStage) {
        Label successLabel = new Label("Employee data updated successfully!");
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(UpdateEmployeeDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(successLabel, backButton);

        return new Scene(layout, 300, 200);
    }
}
