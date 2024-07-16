import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteDataPage {

    public static Scene getScene(Stage primaryStage) {
        Label empIdLabel = new Label("Employee ID:");
        TextField empIdField = new TextField();
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            int empId = Integer.parseInt(empIdField.getText());
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM Employees WHERE emp_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, empId);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee deleted successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Employee ID not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee.");
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(Main.getHomeScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(empIdLabel, empIdField, submitButton, backButton);

        return new Scene(layout, 300, 200);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
