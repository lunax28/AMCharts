import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Controller {

    @FXML
    private TextArea chartsTextArea;

    @FXML
    private JsonQueryUtils apiQuery;

    @FXML
    private Label sourceLabel;

    @FXML
    private Button outputButton;

    @FXML
    private Button getButton;

    @FXML
    private File sourceFolderPath;

    @FXML
    private ChoiceBox<String> countryChoiceBox;

    ObservableList<String> countryObsList = FXCollections.observableArrayList("us","es","de","it","pt");

    @FXML
    private ChoiceBox<String> genreChoiceBox;
    ObservableList<String> genreObsList = FXCollections.observableArrayList("13","12","11","10");


    public Controller(){

        this.apiQuery  = new JsonQueryUtils();

    }

    @FXML
    private void initialize(){

        this.countryChoiceBox.setValue("us");
        this.genreChoiceBox.setValue("13");
        this.countryChoiceBox.setItems(this.countryObsList);
        this.genreChoiceBox.setItems(this.genreObsList);


    }


    @FXML
    public void outputButtonAction(){


        System.out.println("IT WORKS!");
    }

    @FXML
    public void getButtonAction() throws InvalidKeySpecException, NoSuchAlgorithmException {

        StringBuilder link = new StringBuilder("https://api.music.apple.com/v1/catalog/"); //us/charts?types=albums&genre=1127&limit=50

        link.append(this.countryChoiceBox.getValue());
        link.append("/charts?types=albums&genre=");
        link.append(this.genreChoiceBox.getValue());
        link.append("&limit=10");

        System.out.println("LINK IS: " + link.toString());


        JsonObject jsonResponse = apiQuery.getJson(link.toString());
        System.out.println("JSON RESPONSE: " + jsonResponse.toString());


        JsonObject results = jsonResponse.get("results").getAsJsonObject();

        JsonArray albums = results.get("albums").getAsJsonArray();

        JsonObject data = albums.get(0).getAsJsonObject();

        JsonArray dataArray = data.get("data").getAsJsonArray();

        int sizeDataArray = dataArray.size();
        System.out.println("size data array: " + sizeDataArray);

        StringBuilder result = new StringBuilder();


        for(int i = 0; i < sizeDataArray; i++){

            JsonObject firstResult = dataArray.get(i).getAsJsonObject();

            JsonObject attributes = firstResult.get("attributes").getAsJsonObject();

            String albumName = attributes.get("name").getAsString();

            String artistName = attributes.get("artistName").getAsString();
            //String id = attributes.get("id").getAsString();
            String url = attributes.get("url").getAsString();
            int trackCount = attributes.get("trackCount").getAsInt();
            String releaseDate = attributes.get("releaseDate").getAsString();
            String recordLabel = attributes.get("recordLabel").getAsString();
            String copyright = attributes.get("copyright").getAsString();


            result.append(i+1+"; ")
                    .append(albumName)
                    .append("; ")
                    .append(artistName)
                    .append("; ")
                    .append(url)
                    .append("; ")
                    .append(trackCount)
                    .append("; ")
                    .append(releaseDate)
                    .append("; ")
                    .append(recordLabel)
                    .append("; ")
                    .append(recordLabel)
                    .append("\n");


        }
        this.chartsTextArea.setText(result.toString());


        System.out.println("IT WORKS!");
    }

    @FXML
    public void locateFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        this.sourceFolderPath = fileChooser.showOpenDialog(new Stage());

        if(this.sourceFolderPath != null){
            this.sourceLabel.setText(this.sourceFolderPath.getAbsolutePath().toString());
        }

    }

}