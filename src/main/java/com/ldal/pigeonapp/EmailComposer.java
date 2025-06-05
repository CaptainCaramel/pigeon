package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class EmailComposer implements Initializable {
    @FXML
    private TextArea emailText;
    @FXML
    private TextField receiverText;
    @FXML
    private TextField subjText;

    @FXML
    private Button inboxButton;
    @FXML
    private Button draftsButton;
    @FXML
    private Button sentButton;
    @FXML
    private Button spamButton;
    @FXML
    private Button composerButton;
    @FXML
    private Button drafterButton;
    @FXML
    private Button sendButton;

    @FXML
    private Label errorText;

    @FXML
    private VBox sideBarVbox;

    @FXML
    private void goToSelector(ActionEvent actionEvent) throws IOException {
        SideBarController.goToSelector((Button)actionEvent.getSource());
    }

    @FXML
    private void sendEmail(ActionEvent actionEvent){
        SQLServer sqlServer = new SQLServer();

        String rec = receiverText.getText();
        String subj = subjText.getText();
        String eText = emailText.getText();

        if(!sqlServer.validateUser(rec)){
            errorText.setText("*User not found!");
            return;
        }
        if(subj.length() > 75){
            errorText.setText("*Subject max length(75) exceeded!");
            return;
        }
        if(eText.length() > 75000){
            errorText.setText("*Text max length(75k) exceeded!");
            return;
        }
        Email email = new Email(Client.getUser(), sqlServer.userFromEmail(rec), eText, subj);


        Client.EmailSender(email);
        errorText.setText("*Email sent!");
        receiverText.setText("");
        subjText.setText("");
        emailText.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SideBarController.inboxButton = inboxButton;
        SideBarController.draftsButton = draftsButton;
        SideBarController.sentButton = sentButton;
        SideBarController.spamButton = spamButton;
        SideBarController.composerButton = composerButton;
        SideBarController.drafterButton = drafterButton;

        sendButton.setStyle("-fx-background-color : #c83f44; -fx-background-radius : 15");
        sendButton.setOnMouseExited(e -> sendButton.setStyle("-fx-background-color : #c83f44; -fx-background-radius : 15"));
        sendButton.setOnMouseEntered(e -> sendButton.setStyle("-fx-background-color : #da4348; -fx-background-radius : 15"));

        SideBarController.initSideBar();
    }
}
