import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchPage {

    public static Scene getScene(Stage primaryStage) {
        Button searchByIdOrSsnButton = new Button("Search by Employee ID or SSN");
        Button searchBySalaryButton = new Button("Search by Salary Range");
        Button showAllButton = new Button("Show All Employees");
        Button backButton = new Button("Back");

        searchByIdOrSsnButton.setOnAction(e -> primaryStage.setScene(SearchByIdOrSsnPage.getScene(primaryStage)));
        searchBySalaryButton.setOnAction(e -> primaryStage.setScene(SearchBySalaryPage.getScene(primaryStage)));
        showAllButton.setOnAction(e -> primaryStage.setScene(ShowAllEmployeesPage.getScene(primaryStage)));
        backButton.setOnAction(e -> primaryStage.setScene(Main.getHomeScene(primaryStage)));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(searchByIdOrSsnButton, searchBySalaryButton, showAllButton, backButton);

        return new Scene(layout, 300, 200);
    }
}
