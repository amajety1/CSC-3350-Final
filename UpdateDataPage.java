import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateDataPage {

    public static Scene getScene(Stage primaryStage) {
        Button updateEmployeeButton = new Button("Update Employee Data");
        Button updateSalaryButton = new Button("Update Salary by Range");
        Button backButton = new Button("Back");

        updateEmployeeButton.setOnAction(e -> primaryStage.setScene(UpdateEmployeeDataPage.getScene(primaryStage)));
        updateSalaryButton.setOnAction(e -> primaryStage.setScene(UpdateSalaryPage.getScene(primaryStage)));
        backButton.setOnAction(e -> primaryStage.setScene(Main.getHomeScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(updateEmployeeButton, updateSalaryButton, backButton);

        return new Scene(layout, 300, 200);
    }
}
