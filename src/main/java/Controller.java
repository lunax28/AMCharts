import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private Label sourceLabel;

    @FXML
    private Button outputButton;

    @FXML
    private Button getButton;

    @FXML
    private File sourceFolderPath;


    @FXML
    public void outputButtonAction(){


        System.out.println("IT WORKS!");
    }

    @FXML
    public void getButtonAction(){
        System.out.println("IT WORKS!");
    }

    @FXML
    private ChoiceBox<String> countryChoiceBox;

    ObservableList<String> countryObsList = FXCollections.observableArrayList("us","es","de","it","pt");




    @FXML
    private ChoiceBox<String> genreChoiceBox;
    ObservableList<String> genreObsList = FXCollections.observableArrayList("13","12","11","10");


    @FXML
    private void initialize(){

        this.countryChoiceBox.setValue("us");
        this.genreChoiceBox.setValue("13");
        this.countryChoiceBox.setItems(this.countryObsList);
        this.genreChoiceBox.setItems(this.genreObsList);


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