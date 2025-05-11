package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;

public class WelcomeScene
{
    private Stage stage;
    private Scene scene;

    @FXML
    Button loginButton;
    @FXML
    Button signUpButton;

    @FXML
    public void LoginButtonEvent(ActionEvent event) throws IOException
    {
        PigeonApplication.sceneSwitcher("LoginScene");
    }

    @FXML
    public void SignUpButton(ActionEvent event) throws IOException
    {
        PigeonApplication.sceneSwitcher("SignUpScene");
    }

}
