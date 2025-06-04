package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ConMenu
{
    ToolBarController toolBarController = new ToolBarController();

    @FXML
    public void profilebutton(ActionEvent event)
    {
        toolBarController.ConMenu(event);
    }
}
