package com.ldal.pigeonapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import com.ldal.pigeonapp.back.Client;

public class PigeonApplication extends Application {

    Client client;

    @Override
    public void start(Stage stage) throws IOException {

        client = new Client();

        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720);
        scene.setFill(Color.BEIGE);
        stage.setTitle("Pigeon Mail, User : " + client.getUser().getLogin());
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();

    }
}