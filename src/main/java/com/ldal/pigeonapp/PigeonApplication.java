package com.ldal.pigeonapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class PigeonApplication extends Application
{

    @Override
    public void start(Stage stage) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/LoginScene.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Pigeon");
        Image icon = new Image(PigeonApplication.class.getResource("/icon.png").toString());
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}