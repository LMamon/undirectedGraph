/*a program that allows the user to create an undirected graph that is
 visually displayed and to check whether the graph is connected and whether
 it has cycles in response to button clicks.
  Louis M. Project4/11Dec
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        GUI gui = new GUI();

        BorderPane root = new BorderPane();
        root.setCenter(gui.getGraphPane());
        root.setTop(gui.getEdgeControls());
        root.setBottom(gui.buttonAndMessage());

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Undirected Graph GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}