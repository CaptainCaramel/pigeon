package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailReader implements Initializable {
    public static Email email;
    @FXML
    private Label emailText;
    @FXML
    private Label senderText;
    @FXML
    private Label subjText;
    @FXML
    private Label dateText;
    @FXML
    private Label timeText;

    @FXML
    private Button inboxButton;
    @FXML
    private Button draftsButton;
    @FXML
    private Button sentButton;
    @FXML
    private Button spamButton;

    @FXML
    private VBox sideBarVbox;

    @FXML
    private void goToSelector(ActionEvent actionEvent) throws IOException {
        Button clickedButton = (Button) actionEvent.getSource();
        if(clickedButton.equals(inboxButton)) EmailSelector.folderID = 0;
        else if(clickedButton.equals(draftsButton)) EmailSelector.folderID = 1;
        else if(clickedButton.equals(sentButton)) EmailSelector.folderID = 2;
        else if(clickedButton.equals(spamButton)) EmailSelector.folderID = 3;

        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (inboxButton.getScene().getWindow());
        stage.setScene(scene);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String dateTime = email.getDateTime();
        senderText.setText(email.getSender().getEmail());
        emailText.setText(email.getText());
        subjText.setText(email.getSubject());
        dateText.setText(dateTime.substring(0,10));
        timeText.setText(dateTime.substring(11));
    }

}
