package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileScene implements Initializable
{
    @FXML
    private Label mailDeclarer;
    @FXML
    private Label usernameDeclarer;
    @FXML
    private Label IdDeclarer;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label dateCreatedDeclarer;
    @FXML
    private Label lastLogedinDeclarer;
    SQLServer sqlServer = new SQLServer();
    @FXML
    public void Backtomenu(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    @FXML
    public void profileDeleterStart(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/ProfileDeletionScene.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Circle profileCircle = new Circle(75, Color.CRIMSON);

        Label initialText = new Label(String.valueOf(Client.getUser().getLogin().charAt(0)) + String.valueOf(Client.getUser().getLogin().charAt(1)));
        initialText.setTextFill(Color.WHITE);
        initialText.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        StackPane circleWithText = new StackPane(profileCircle, initialText);
        circleWithText.setPrefSize(120, 120);

        circleWithText.setLayoutX(310);
        circleWithText.setLayoutY(222);

        anchorPane.getChildren().add(circleWithText);

        mailDeclarer.setText("Email: " + Client.getUser().getEmail());
        usernameDeclarer.setText("Username: " + Client.getUser().getLogin());
        IdDeclarer.setText("Account ID: " + Client.getUser().getId());
        String dateCreated = sqlServer.getDateCreatedFromID(Client.getUser().getLogin());
        dateCreatedDeclarer.setText("Date Created: " + dateCreated);
        lastLogedinDeclarer.setText("Logged In Since: " + PigeonApplication.dateTimeSincer);
    }
}
