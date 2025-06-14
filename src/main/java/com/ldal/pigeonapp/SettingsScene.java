package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsScene
{
    @FXML
    private TextField currentpassword;
    @FXML
    private TextField newpassword;
    @FXML
    private TextField newpasswordconf;
    PassHasher passHasher = new PassHasher();
    SQLServer sqlServer = new SQLServer();
    @FXML
    private Label warner;

    @FXML
    public void changepassword(ActionEvent event)
    {
        if(sqlServer.validatePassword(Client.getUser().getLogin(), passHasher.hasher(currentpassword.getText())) && newpassword.getText().equals(newpasswordconf.getText()) && User.validatePassword(newpassword.getText()))
        {
            sqlServer.changePassword(passHasher.hasher(newpassword.getText()), Client.getUser().getLogin());
            warner.setStyle("-fx-text-fill: green");
            warner.setText("Password changed successfully");
            Client.setRememberMe(false);
        }
        else
        {
            warner.setStyle("-fx-text-fill: red");
            warner.setText("Invalid Information");
            Client.setRememberMe(Client.isRememberMe());
        }
    }
    @FXML
    public void spamChanger(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SpamSettingsScene.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }
    public void Backtomenu(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }
}
