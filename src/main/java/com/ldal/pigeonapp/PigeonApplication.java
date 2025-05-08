package com.ldal.pigeonapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import com.ldal.pigeonapp.back.Client;

public class PigeonApplication extends Application
{
    Client client;

    @Override
    public void start(Stage stage) throws IOException
    {
        client = new Client();
        

        Group root = new Group();

        Label label = new Label("");
        TextField textfield = new TextField();
        Button button = new Button("Login");
        HBox row1 = new HBox(10);
        row1.getChildren().addAll(label, textfield, button);
        root.getChildren().add(row1);

        Image loginBG = new Image("C:\\Users\\User\\Downloads\\loginBackgroud.png");
        BackgroundImage backgroundimage = new BackgroundImage(loginBG,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);

        Scene scene = new Scene(root, 1280, 720);

        stage.setTitle("Pigeon Mail, User : " + client.getUser().getLogin());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}