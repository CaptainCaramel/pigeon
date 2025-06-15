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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SignUpScene implements Initializable
{
    private PassHasher passHasher;
    private SQLServer sqlServer = new SQLServer();

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
    String error;
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
            WarnerClass.WarnerError(warning, "Please input your data", false);
        }
        else
        {
            if (User.validateEmail(email1) && User.validateLogin(username1) && User.validatePassword(password1) && !sqlServer.checkDuplicateLogin(username1) && !sqlServer.checkDuplicateEmail(email1))
            {
                String recoverypass = passHasher.backuppassword();

                String formatted = LocalDateTimer.localDateTime();

                sqlServer.SignUp(username1, email1 + "@pigeon.nest", passHasher.hasher(password1), recoverypass, formatted);
                warning.setText("Successfully signed up, recoverypass: " + recoverypass);
                WarnerClass.WarnerError(warning, "Successfully signed up, recoverypass: " + recoverypass, true);
            }
            else if(!User.validateEmail(email1) || !User.validateLogin(username1))
            {
                if(email1.length() < 4)
                {
                    error = "Email must be over 4 characters long";
                    WarnerClass.WarnerError(warning, "Email must be over 4 characters long", false);
                }
                else if(email1.length() > 20)
                {
                    error = "Email must be under 20 characters long";
                }
                else
                {
                    error = "Email or username contains invalid characters or restricted words";
                }
                WarnerClass.WarnerError(warning, error, false);
            }
            else if(!User.validatePassword(password1))
            {
                String error;
                if(password1.length() < 8)
                {
                    error = "password must be over 8 characters long in login";
                }
                else if(password1.length() > 26)
                {
                    error = "password must be under 26 characters long";
                }
                else
                {
                    error = "Invalid character or restricted word present in username";
                }
                WarnerClass.WarnerError(warning, error, false);
            }
            else if(sqlServer.checkDuplicateLogin(username1))
            {
                WarnerClass.WarnerError(warning, "Duplicate login", false);
            }
            else if(sqlServer.checkDuplicateEmail(email1))
            {
                WarnerClass.WarnerError(warning, "Duplicate email", false);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passHasher = new PassHasher();
        sqlServer = Client.getSQLServer();
    }
}
