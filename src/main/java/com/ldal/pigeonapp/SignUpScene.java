package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpScene
{
    private final SQLServer sqlServer;
    private final PassHasher passHasher;
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

    public SignUpScene() {
        sqlServer = new SQLServer();
        passHasher = new PassHasher();
    }

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
            }
            else
            {
                warning.setText("Invalid information");
            }
        }
    }
}
