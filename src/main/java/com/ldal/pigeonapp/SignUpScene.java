package com.ldal.pigeonapp;
import javafx.fxml.FXML;

import java.awt.*;

public class SignUpScene
{
    private SQLServer sqlServer;
    private PassHasher passHasher;
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

    public SignUpScene()
    {

    }

    private void SignUpButton()
    {
        checkinfo();
    }

    public boolean checkinfo()
    {
        while(true)
        {
            String username1 = username.getText();
            String password1 = password.getText();
            String email1 = email.getText();

            if (username1.length() == 0 || password1.length() == 0 || email1.length() == 0)
            {
                warning.setText("*Please input your data");
                continue;
            }
            else
            {

            }
        }
    }
}
