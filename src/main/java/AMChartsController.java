/**
 * Sample Skeleton for 'AMChartsGui.fxml' Controller Class
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.equilibriummusicgroup.AMCharts.model.ChartsModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AMChartsController {

    @FXML
    private ChartsModel chartsModel;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="albumButton"
    private Button albumButton; // Value injected by FXMLLoader

    @FXML // fx:id="songsButton"
    private Button songsButton; // Value injected by FXMLLoader

    @FXML // fx:id="keywordsButton"
    private Button keywordsButton; // Value injected by FXMLLoader

    public void setModel(ChartsModel model) {
        this.chartsModel = model;
    }

    @FXML
    void ChangeAlbumScene(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumChartsGui.fxml"));

        Parent root = loader.load();
        AlbumChartsController controller = loader.getController();
        controller.setModel(this.chartsModel);


        Stage stage = (Stage) albumButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void aboutItemAction(ActionEvent event) {

    }

    @FXML
    void changeKeywordsButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("KeywordsChartsGui.fxml"));

        Parent root = loader.load();
        KeywordsChartsController controller = loader.getController();
        controller.setModel(this.chartsModel);


        Stage stage = (Stage) albumButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    void changeSongsScene(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SongsChartsGui.fxml"));

        Parent root = loader.load();
        SongsChartsController controller = loader.getController();
        controller.setModel(this.chartsModel);


        Stage stage = (Stage) songsButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert albumButton != null : "fx:id=\"albumButton\" was not injected: check your FXML file 'AMChartsGui.fxml'.";
        assert songsButton != null : "fx:id=\"songsButton\" was not injected: check your FXML file 'AMChartsGui.fxml'.";
        assert keywordsButton != null : "fx:id=\"keywordsButton\" was not injected: check your FXML file 'AMChartsGui.fxml'.";

    }
}
