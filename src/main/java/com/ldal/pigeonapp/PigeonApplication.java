package com.ldal.pigeonapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PigeonApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        Scene scene;
        Parent root;
        new Client();
        if(Client.getUser() != null && Client.isRememberMe())
        {
            root = FXMLLoader.load(PigeonApplication.class.getResource("/LoginScene.fxml"));
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/ConMenu.fxml"));
        }
        else
        {
            root = FXMLLoader.load(PigeonApplication.class.getResource("/LoginScene.fxml"));
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
            //root = FXMLLoader.load(PigeonApplication.class.getResource("/ConMenu.fxml"));
        }

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Pigeon");
        Image icon = new Image(PigeonApplication.class.getResource("/icon.png").toString());
        stage.getIcons().add(icon);
        stage.show();
    }


    public static void main(String[] args)
    {
        launch();
    }
}