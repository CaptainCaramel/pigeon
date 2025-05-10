package com.ldal.pigeonapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    public LoginScene() {
        sqlServer = new SQLServer();
        passHasher = new PassHasher();
    }

    @FXML
    private void LoginButton()
    {
        checkinfo();
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
                if(!sqlServer.validateLogin(username1) || !sqlServer.validatePassword(username1, passHasher.hasher(password1))){
                    warning.setText("*Invalid username or password!");
                    return;
                }
                else{
                    warning.setText("*Login successful!");
                }
            }
    }
}
