import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddColumnPage {

    public static Scene getScene(Stage primaryStage) {
        Label columnNameLabel = new Label("Column Name:");
        TextField columnNameField = new TextField();
        Label columnValuesLabel = new Label("Column Values (enter as comma-separated list):");
        TextField columnValuesField = new TextField();
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            String columnName = columnNameField.getText();
            String columnValues = columnValuesField.getText();

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Add the new column
                String addColumnSql = "ALTER TABLE Employees ADD COLUMN " + columnName + " VARCHAR(100)";
                PreparedStatement addColumnStatement = connection.prepareStatement(addColumnSql);
                addColumnStatement.executeUpdate();

                // Update the new column with values
                String[] values = columnValues.split(",");
                for (int i = 0; i < values.length; i++) {
                    String updateValueSql = "UPDATE Employees SET " + columnName + " = ? WHERE emp_id = ?";
                    PreparedStatement updateValueStatement = connection.prepareStatement(updateValueSql);
                    updateValueStatement.setString(1, values[i].trim());
                    updateValueStatement.setInt(2, i + 1); // Assuming emp_id starts from 1 to 20
                    updateValueStatement.executeUpdate();
                }

                primaryStage.setScene(getSuccessScene(primaryStage));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(AddDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(columnNameLabel, columnNameField, columnValuesLabel, columnValuesField, submitButton, backButton);

        return new Scene(layout, 300, 300);
    }

    private static Scene getSuccessScene(Stage primaryStage) {
        Label successLabel = new Label("Column added successfully!");
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(AddDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(successLabel, backButton);

        return new Scene(layout, 300, 200);
    }
}
