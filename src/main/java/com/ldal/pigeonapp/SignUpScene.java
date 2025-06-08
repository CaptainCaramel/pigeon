package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpScene implements Initializable
{
    private PassHasher passHasher;
    private SQLServer sqlServer;

    private User user;
    @FXML
    public Button SignUpbutton;
    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    public TextField email;
    @FXML
    public Label warning;
    @FXML
    public Button back;

    @FXML
    private void SignUpAction(ActionEvent event)
    {
        checkInfo();
    }

    @FXML
    private void Backtomenu(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void checkInfo()
    {
        String username1 = username.getText();
        String password1 = password.getText();
        String email1 = email.getText();

        if (username1.isEmpty() || password1.isEmpty() || email1.isEmpty())
        {
            warning.setText("*Please input your data");
        }
        else
        {
            if (User.validateEmail(email1) && User.validateLogin(username1) && User.validatePassword(password1) && !sqlServer.checkDuplicateLogin(username1))
            {
                sqlServer.SignUp(username1, email1 + "@pigeon.com", passHasher.hasher(password1), passHasher.backuppassword());
                warning.setText("Successfully signed up");
            }
            else if(!User.validateEmail(email1) || !User.validateLogin(username1))
            {
                warning.setText("Invalid Login or email");
            }
            else if(!User.validatePassword(password1))
            {
                warning.setText("Invalid password");
            }
            else if(sqlServer.checkDuplicateLogin(username1))
            {
                warning.setText("Duplicate login");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passHasher = new PassHasher();
        sqlServer = Client.getSQLServer();
    }
}
