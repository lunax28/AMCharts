/**
 * Sample Skeleton for 'KeywordsChartsGui.fxml' Controller Class
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import com.equilibriummusicgroup.AMCharts.model.ChartsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class KeywordsChartsController {

    @FXML
    private ChartsModel chartsModel;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="countryChoiceBox"
    private ChoiceBox<String> countryChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="keywordTextField"
    private TextField keywordTextField; // Value injected by FXMLLoader

    @FXML // fx:id="getHintsButton"
    private Button getHintsButton; // Value injected by FXMLLoader

    @FXML // fx:id="resetButton"
    private Button resetButton; // Value injected by FXMLLoader

    @FXML // fx:id="hintsTextArea"
    private TextArea hintsTextArea; // Value injected by FXMLLoader

    @FXML // fx:id="progressBar"
    private ProgressBar progressBar; // Value injected by FXMLLoader

    @FXML // fx:id="statusLabel"
    private Label statusLabel; // Value injected by FXMLLoader

    @FXML // fx:id="changeSceneButton"
    private Button changeSceneButton; // Value injected by FXMLLoader

    @FXML
    private ObservableList<String> countryObsList = FXCollections.observableArrayList("de", "es", "fr", "it", "jp", "pt", "us", "at","cn","br","ro");



    public void setModel(ChartsModel model) {
        this.chartsModel = model;
    }

    @FXML
    void aboutItemAction(ActionEvent event) {

    }

    @FXML
    void changeSceneButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AMChartsGui.fxml"));

        Parent root = loader.load();

        AMChartsController controller = loader.getController();
        controller.setModel(this.chartsModel);

        Stage stage = (Stage) changeSceneButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void getButtonAction(ActionEvent event) throws InvalidKeySpecException, NoSuchAlgorithmException, MalformedURLException, URISyntaxException {

        StringBuilder result = new StringBuilder();

        if(this.keywordTextField.getText().isEmpty()){
            displayErrorMessage("Make sure to insert a keyword!");
            return;
        }

        String countryValue = this.countryChoiceBox.getValue();

        String kewyword = this.keywordTextField.getText();
        String keywordSanitized = kewyword.replaceAll(" ", "+");

        System.out.println("keywordSanitized: " + keywordSanitized);


        String link = linkBuilder(keywordSanitized);

        if (link == null) {
            return;
        }

        result = chartsModel.getKeywordsHints(link);
        this.hintsTextArea.setText(result.toString());


    }

    private String linkBuilder(String keywordSanitized) throws URISyntaxException, MalformedURLException {
        //https://api.music.apple.com/v1/catalog/us/search/hints?term=love&limit=10
        StringBuilder link = new StringBuilder("https://api.music.apple.com/v1/catalog/"); //us/charts?types=albums&genre=1127&limit=50

        link.append(this.countryChoiceBox.getValue());
        link.append("/search/hints?term=");
        link.append(keywordSanitized);
        link.append("&limit=50");

        System.out.println("LINK IS: " + link.toString());

        String xstr = "维也纳恩斯特哈佩尔球场" ;

        URI uri = new URI(link.toString());
        URL url = new URL(uri.toASCIIString());

        System.out.println("URL: " + url.toString());


        return url.toString();
    }

    @FXML
    void resetAction(ActionEvent event) {

        this.keywordTextField.clear();
        this.hintsTextArea.clear();

    }

    public void displayErrorMessage(String textMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();
        return;

    }

    private void displayExceptionDialog(Throwable ex, String exceptionMessage) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Exception");
        alert.setContentText(exceptionMessage);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert countryChoiceBox != null : "fx:id=\"countryChoiceBox\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";
        assert keywordTextField != null : "fx:id=\"keywordTextField\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";
        assert getHintsButton != null : "fx:id=\"getHintsButton\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";
        assert hintsTextArea != null : "fx:id=\"hintsTextArea\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";
        assert changeSceneButton != null : "fx:id=\"changeSceneButton\" was not injected: check your FXML file 'KeywordsChartsGui.fxml'.";

        this.countryChoiceBox.setValue("us");
        this.countryChoiceBox.setItems(this.countryObsList);
    }
}
