/**
 * Sample Skeleton for 'SongsChartsGui.fxml' Controller Class
 */

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import com.equilibriummusicgroup.AMCharts.model.ChartsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SongsChartsController {

    @FXML
    private ChartsModel chartsModel;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="countryChoiceBox"
    private ChoiceBox<String> countryChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="genreChoiceBox"
    private ChoiceBox<String> genreChoiceBox; // Value injected by FXMLLoader

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
    private Button betaButton;

    @FXML
    private Map<String, Integer> genreMap;

    @FXML
    private ObservableList<String> countryObsList = FXCollections.observableArrayList("de", "es", "fr", "it", "jp", "pt", "us", "at");

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

    private StringBuilder allGenresResult;

    @FXML // fx:id="limitTextField"
    private TextField limitTextField; // Value injected by FXMLLoader



    public SongsChartsController() {
        genreMap = new HashMap<>();
    }




    private String linkBuilder() {
        StringBuilder link = new StringBuilder("https://api.music.apple.com/v1/catalog/"); //us/charts?types=albums&genre=1127&limit=50

        link.append(this.countryChoiceBox.getValue());
        link.append("/charts?types=songs&genre=");
        link.append(this.genreMap.get(this.genreChoiceBox.getValue()));
        link.append("&limit=50");

        System.out.println("LINK IS: " + link.toString());
        return link.toString();
    }


    @FXML
    void getSongsButton(ActionEvent event) throws InvalidKeySpecException, NoSuchAlgorithmException {

        String countryValue = this.countryChoiceBox.getValue();

        String genreValue = this.genreChoiceBox.getValue();

        StringBuilder result = new StringBuilder();

        this.albumsTextArea.clear();

        String link = linkBuilder();

        if (link == null) {
            return;
        }

        String limit = this.limitTextField.getText();
        int loopLimit = 1;
        try {
            loopLimit = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            displayErrorMessage("Limit is negative or 0!");
            e.printStackTrace();
            return;
        }

        /*if (loopLimit <= 0) {
            displayErrorMessage("Limit is negative or 0!");
            return;
        }*/

        result = chartsModel.getSongsCharts(link, loopLimit, countryValue,this.genreMap.get(this.genreChoiceBox.getValue()).toString());

        this.albumsTextArea.setText(result.toString());

    }

    @FXML
    void resetAction(ActionEvent event) {
        this.albumsTextArea.clear();

    }

    @FXML
    void aboutItemAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AMCharts v1.0");
        alert.setHeaderText("AMCharts v1.0\n");
        alert.setContentText("In the first screen you can retrieve the top albums by genre.\n" +
                "In the second screen you can retrieve the top tracks by genre. In the LIMIT text field, you can type how many songs you are going to query.");
        alert.showAndWait();

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert countryChoiceBox != null : "fx:id=\"countryChoiceBox\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert genreChoiceBox != null : "fx:id=\"genreChoiceBox\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert getButton != null : "fx:id=\"getButton\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert albumsTextArea != null : "fx:id=\"albumsTextArea\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert changeSceneButton != null : "fx:id=\"changeSceneButton\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";
        assert limitTextField != null : "fx:id=\"limitTextField\" was not injected: check your FXML file 'SongsChartsGui.fxml'.";

        this.countryChoiceBox.setValue("us");
        this.genreChoiceBox.setValue("New Age");
        this.countryChoiceBox.setItems(this.countryObsList);
        this.genreChoiceBox.setItems(this.genreObsList);
        genreMap.put("Alternative", 20);
        genreMap.put("Ambient", 1056);
        genreMap.put("Blues", 2);
        genreMap.put("Children’s Music", 4);
        genreMap.put("Classical", 5);
        genreMap.put("Country", 6);
        genreMap.put("Dance", 17);
        genreMap.put("Easy Listening", 25);
        genreMap.put("Electronic", 7);
        genreMap.put("Environmental", 1125);
        genreMap.put("Fitness & Workout", 50);
        genreMap.put("Healing", 1126);
        genreMap.put("Holiday", 8);
        genreMap.put("Instrumental", 53);
        genreMap.put("Jazz", 11);
        genreMap.put("Latino", 12);
        genreMap.put("Lounge", 1054);
        genreMap.put("Meditation", 1127);
        genreMap.put("Nature", 1128);
        genreMap.put("New Age", 13);
        genreMap.put("Relaxation", 1129);
        genreMap.put("Soundtrack", 16);
        genreMap.put("Travel", 1130);
        genreMap.put("World", 19);

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

    public void displayErrorMessage(String textMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(textMessage);
        alert.showAndWait();

    }

    public void setModel(ChartsModel model) {
        this.chartsModel = model;
    }

    @FXML
    void doBetaButton(ActionEvent event) throws InvalidKeySpecException, NoSuchAlgorithmException {
        allGenresResult = new StringBuilder();
        String countryValue = this.countryChoiceBox.getValue();
        StringBuilder result = new StringBuilder();

        this.albumsTextArea.clear();

        List<String> linkArray = new ArrayList<>();



      //List<String> allGenresUrl = linkAllBuilder();

        String limit = this.limitTextField.getText();
        int loopLimit = 1;
        try {
            loopLimit = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        if (loopLimit <= 0) {
            displayErrorMessage("Limit is negative or 0!");
            return;
        }


        Scanner scanner = new Scanner(this.allGenres);

        List<String> itunesGenres = new ArrayList<>();

        while (scanner.hasNext()) {
            String genre = scanner.nextLine();
            System.out.println("genre: " + genre);
            itunesGenres.add(genre);

        }


        List<String> allGenresUrl = new ArrayList<>();
        for (String genreId : itunesGenres) {

            StringBuilder linkBuild = new StringBuilder("https://api.music.apple.com/v1/catalog/"); //us/charts?types=albums&genre=1127&limit=50

            linkBuild.append(this.countryChoiceBox.getValue());
            linkBuild.append("/charts?types=songs&genre=");
            linkBuild.append(genreId);
            linkBuild.append("&limit=50");

            System.out.println("genre link: " + linkBuild.toString());

            allGenresUrl.add(linkBuild.toString());

            result = chartsModel.getAllSongsCharts(linkBuild.toString(), loopLimit, countryValue,genreId);

            allGenresResult.append(result);
        }

        /*for (String genreUrlLink : allGenresUrl) {


            result = chartsModel.getAllSongsCharts(genreUrlLink, loopLimit, countryValue);

            allGenresResult.append(result);


        }
*/
        this.albumsTextArea.setText(allGenresResult.toString());


    }


    private List<String> linkAllBuilder() {

        Scanner scanner = new Scanner(this.allGenres);

        List<String> itunesGenres = new ArrayList<>();

        while (scanner.hasNext()) {
            String genre = scanner.nextLine();
            System.out.println("genre: " + genre);
            itunesGenres.add(genre);

        }


        List<String> allGenresUrl = new ArrayList<>();
        for (String genreId : itunesGenres) {

            StringBuilder linkBuild = new StringBuilder("https://api.music.apple.com/v1/catalog/"); //us/charts?types=albums&genre=1127&limit=50

            linkBuild.append(this.countryChoiceBox.getValue());
            linkBuild.append("/charts?types=songs&genre=");
            linkBuild.append(genreId);
            linkBuild.append("&limit=50");

            System.out.println("genre link: " + linkBuild.toString());

            allGenresUrl.add(linkBuild.toString());
        }


        return allGenresUrl;


    }


    private String allGenres = "13\n" +
            "53\n"; /*+
            "1009\n" +
            "1010\n" +
            "1011\n" +
            "1012\n" +
            "1013\n" +
            "1210\n" +
            "3\n" +
            "1167\n" +
            "1171\n" +
            "4\n" +
            "1014\n" +
            "1015\n" +
            "1016\n" +
            "5\n" +
            "1017\n" +
            "1018\n" +
            "1019\n" +
            "1020\n" +
            "1021\n" +
            "1022\n" +
            "1023\n" +
            "1024\n" +
            "1025\n" +
            "1026\n" +
            "1027\n" +
            "1028\n" +
            "1029\n" +
            "1030\n" +
            "1031\n" +
            "1032\n" +
            "1211\n" +
            "6\n" +
            "1033\n" +
            "1034\n" +
            "1035\n" +
            "1036\n" +
            "1037\n" +
            "1038\n" +
            "1039\n" +
            "1040\n" +
            "1041\n" +
            "1042\n" +
            "1043\n" +
            "7\n" +
            "1056\n" +
            "1057\n" +
            "1058\n" +
            "1060\n" +
            "1061\n" +
            "8\n" +
            "1079\n" +
            "1080\n" +
            "1081\n" +
            "1082\n" +
            "1083\n" +
            "1084\n" +
            "1085\n" +
            "1086\n" +
            "1087\n" +
            "1088\n" +
            "1089\n" +
            "1090\n" +
            "1091\n" +
            "1092\n" +
            "1093\n" +
            "9\n" +
            "10\n" +
            "1062\n" +
            "1063\n" +
            "1064\n" +
            "1065\n" +
            "1066\n" +
            "1067\n" +
            "11\n" +
            "1052\n" +
            "1106\n" +
            "1107\n" +
            "1108\n" +
            "1109\n" +
            "1110\n" +
            "1111\n" +
            "1112\n" +
            "1113\n" +
            "1114\n" +
            "1207\n" +
            "1208\n" +
            "1209\n" +
            "12\n" +
            "1115\n" +
            "1116\n" +
            "1117\n" +
            "1118\n" +
            "1119\n" +
            "1120\n" +
            "1121\n" +
            "1123\n" +
            "1124\n" +
            "13\n" +
            "1125\n" +
            "1126\n" +
            "1127\n" +
            "1128\n" +
            "1129\n" +
            "1130\n" +
            "14\n" +
            "1131\n" +
            "1132\n" +
            "1133\n" +
            "1134\n" +
            "1135\n" +
            "15\n" +
            "1136\n" +
            "1137\n" +
            "1138\n" +
            "1139\n" +
            "1140\n" +
            "1141\n" +
            "1142\n" +
            "1143\n" +
            "16\n" +
            "1165\n" +
            "1166\n" +
            "1168\n" +
            "1169\n" +
            "1172\n" +
            "17\n" +
            "1044\n" +
            "1045\n" +
            "1046\n" +
            "1047\n" +
            "1048\n" +
            "1049\n" +
            "1050\n" +
            "1051\n" +
            "18\n" +
            "1068\n" +
            "1069\n" +
            "1070\n" +
            "1071\n" +
            "1072\n" +
            "1073\n" +
            "1074\n" +
            "1075\n" +
            "1076\n" +
            "1077\n" +
            "1078\n" +
            "19\n" +
            "1177\n" +
            "1178\n" +
            "1179\n" +
            "1180\n" +
            "1181\n" +
            "1182\n" +
            "1184\n" +
            "1185\n" +
            "1186\n" +
            "1187\n" +
            "1188\n" +
            "1189\n" +
            "1190\n" +
            "1191\n" +
            "1195\n" +
            "1196\n" +
            "1197\n" +
            "1198\n" +
            "1199\n" +
            "1200\n" +
            "1201\n" +
            "1202\n" +
            "1203\n" +
            "1204\n" +
            "1205\n" +
            "1206\n" +
            "20\n" +
            "1001\n" +
            "1002\n" +
            "1003\n" +
            "1004\n" +
            "1005\n" +
            "1006\n" +
            "21\n" +
            "1144\n" +
            "1145\n" +
            "1146\n" +
            "1147\n" +
            "1148\n" +
            "1149\n" +
            "1150\n" +
            "1151\n" +
            "1152\n" +
            "1153\n" +
            "1154\n" +
            "1155\n" +
            "1156\n" +
            "1157\n" +
            "1158\n" +
            "1159\n" +
            "1160\n" +
            "1161\n" +
            "1162\n" +
            "1163\n" +
            "22\n" +
            "1094\n" +
            "1095\n" +
            "1096\n" +
            "1097\n" +
            "1098\n" +
            "1099\n" +
            "1100\n" +
            "1101\n" +
            "1103\n" +
            "1104\n" +
            "1105\n" +
            "23\n" +
            "1173\n" +
            "1174\n" +
            "1175\n" +
            "1176\n" +
            "24\n" +
            "1183\n" +
            "1192\n" +
            "1193\n" +
            "1194\n" +
            "25\n" +
            "1053\n" +
            "1054\n" +
            "1055\n" +
            "27\n" +
            "28\n" +
            "29\n" +
            "30\n" +
            "50\n" +
            "51\n" +
            "52\n" +
            "53\n" +
            "1122\n" +
            "1220\n" +
            "1221\n" +
            "1222\n" +
            "1223\n" +
            "1224\n" +
            "1225\n" +
            "1226\n" +
            "1227\n" +
            "1228\n" +
            "1229\n" +
            "50000061\n" +
            "50000063\n" +
            "50000064\n" +
            "50000066\n" +
            "50000068";
*/



}

