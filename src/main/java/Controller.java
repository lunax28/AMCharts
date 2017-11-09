import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Controller used by JavaFX to generate a GUI application
 */

public class Controller {

    private String nextLink;

    @FXML
    private TextArea chartsTextArea;

    @FXML
    private JsonQueryUtils apiQuery;

    @FXML
    private File sourceFolderPath;

    @FXML
    private ChoiceBox<String> countryChoiceBox;

    @FXML
    private ObservableList<String> countryObsList = FXCollections.observableArrayList("us","es","de","it","pt");

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    @FXML
    private ObservableList<String> genreObsList = FXCollections.observableArrayList("Alternative",
            "Ambient",
            "Blues",
            "Children’s Music",
            "Classical",
            "Country",
            "Dance",
            "Easy Listening",
            "Electronic",
            "Environmental",
            "Fitness & Workout",
            "Healing",
            "Holiday",
            "Instrumental",
            "Jazz",
            "Latino",
            "Lounge",
            "Meditation",
            "Nature",
            "New Age",
            "Relaxation",
            "Soundtrack",
            "Travel",
            "World");

    @FXML
    private Map<String, Integer> genreMap;



    public Controller(){
        this.apiQuery  = new JsonQueryUtils();
        genreMap = new HashMap<>();
    }


    @FXML
    private void initialize(){

        this.countryChoiceBox.setValue("us");
        this.genreChoiceBox.setValue("New Age");
        this.countryChoiceBox.setItems(this.countryObsList);
        this.genreChoiceBox.setItems(this.genreObsList);
        genreMap.put("Alternative",20);
        genreMap.put("Ambient", 1056);
        genreMap.put("Blues",2);
        genreMap.put("Children’s Music",4);
        genreMap.put("Classical",5);
        genreMap.put("Country",6);
        genreMap.put("Dance",17);
        genreMap.put("Easy Listening",25);
        genreMap.put("Electronic",7);
        genreMap.put("Environmental",1125);
        genreMap.put("Fitness & Workout",50);
        genreMap.put("Healing",1126);
        genreMap.put("Holiday",8);
        genreMap.put("Instrumental",53);
        genreMap.put("Jazz",11);
        genreMap.put("Latino",12);
        genreMap.put("Lounge",1054);
        genreMap.put("Meditation",1127);
        genreMap.put("Nature",1128);
        genreMap.put("New Age",13);
        genreMap.put("Relaxation",1129);
        genreMap.put("Soundtrack",16);
        genreMap.put("Travel", 1130);
        genreMap.put("World",19);
    }

    @FXML
    public void getButtonAction() throws InvalidKeySpecException, NoSuchAlgorithmException {

        StringBuilder link = new StringBuilder("https://api.music.apple.com/v1/catalog/"); //us/charts?types=albums&genre=1127&limit=50

        link.append(this.countryChoiceBox.getValue());
        link.append("/charts?types=albums&genre=");
        link.append(this.genreMap.get(this.genreChoiceBox.getValue()));
        link.append("&limit=50");

        System.out.println("LINK IS: " + link.toString());

        this.getCharts(link.toString());

    }

    /**
     * This method is used to parse the <code>jsonResponse</code> from the <code>JsonQueryUtils</code>class.
     * @param link This is the first paramter to addNum method
     * @return void
     */
    private void getCharts(String link) throws InvalidKeySpecException, NoSuchAlgorithmException {

        //a StringBuilder object to store the list of albums
        StringBuilder result = new StringBuilder();

        //helper variable to store the numerical position of each album in the list
        int position = 1;

        //this variable limits the search for additional pages if the "next" field
        //is absent. Otherwise it is set to 6.
        int loopLimit = 1;

        //for loop to retrieve 6 pages of charts. Look at the AM API doc
        for(int x = 0; x < loopLimit; x++) {

            JsonObject jsonResponse = apiQuery.getJson(link);

            System.out.println("JSON RESPONSE: " + jsonResponse.toString());

            JsonObject results = jsonResponse.get("results").getAsJsonObject();

            JsonArray albums = results.get("albums").getAsJsonArray();

            JsonObject data = albums.get(0).getAsJsonObject();

            if (this.checkNode(data, "next")) {
                loopLimit = 6;
                this.nextLink = data.get("next").getAsString();
            }

            System.out.println("this.nextLink: " + this.nextLink);

            JsonArray dataArray = data.get("data").getAsJsonArray();

            int sizeDataArray = dataArray.size();
            System.out.println("size data array: " + sizeDataArray);

            //after reaching the correct field we loop over each album to retrieve the info
            for (int i = 0; i < sizeDataArray; i++) {

                JsonObject firstResult = dataArray.get(i).getAsJsonObject();

                JsonObject attributes = firstResult.get("attributes").getAsJsonObject();

                JsonObject artwork = attributes.get("artwork").getAsJsonObject();

                String artworkUrl = "";

                //checking whether the json field exists. If not, an error string is inserted
                if (this.checkNode(artwork, "url")) {
                    artworkUrl = artwork.get("url").getAsString();
                } else {
                    artworkUrl = "NOT FOUND";
                }

                //replacing {w} and {h} with the respective width and height
                Integer width = artwork.get("width").getAsInt();
                String coverUrl = artworkUrl.replaceAll("\\{w\\}", width.toString()).replaceAll("\\{h\\}", width.toString());
                System.out.println("COVER URL: " + coverUrl);

                String albumName = "";
                if (this.checkNode(attributes, "name")) {
                    albumName = attributes.get("name").getAsString();
                } else {
                    albumName = "NOT FOUND";
                }

                String artistName = "";
                if (this.checkNode(attributes, "artistName")) {
                    artistName = attributes.get("artistName").getAsString();
                } else {
                    artistName = "NOT FOUND";
                }

                String url = "";
                if (this.checkNode(attributes, "url")) {
                    url = attributes.get("url").getAsString();
                } else {
                    url = "NOT FOUND";
                }

                int trackCount = 0;
                if (this.checkNode(attributes, "trackCount")) {
                    trackCount = attributes.get("trackCount").getAsInt();
                }

                String releaseDate = "";
                if (this.checkNode(attributes, "releaseDate")) {
                    releaseDate = attributes.get("releaseDate").getAsString();
                } else {
                    releaseDate = "NOT FOUND";
                }

                String recordLabel = "";
                if (this.checkNode(attributes, "recordLabel")) {
                    recordLabel = attributes.get("recordLabel").getAsString();
                } else {
                    recordLabel = "NOT FOUND";
                }

                String copyright = "";
                if (this.checkNode(attributes, "copyright")) {
                    copyright = attributes.get("copyright").getAsString();
                } else {
                    copyright = "NOT FOUND";
                }

                //we append all the info retrieved and thus build the chart
                result.append(position + "; ")
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
                        .append(copyright)
                        .append("; ")
                        .append(coverUrl)
                        .append("\n");

                //incrementing the album position
                position++;
            }
            //after going through all the first page array elements we pass
            //another link containing the second page results. This up to the 6th page.
            StringBuilder next = new StringBuilder("https://api.music.apple.com");
            next.append(this.nextLink);

            link = next.toString();

        }

        this.chartsTextArea.setText(result.toString());

    }

    /**
     * Helper method to check whether the json retrieved has the field passed as a parameter the <code>jsonResponse</code> from the <code>JsonQueryUtils</code>class.
     * @param gson the json object retrieved
     * @param key the key against which the check is made
     * @return Boolean
     */
    @FXML
    private Boolean checkNode(JsonObject gson, String key){
        return gson.has(key);
    }

    /**
     * This method displays the name and the version number of the program,
     * when the About item menu is clicked.
     */
    @FXML
    public void aboutItemAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AMCharts v1.0");
        alert.setHeaderText("AMCharts v1.0\n");
        alert.showAndWait();
    }

    /**
     * This method clears the text area.
     */
    @FXML
    public void resetAction(){

        this.chartsTextArea.clear();

    }

}