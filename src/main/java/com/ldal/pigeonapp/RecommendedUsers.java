package com.ldal.pigeonapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RecommendedUsers
{
    public static void recommendedUsersTab(ArrayList<String> topRecepeints, AnchorPane anchorPane)
    {
        if(!topRecepeints.isEmpty())
        {
            VBox vBox = new VBox();
            for(String s : topRecepeints)
            {
                Button recepeint = new Button(s);
                recepeint.getStylesheets().add("styleforROBOTO.css");
                recepeint.getStyleClass().add("custom-font-even-smaller");
                recepeint.setStyle("-fx-background-color: transparent; -fx-font-size: 12px; -fx-text-fill: black; -fx-radius-size: 6px; -fx-background-radius: 10; -fx-opacity: 0.82;");
                vBox.getChildren().add(recepeint);
            }
            vBox.setStyle("-fx-background-color: beige;");
            anchorPane.getChildren().add(vBox);
        }
    }
}
