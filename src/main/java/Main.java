import com.equilibriummusicgroup.AMCharts.model.ChartsModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumChartsGui.fxml"));
            BorderPane root = (BorderPane)loader.load();
            AlbumChartsController controller = loader.getController();
            ChartsModel chartsModel = new ChartsModel();
            controller.setModel(chartsModel) ;
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}