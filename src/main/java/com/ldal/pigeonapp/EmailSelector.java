package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmailSelector implements Initializable {
    @FXML
    private VBox emailListBox;

    private ArrayList<Button> eButtons = new ArrayList<>();
    ArrayList<Email> inbox = Client.getInbox();


    @FXML
    private void viewEmail(ActionEvent actionEvent) throws IOException {
        Button clickedButton = (Button) actionEvent.getSource();

        for (int i = 0; i < eButtons.size(); i++) {
            if(clickedButton.equals(eButtons.get(i))){
                EmailReader.email = inbox.get(i);
                break;
            }
        }

        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailReader.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (emailListBox.getScene().getWindow());
        stage.setScene(scene);

    }


    private void displayFolder(ArrayList<Email> folder){
        for (int i = 0; i < folder.size(); i++) {
            Email email = folder.get(i);

            Group bGroup = new Group();

            Button button = new Button();
            button.setPrefWidth(1126);
            button.setPrefHeight(43);
            button.setStyle("-fx-background-color: #ffc885; -fx-border-color: #9c754f;");
            button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #ffd5a1; -fx-border-color: #9c754f;"));
            button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #ffc885; -fx-border-color: #9c754f;"));

            HBox textsHbox = new HBox();

            textsHbox.setMouseTransparent(true);
            textsHbox.setPrefWidth(1126);
            textsHbox.setPrefHeight(43);
            textsHbox.setLayoutX(7);


            Label sender = new Label(email.getSender().getEmail());
            sender.setPrefWidth(285);
            sender.setPrefHeight(35);
            sender.setFont(Font.font("roboto", FontWeight.BOLD, FontPosture.REGULAR, 17));
            sender.setTextFill(Color.web("0x383838"));

            Label subject = new Label(email.getSubject());
            subject.setPrefWidth(397);
            subject.setPrefHeight(35);
            subject.setFont(Font.font("roboto", FontWeight.NORMAL, FontPosture.REGULAR, 17));
            subject.setTextFill(Color.web("0x383838"));

            Label date = new Label(getRelativeTime(email));
            date.setAlignment(Pos.CENTER_RIGHT);
            date.setPrefWidth(412);
            date.setPrefHeight(35);
            date.setFont(Font.font("roboto", FontWeight.LIGHT, FontPosture.ITALIC, 17));
            date.setTextFill(Color.web("0x383838", 0.5));

            date.setOpacity(25);

            Label[] labels = {sender, subject, date};

            textsHbox.getChildren().addAll(labels);

            bGroup.getChildren().add(button);
            bGroup.getChildren().add(textsHbox);

            button.setOnAction(e -> {
                try {
                    viewEmail(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            emailListBox.getChildren().add(bGroup);

            eButtons.add(button);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayFolder(inbox);
    }

    public String getRelativeTime(Email email)
    {
        String dt = email.getDateTime();

        int year = Integer.parseInt(dt.substring(0, 4));
        int month = Integer.parseInt(dt.substring(5, 7));
        int day = Integer.parseInt(dt.substring(8, 10));
        int hour = Integer.parseInt(dt.substring(11, 13));
        int minute = Integer.parseInt(dt.substring(14, 16));

        LocalDateTime sentTime = LocalDateTime.of(year, month, day, hour, minute);
        LocalDateTime now = LocalDateTime.now();

        long minutesago = ChronoUnit.MINUTES.between(sentTime, now);
        long hoursago = ChronoUnit.HOURS.between(sentTime, now);
        long daysago = ChronoUnit.DAYS.between(sentTime, now);
        long months = ChronoUnit.MONTHS.between(sentTime, now);
        long years = ChronoUnit.YEARS.between(sentTime, now);

        if(hoursago < 1) return minutesago + " minuts ago";
        if(daysago == 0 && hoursago < 12) return hoursago + " hours ago";
        if(daysago == 00 && hoursago >= 12) return "today";
        if(daysago == 1) return "yesterday";
        if(daysago >= 2 && daysago <= 6) return "few days ago";
        if(daysago >= 7 && daysago <= 13) return "last week";
        if(daysago >= 14 && daysago <= 30) return "few weeks ago";
        if(months == 1) return "last month";

        return sentTime.toString();
    }
}
