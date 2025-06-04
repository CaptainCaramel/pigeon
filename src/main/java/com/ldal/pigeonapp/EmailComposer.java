package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailComposer implements Initializable {
    @FXML
    private TextField emailText;
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
    private VBox sideBarVbox;

    @FXML
    private void goToSelector(ActionEvent actionEvent) throws IOException {
        SideBarController.goToSelector((Button)actionEvent.getSource());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SideBarController.inboxButton = inboxButton;
        SideBarController.draftsButton = draftsButton;
        SideBarController.sentButton = sentButton;
        SideBarController.spamButton = spamButton;

        SideBarController.initSideBar();
    }
}
