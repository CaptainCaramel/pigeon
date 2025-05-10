package com.ldal.pigeonapp;

import com.ldal.pigeonapp.back.PassHasher;
import com.ldal.pigeonapp.back.SQLServer;
import javafx.fxml.FXML;

import java.awt.*;

public class LoginScene
{
    private SQLServer sqlServer;
    private PassHasher passHasher;
    @FXML
    public Button loginbutton;
    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    public Checkbox remmemberme;
    @FXML
    public Label warning;

    public LoginScene() {}

    private void LoginButton()
    {
        checkinfo();
    }

    public boolean checkinfo()
    {
        while(true)
        {
            String username1 = username.getText();
            String password1 = password.getText();

            if (username1.length() == 0 || password1.length() == 0)
            {
                warning.setText("*Please input your data");
                continue;
            }
            else
            {
                boolean usernamevalidation = sqlServer.validateLogin(username1);
                String hased = passHasher.hasher(password1);
                boolean passwordvalidation = sqlServer.validatePassword(password1, hased);
                if (usernamevalidation == true && passwordvalidation == true) return true;
                else continue;
            }
        }
    }
}
