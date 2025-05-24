package com.ldal.pigeonapp;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        senderText.setText(email.getSender().getEmail());
        emailText.setText(email.getText());
        subjText.setText(email.getSubject());
        dateText.setText(email.getDateTime());


    }
}
