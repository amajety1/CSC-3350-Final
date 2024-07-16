import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateSalaryPage {

    public static Scene getScene(Stage primaryStage) {
        Label minSalaryLabel = new Label("Minimum Salary:");
        TextField minSalaryField = new TextField();
        Label maxSalaryLabel = new Label("Maximum Salary:");
        TextField maxSalaryField = new TextField();
        Label percentageIncreaseLabel = new Label("Percentage Increase:");
        TextField percentageIncreaseField = new TextField();
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            double minSalary = Double.parseDouble(minSalaryField.getText());
            double maxSalary = Double.parseDouble(maxSalaryField.getText());
            double percentageIncrease = Double.parseDouble(percentageIncreaseField.getText());

            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "UPDATE Employees SET salary = salary + (salary * ? / 100) WHERE salary >= ? AND salary < ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setDouble(1, percentageIncrease);
                statement.setDouble(2, minSalary);
                statement.setDouble(3, maxSalary);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    primaryStage.setScene(getSuccessScene(primaryStage));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(UpdateDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(minSalaryLabel, minSalaryField, maxSalaryLabel, maxSalaryField, percentageIncreaseLabel, percentageIncreaseField, submitButton, backButton);

        return new Scene(layout, 300, 300);
    }

    private static Scene getSuccessScene(Stage primaryStage) {
        Label successLabel = new Label("Salaries updated successfully!");
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(UpdateDataPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(successLabel, backButton);

        return new Scene(layout, 300, 200);
    }
}
