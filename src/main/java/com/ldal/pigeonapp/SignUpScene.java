package com.ldal.pigeonapp;

import javafx.fxml.FXML;

import java.awt.*;

public class SignUpScene {
    private SQLServer sqlServer;
    private PassHasher passHasher;
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

    public SignUpScene() {
        sqlServer = new SQLServer();
        passHasher = new PassHasher();
    }

    private void SignUpButton() {
        checkinfo();
    }

    public void checkinfo()
    {
        String username1 = username.getText();
        String password1 = password.getText();
        String email1 = email.getText();

        if (username1.length() == 0 || password1.length() == 0 || email1.length() == 0) {
            warning.setText("*Please input your data");
        } else {
            if (User.validateEmail(email1) && User.validateLogin(username1) && User.validatePassword(password1) && !sqlServer.checkDuplicateLogin(username1)) {
                sqlServer.SignUp(username1, email1 + "@pigeon.com", passHasher.hasher(password1), passHasher.backuppassword());
            } else {
                warning.setText("Invalid information");
            }
        }
    }
}
