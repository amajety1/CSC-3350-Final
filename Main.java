import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(getHomeScene(primaryStage));
        primaryStage.show();
    }

    public static Scene getHomeScene(Stage primaryStage) {
        // Create buttons for navigation
        Button addButton = new Button("Add Data");
        Button updateButton = new Button("Update Data");
        Button deleteButton = new Button("Delete Data");
        Button searchButton = new Button("Search");

        // Event handlers for buttons to navigate to different pages
        addButton.setOnAction(e -> primaryStage.setScene(AddDataPage.getScene(primaryStage)));
        updateButton.setOnAction(e -> primaryStage.setScene(UpdateDataPage.getScene(primaryStage)));
        deleteButton.setOnAction(e -> primaryStage.setScene(DeleteDataPage.getScene(primaryStage)));
        searchButton.setOnAction(e -> primaryStage.setScene(SearchPage.getScene(primaryStage)));

        // Layout for the home page
        VBox layout = new VBox(10);
        layout.getChildren().addAll(addButton, updateButton, deleteButton, searchButton);

        // Create the home scene
        return new Scene(layout, 300, 200);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
