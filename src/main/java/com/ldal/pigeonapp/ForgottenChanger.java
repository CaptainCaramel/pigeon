package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgottenChanger
{
    @FXML
    private PasswordField backuppassword;
    @FXML
    private PasswordField newpassword;
    @FXML
    private PasswordField newpasswordconf;
    @FXML
    private TextField loginer;
    @FXML
    private Label warner;
    @FXML
    private Label warner1;
    SQLServer sqlServer = new SQLServer();
    PassHasher passHasher = new PassHasher();

    @FXML
    public void Backtomenu(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void changepassword(ActionEvent event)
    {
        if(sqlServer.validateLogin(loginer.getText()) && sqlServer.validateRecoveryPass(loginer.getText(), backuppassword.getText()) && newpasswordconf.getText().equals(newpassword.getText()))
        {
            sqlServer.changePassword(passHasher.hasher(newpassword.getText()), loginer.getText());
            warner.setStyle("-fx-text-fill: green");
            String newRecoverypass = passHasher.backuppassword();
            sqlServer.changeRecoverypass(newRecoverypass, loginer.getText());
            warner.setText("Password changed successfully");
            warner1.setStyle("-fx-text-fill: green");
            warner1.setText("new recoverypass: " + newRecoverypass);
        }
        else
        {
            warner.setStyle("-fx-text-fill: red");
            warner.setText("Invalid Information");
        }
    }
}
