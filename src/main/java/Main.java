import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The AMCharts program implements an application that
 * retrieves the top charts from the Apple Music API.
 *
 * @author  lunax28
 * @version 1.0
 * @since   2017-09-07
 */

public class Main extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("AMChartsGui.fxml"));
        window.setTitle("Apple Music Charts");
        window.setScene(new Scene(root, 640, 382));
        window.setResizable(false);
        window.show();
    }

    public Stage getWindow() {
        return window;
    }
}