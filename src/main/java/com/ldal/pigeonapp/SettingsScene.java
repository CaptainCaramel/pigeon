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
    PassHasher passHasher = new PassHasher();
    SQLServer sqlServer = new SQLServer();
    @FXML
    private Label warner;

    @FXML
    public void changepassword(ActionEvent event)
    {
        if(sqlServer.validatePassword(Client.getUser().getLogin(), passHasher.hasher(currentpassword.getText())))
        {
            sqlServer.changePassword(Client.getUser().getLogin(), passHasher.hasher(currentpassword.getText()));
        }
        else
        {
            warner.setText("Incorrect Information");
        }
    }

    @FXML
    public void back(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
