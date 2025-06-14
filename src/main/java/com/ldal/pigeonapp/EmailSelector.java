package com.ldal.pigeonapp;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmailSelector implements Initializable {
    @FXML
    private VBox emailListBox;
    @FXML
    private Button inboxButton;
    @FXML
    private Button draftsButton;
    @FXML
    private Button sentButton;
    @FXML
    private Button spamButton;
    @FXML
    private Button composerButton;
    @FXML
    private Button configureButton;
    @FXML
    private Button searchButton;
    ToolBarController toolBarController = new ToolBarController();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox sideBarVbox;
    @FXML
    private ChoiceBox<String> sorter;
    @FXML
    public Label declareText;
    @FXML
    private TextField searchinput;

    @FXML
    private void profilebutton(ActionEvent event) {
        toolBarController.ConMenu(event, anchorPane);
    }
    @FXML
    private Button folderClearer;

    public static int folderID;

    private ArrayList<Button> eButtons = new ArrayList<>();
    ArrayList<Email> inbox;
    ArrayList<Email> drafts;
    ArrayList<Email> sent;
    static ArrayList<Email> spam;
    static ArrayList<Email> deleted = new ArrayList<>();

    public ArrayList<Email> spamitout(ArrayList<Email> Inbox) {
        ArrayList<Email> spam = new ArrayList<>();
        for (Email e : Inbox) {
            if (Client.getSpamblacklist().contains(e.getSender().getLogin())) {
                spam.add(e);
            }
        }
        return spam;
    }

    @FXML
    private void viewEmail(ActionEvent actionEvent) throws IOException {
        Button clickedButton = (Button) actionEvent.getSource();

        ArrayList<Email> folder = new ArrayList<>();
        if (folderID == 0) {
            folder = inbox;
            declareText.setText("INBOX");
        } else if (folderID == 1) {
            declareText.setText("DRAFT");
            folder = drafts;
        } else if (folderID == 2) {
            folder = sent;
            declareText.setText("SENT");
        } else if (folderID == 3) {
            folder = spam;
            declareText.setText("SPAM");
        }

        for (int i = 0; i < eButtons.size(); i++) {
            if (clickedButton.equals(eButtons.get(i))) {
                EmailReader.email = folder.get(i);
                //if (!read.contains(folder.get(i)))
                //{read.add(folder.get(i));}
                break;
            }
        }

        Parent root = FXMLLoader.load(PigeonApplication.class.getResource("/EmailReader.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) (emailListBox.getScene().getWindow());
        stage.setScene(scene);

    }

    @FXML
    private void viewDraft(ActionEvent actionEvent) throws IOException {
        Button clickedButton = (Button) actionEvent.getSource();

        for (int i = 0; i < eButtons.size(); i++)
        {
            if (clickedButton.equals(eButtons.get(i)))
            {
                EmailComposer.draft = drafts.get(i);
                break;
            }
        }
        SideBarController.editDraft(actionEvent);
    }


    @FXML
    private void goToComposer(ActionEvent actionEvent) throws IOException {
        SideBarController.goToComposer(actionEvent);
    }


    @FXML
    private void switchFolder(ActionEvent actionEvent) {
        eButtons.clear();

        Button clickedButton = (Button) actionEvent.getSource();
        if (clickedButton.equals(inboxButton)) {
            folderID = 0;
            displayFolder(inbox);
            declareText.setText("INBOX");
            folderClearer.setText("");
            folderClearer.setDisable(true);
        }
        if (clickedButton.equals(draftsButton)) {
            folderID = 1;
            displayFolder(drafts);
            declareText.setText("DRAFTS");
            folderClearer.setText("Clear DRAFT out");
            folderClearer.setDisable(false);
            folderClearer.setOnAction(e ->
            {
                refreshFolder();
            });
        }
        if (clickedButton.equals(sentButton)) {
            folderID = 2;
            displayFolder(sent);
            declareText.setText("SENT");
            folderClearer.setText("");
            folderClearer.setDisable(true);
        }
        if (clickedButton.equals(spamButton)) {
            folderID = 3;
            displayFolder(spam);
            declareText.setText("SPAM");
            folderClearer.setText("");
            folderClearer.setDisable(true);
        }
    }

    @FXML
    public void refreshFolder() {
        inbox = Client.getInbox();
        drafts = Client.getDrafts();
        sent = Client.getSent();
        spam = spamitout(Client.getInbox());

        inbox.removeIf(e -> Client.getSpamblacklist().contains(e.getSender().getLogin()) || deleted.contains(e));
        drafts.removeIf(e -> !e.getSender().getLogin().equals(Client.getUser().getLogin()) || deleted.contains(e));
        sent.removeIf(e -> !e.getSender().getLogin().equals(Client.getUser().getLogin()) || deleted.contains(e));
        spam.removeIf(e -> !Client.getSpamblacklist().contains(e.getSender().getLogin()) || deleted.contains(e));

        if (folderID == 0) displayFolder(inbox);
        else if (folderID == 1) displayFolder(drafts);
        else if (folderID == 2) displayFolder(sent);
        else if (folderID == 3) displayFolder(spam);
    }

    private void displayFolder()
    {
        if (folderID == 0) displayFolder(inbox);
        else if (folderID == 1) displayFolder(drafts);
        else if (folderID == 2) displayFolder(sent);
        else if (folderID == 3) displayFolder(spam);
    }


    private void displayFolder(ArrayList<Email> folder) {
        sorter.setCursor(Cursor.HAND);
        eButtons.clear();
        emailListBox.getChildren().clear();
        if (folder != drafts) {
            folder = new ArrayList<>(folder.stream()
                    .sorted(new Comparator<Email>() {
                        @Override
                        public int compare(Email o1, Email o2) {
                            if (Objects.equals(sorter.getValue(), "Oldest"))
                                return Long.compare(o2.minutesAgo(), o1.minutesAgo());
                            else return Long.compare(o1.minutesAgo(), o2.minutesAgo());
                        }
                    }).collect(Collectors.toList()));
        }

        for (Email email : folder) {
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

            Label sender;
            if(folderID != 2)
            {
                sender = new Label(email.getSender().getEmail());
            }
            else
            {
                sender = new Label(email.getReceiver().getEmail());
            }
            sender.setPrefWidth(285);
            sender.setPrefHeight(35);
            sender.setFont(Font.font("roboto", FontWeight.BOLD, FontPosture.REGULAR, 17));
            sender.setTextFill(Color.web("0x383838"));

            /*
            StackPane stackPane = new StackPane();
            Button deleter = new Button();
            Image image = new Image("GarbagecanIcon.png");
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setMouseTransparent(true);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            stackPane.getChildren().addAll(deleter,imageView);
             */

            Label subject = new Label();

            subject.setText(email.getSubject());
            subject.setPrefWidth(397);
            subject.setPrefHeight(35);
            subject.setFont(Font.font("roboto", FontWeight.NORMAL, FontPosture.REGULAR, 17));
            subject.setTextFill(Color.web("0x383838"));

            Label date = new Label();
            if (email.getDateTime() != null) date.setText(getRelativeTime(email));
            date.setAlignment(Pos.CENTER_RIGHT);
            date.setPrefWidth(412);
            date.setPrefHeight(35);
            date.setFont(Font.font("roboto", FontWeight.LIGHT, FontPosture.ITALIC, 17));
            date.setTextFill(Color.web("0x383838", 0.5));

            date.setOpacity(25);

            Label[] labels = {sender, subject, date};

            textsHbox.getChildren().addAll(labels);
            //textsHbox.getChildren().add(stackPane);

            bGroup.getChildren().add(button);
            bGroup.getChildren().add(textsHbox);

            if (folderID != 1) {
                button.setOnAction(e -> {
                    try {
                        viewEmail(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            } else {
                button.setOnAction(e -> {
                    try {
                        viewDraft(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            emailListBox.getChildren().add(bGroup);

            eButtons.add(button);

            for(Button b : eButtons)
            {
                b.setCursor(Cursor.HAND);
            }
        }
    }

    @FXML
    public void searchbutton(ActionEvent event) {
        String search = searchinput.getText();
        ArrayList<Email> searchedmails = new ArrayList<>();
        ArrayList<Email> e = new ArrayList<>();
        if (folderID == 0) e = inbox;
        else if (folderID == 1) e = drafts;
        else if (folderID == 2) e = sent;
        else if (folderID == 3) e = spam;
        for (Email i : e) {
            if ((i.getSender().getLogin().toLowerCase()).contains(search.toLowerCase()) || (i.getSender().getEmail().toLowerCase()).contains(search.toLowerCase()) || (i.getSubject().toLowerCase()).contains(search.toLowerCase()) || (i.getText().toLowerCase()).contains(search.toLowerCase())) {
                searchedmails.add(i);
            }
        }
        //an aq
        displayFolder(searchedmails);
        //an display foldershi shignit
    }

    public String getRelativeTime(Email email) {
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

        if (daysago == 0) return dt.substring(11);
        if (daysago == 1) return "Yesterday";
        if (daysago >= 2 && daysago <= 6) return "A few days ago";
        if (daysago >= 7 && daysago <= 13) return "Last week";
        if (daysago >= 14 && daysago <= 30) return "A few weeks ago";
        if (months == 1) return "Last month";

        return sentTime.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshFolder();

        SideBarController.inboxButton = inboxButton;
        SideBarController.draftsButton = draftsButton;
        SideBarController.spamButton = spamButton;
        SideBarController.sentButton = sentButton;
        SideBarController.composerButton = composerButton;
        SideBarController.configureButton = configureButton;
        SideBarController.clearerButton = folderClearer;

        SideBarController.inboxButton.setCursor(Cursor.HAND);
        SideBarController.draftsButton.setCursor(Cursor.HAND);
        SideBarController.spamButton.setCursor(Cursor.HAND);
        SideBarController.sentButton.setCursor(Cursor.HAND);
        SideBarController.composerButton.setCursor(Cursor.HAND);
        SideBarController.configureButton.setCursor(Cursor.HAND);
        SideBarController.clearerButton.setCursor(Cursor.HAND);

        searchButton.setCursor(Cursor.HAND);

        SideBarController.initSideBar();

        displayFolder();

        sorter.getItems().addAll("Oldest", "Latest");
        sorter.setValue("Latest");

        sorter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
        {
            displayFolder();
        });
    }
}
