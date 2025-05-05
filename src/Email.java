import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class Email implements Serializable
{
    private User sender, receiver;
    private String subject, text;
    private Media attachment;
    private String dateTime;

    @Serial
    private static final long serialVersionUID = 2009L;

    Email(User sender, User receiver, String text, String subject){
        setSender(sender);
        setReceiver(receiver);
        setText(text);
        setSubject(subject);
    }


    public static String dateTimeToString(LocalDateTime localDateTime){
        String month = "" + localDateTime.getMonthValue();
        if(localDateTime.getMonthValue() < 10) month = "0" + month;

        String day = "" + localDateTime.getDayOfMonth();
        if(localDateTime.getMonthValue() < 10) day = "0" + day;

        return localDateTime.getYear() + "-" + month + "-" + day + " " + localDateTime.getHour() + ":" + localDateTime.getMinute() + ":" + localDateTime.getSecond();
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Media getAttachment() {
        return attachment;
    }

    public void setAttachment(Media attachment) {
        this.attachment = attachment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "To : " + receiver.getEmail() + ";" +
                "\nSubject : " + subject +
                "\nAt : " + dateTime +
                "\n---------------------------\n" +
                text +
                "\n---EMAIL-END---";

    }
}
