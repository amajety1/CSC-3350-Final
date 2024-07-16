import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddEmployeePage {

    public static Scene getScene(Stage primaryStage) {
        VBox layout = new VBox(10);
        Map<String, TextField> textFields = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "Employees", null);

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                Label label = new Label(columnName + ":");
                TextField textField = new TextField();
                textFields.put(columnName, textField);
                layout.getChildren().addAll(label, textField);
            }

            columns.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            try (Connection connection = DatabaseConnection.getConnection()) {
                StringBuilder sql = new StringBuilder("INSERT INTO Employees (");
                StringBuilder values = new StringBuilder("VALUES (");
                textFields.forEach((columnName, textField) -> {
                    sql.append(columnName).append(", ");
                    values.append("?, ");
                });

                sql.setLength(sql.length() - 2); // Remove trailing comma and space
                values.setLength(values.length() - 2); // Remove trailing comma and space

                sql.append(") ").append(values).append(")");

                PreparedStatement statement = connection.prepareStatement(sql.toString());
                int index = 1;
                for (TextField textField : textFields.values()) {
                    statement.setString(index++, textField.getText());
                }

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    primaryStage.setScene(getSuccessScene(primaryStage));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(AddDataPage.getScene(primaryStage)));

        layout.getChildren().addAll(submitButton, backButton);

        return new Scene(layout, 300, 300);
    }

    private static Scene getSuccessScene(Stage primaryStage) {
        Label successLabel = new Label("A new employee was inserted successfully!");
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(AddDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(successLabel, backButton);

        return new Scene(layout, 300, 200);
    }
}
