package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScene implements Initializable
{
    private PassHasher passHasher;
    private SQLServer sqlServer;
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

    private boolean remMe;

    Client client = new Client();


    @FXML
    private void LoginButton(ActionEvent event) throws IOException {
        String username1 = username.getText();
        String password1 = password.getText();

        if (username1.isEmpty() || password1.isEmpty()) {
            warning.setText("*Please input your data");
        } else {
            if (!sqlServer.validateLogin(username1) || !sqlServer.validatePassword(username1, passHasher.hasher(password1))) {
                warning.setStyle("-fx-text-fill: red");
                warning.setText("*Invalid username or password!");
                return;
            } else {
                warning.setStyle("-fx-text-fill: green");
                warning.setText("*Login successful!");

                Client.login(username1);
                client.saveSettings();
                System.out.println(Client.getUser().getLogin());

                Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailSelector.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);

            }
        }
    }

    @FXML
    public void rememberAction(ActionEvent event){

        Client.setRememberMe(rememberMe.isSelected());
        client.saveSettings();
    }

    @FXML
    private void Backtomenu(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void Forgotter(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/ForgottenChanger.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passHasher = new PassHasher();
        sqlServer = Client.getSQLServer();
    }
}
