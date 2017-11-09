/**
 * Sample Skeleton for 'SongsChartsGui.fxml' Controller Class
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
        import javafx.scene.control.ChoiceBox;
        import javafx.scene.control.Label;
        import javafx.scene.control.ProgressBar;
        import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SongsChartsController {

    @FXML
    private ChartsModel chartsModel;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="countryChoiceBox"
    private ChoiceBox<?> countryChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="genreChoiceBox"
    private ChoiceBox<?> genreChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="getButton"
    private Button getButton; // Value injected by FXMLLoader

    @FXML // fx:id="resetButton"
    private Button resetButton; // Value injected by FXMLLoader

    @FXML // fx:id="albumsTextArea"
    private TextArea albumsTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="progressBar"
    private ProgressBar progressBar; // Value injected by FXMLLoader

    @FXML // fx:id="statusLabel"
    private Label statusLabel; // Value injected by FXMLLoader

    @FXML // fx:id="changeSceneButton"
    private Button changeSceneButton; // Value injected by FXMLLoader

    @FXML
    void aboutItemAction(ActionEvent event) {

    }

    @FXML
    void changeSceneButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbumChartsGui.fxml")) ;

        Parent root = loader.load();

        AlbumChartsController controller = loader.getController() ;
        controller.setModel(this.chartsModel) ;

        Stage stage = (Stage) changeSceneButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void getButtonAction(ActionEvent event) {

    }

    @FXML
    void resetAction(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert countryChoiceBox != null : "fx:id=\"countryChoiceBox\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert genreChoiceBox != null : "fx:id=\"genreChoiceBox\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert getButton != null : "fx:id=\"getButton\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert albumsTextArea != null : "fx:id=\"albumsTextArea\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert changeSceneButton != null : "fx:id=\"changeSceneButton\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";

    }

    public void setModel(ChartsModel model) {
        this.chartsModel = model;
    }
}

