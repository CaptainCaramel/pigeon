package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PropertySheet;

public class ToolBarController
{
    public boolean isAlreadyCreated = false;
    ContextMenu contextMenu = new ContextMenu();

    @FXML
    public void ConMenu(ActionEvent event)
    {
        if(!isAlreadyCreated)
        {
            creatingWindow(event);
           isAlreadyCreated = true;
        }
        else
        {
            contextMenu.getItems().clear();
            creatingWindow(event);
            isAlreadyCreated = false;
        }
    }

    public void creatingWindow(ActionEvent event)
    {
        Circle profileCircle = new Circle(60, Color.CRIMSON);

        Label username = new Label(Client.getUser().getEmail());
        username.setStyle("-fx-text-fill: crimson; -fx-font-weight: bold; -fx-font-size: 15");
        Label initialText = new Label(String.valueOf(Client.getUser().getLogin().charAt(0)) + String.valueOf(Client.getUser().getLogin().charAt(1)));
        initialText.setTextFill(Color.WHITE);
        initialText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        StackPane circleWithText = new StackPane(profileCircle, initialText);
        circleWithText.setPrefSize(120, 120);

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(circleWithText, username);

        hbox.setMouseTransparent(true);
        hbox.setFocusTraversable(false);
        hbox.setBackground(Background.EMPTY);
        hbox.setStyle("-fx-background-color: transparent;");

        HBox vBox = new HBox(6);
        Button settingsbutton = new Button();
        Button logOut = new Button();
        settingsbutton.setText("Settings");
        logOut.setText("LogOut");
        vBox.getChildren().addAll(settingsbutton, logOut);

        CustomMenuItem profileItem = new CustomMenuItem(hbox);
        CustomMenuItem buttons = new CustomMenuItem(vBox);
        profileItem.setStyle(String.valueOf(Color.BEIGE));

        contextMenu.getItems().addAll(profileItem, buttons);

        contextMenu.show((Button) event.getSource(), Side.RIGHT, 400, 0);
    }
}
