import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddDataPage {

    public static Scene getScene(Stage primaryStage) {
        Button addColumnButton = new Button("Add Column");
        Button addEmployeeButton = new Button("Add New Employee");
        Button backButton = new Button("Back");

        addColumnButton.setOnAction(e -> primaryStage.setScene(AddColumnPage.getScene(primaryStage)));
        addEmployeeButton.setOnAction(e -> primaryStage.setScene(AddEmployeePage.getScene(primaryStage)));
        backButton.setOnAction(e -> primaryStage.setScene(Main.getHomeScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(addColumnButton, addEmployeeButton, backButton);

        return new Scene(layout, 300, 200);
    }
}
