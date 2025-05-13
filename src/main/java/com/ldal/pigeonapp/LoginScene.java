package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScene
{
    private final SQLServer sqlServer;
    private PassHasher passHasher;

    @FXML
    public Button loginbutton;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public CheckBox rememberMe;
    @FXML
    public Label warning;
    @FXML
    public Button button;

    public LoginScene()
    {
        sqlServer = new SQLServer();
        passHasher = new PassHasher();
    }

    @FXML
    private void LoginButton(ActionEvent event)
    {
        checkinfo();
    }

    @FXML
    private void Backtomenu(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void checkinfo()
    {
        String username1 = username.getText();
        String password1 = password.getText();

        if (username1.isEmpty() || password1.isEmpty())
        {
            warning.setText("*Please input your data");
        }
        else
        {
            if(!sqlServer.validateLogin(username1) || !sqlServer.validatePassword(username1, passHasher.hasher(password1)))
            {
               warning.setText("*Invalid username or password!");
               return;
           }
            else
            {
               warning.setText("*Login successful!");
            }
        }
    }
}
