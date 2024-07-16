import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchByIdOrSsnPage {

    public static Scene getScene(Stage primaryStage) {
        Label empIdLabel = new Label("Employee ID:");
        TextField empIdField = new TextField();
        Label ssnLabel = new Label("SSN:");
        TextField ssnField = new TextField();
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");

        searchButton.setOnAction(e -> {
            String empIdText = empIdField.getText();
            String ssnText = ssnField.getText();

            if (empIdText.isEmpty() && ssnText.isEmpty()) {
                showAlert("Error", "Please enter either Employee ID or SSN.");
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                if (!empIdText.isEmpty() && !ssnText.isEmpty()) {
                    searchByEmpIdAndSsn(primaryStage, connection, empIdText, ssnText);
                } else if (!empIdText.isEmpty()) {
                    searchByEmpId(primaryStage, connection, empIdText);
                } else {
                    searchBySsn(primaryStage, connection, ssnText);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(SearchPage.getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(empIdLabel, empIdField, ssnLabel, ssnField, searchButton, backButton);

        return new Scene(layout, 300, 300);
    }

    private static void searchByEmpId(Stage primaryStage, Connection connection, String empIdText) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE emp_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(empIdText));
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            primaryStage.setScene(getEmployeeDetailsScene(primaryStage, resultSet));
        } else {
            showAlert("Error", "No employee found with Employee ID: " + empIdText);
        }
    }

    private static void searchBySsn(Stage primaryStage, Connection connection, String ssnText) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE ssn = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, ssnText);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            primaryStage.setScene(getEmployeeDetailsScene(primaryStage, resultSet));
        } else {
            showAlert("Error", "No employee found with SSN: " + ssnText);
        }
    }

    private static void searchByEmpIdAndSsn(Stage primaryStage, Connection connection, String empIdText, String ssnText) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE emp_id = ? AND ssn = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(empIdText));
        statement.setString(2, ssnText);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            primaryStage.setScene(getEmployeeDetailsScene(primaryStage, resultSet));
        } else {
            showAlert("Error", "No employee found with the given Employee ID and SSN.");
        }
    }

    private static Scene getEmployeeDetailsScene(Stage primaryStage, ResultSet resultSet) throws SQLException {
        Label empIdLabel = new Label("Employee ID: " + resultSet.getInt("emp_id"));
        Label nameLabel = new Label("Name: " + resultSet.getString("name"));
        Label ssnLabel = new Label("SSN: " + resultSet.getString("ssn"));
        Label salaryLabel = new Label("Salary: " + resultSet.getDouble("salary"));
        Label jobTitleLabel = new Label("Title: " + resultSet.getString("job_title"));
        Label divisionLabel = new Label("Division: " + resultSet.getString("division"));
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> primaryStage.setScene(getScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(empIdLabel, nameLabel, ssnLabel, salaryLabel, jobTitleLabel, divisionLabel, backButton);

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
