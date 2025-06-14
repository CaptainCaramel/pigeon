package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class SideBarController {
    public static Button inboxButton;
    public static Button draftsButton;
    public static Button sentButton;
    public static Button spamButton;
    public static Button composerButton;
    public static Button drafterButton;
    public static Button configureButton;
    public static Button clearerButton;

    public static void goToSelector(Button clickedButton) throws IOException
    {
        if(clickedButton.equals(inboxButton))
        {
            EmailSelector.folderID = 0;
            //EmailSelector.declareText.setText("INBOX");
        }
        else if(clickedButton.equals(draftsButton))
        {
            EmailSelector.folderID = 1;
            //EmailSelector.declareText.setText("DRAFT");
        }
        else if(clickedButton.equals(sentButton))
        {
            EmailSelector.folderID = 2;
            //EmailSelector.declareText.setText("SENT");
        }
        else if(clickedButton.equals(spamButton))
        {
            EmailSelector.folderID = 3;
            //EmailSelector.declareText.setText("SPAM");
        }

        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (clickedButton.getScene().getWindow());
        stage.setScene(scene);

    }

    public static void goToComposer(ActionEvent actionEvent) throws IOException {
        EmailComposer.draft = null;

        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailComposer.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (((Node)(actionEvent.getSource())).getScene().getWindow());
        stage.setScene(scene);
    }

    public static void editDraft(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailComposer.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (((Node)(actionEvent.getSource())).getScene().getWindow());
        stage.setScene(scene);
    }

    public static void initSideBar(){
        Button[] buttons = {inboxButton, draftsButton, sentButton, spamButton, composerButton, configureButton, clearerButton};
        //ystem.out.println(Arrays.toString(buttons));
        for(Button b : buttons){
            b.setStyle("-fx-text-fill: #000000; -fx-background-color: TRANSPARENT");
            b.setOnMouseEntered(event -> b.setStyle("-fx-text-fill: #e17c65; -fx-background-color: TRANSPARENT"));
            b.setOnMouseExited(event -> b.setStyle("-fx-text-fill: #000000; -fx-background-color: TRANSPARENT"));

        }
    }



}
