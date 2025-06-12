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

public class SpamSettingsScene
{
    @FXML
    private TextField userinput;
    @FXML
    private Label warner;
    @FXML
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
    @FXML
    public void private1(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/SettingsScene.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {throw new RuntimeException(e);}
    }
    @FXML
    public void spamAdd(ActionEvent event)
    {
        String userinput1 = userinput.getText();
        if(!userinput1.isEmpty() && Client.getSQLServer().validateEmail(userinput1) && !Client.getSpamblacklist().contains(userinput1))
        {
            Client.setSpamblacklist(userinput1);
            warner.setText(userinput1 + " spamlisted");
            Client.saveSpamBlacklist();
        }
        else if(!userinput1.isEmpty() || Client.getSQLServer().validateLogin(userinput1))
        {
            warner.setText("Invalid user");
        }
        else
        {
            warner.setText("User already spamlisted");
        }
    }
    @FXML
    public void spamRemove(ActionEvent event)
    {
        String userinput1 = userinput.getText();
        if(!userinput1.isEmpty() && Client.getSQLServer().validateEmail(userinput1) && Client.getSpamblacklist().contains(userinput1))
        {
            Client.removeSpamblacklist(userinput1);
            warner.setText(userinput1 + " removed from spamlist");
            Client.saveSpamBlacklist();
        }
        else
        {
            warner.setText("Invalid user");
        }
    }

    public static class SqlSetupScene {
    }
}
