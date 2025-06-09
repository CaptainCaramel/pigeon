package com.ldal.pigeonapp;

import java.io.*;
import java.time.LocalDateTime;

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

    Email(){

    }


    public static String dateTimeToString(LocalDateTime localDateTime){
        String month = "" + localDateTime.getMonthValue();
        if(localDateTime.getMonthValue() < 10) month = "0" + month;

        String day = "" + localDateTime.getDayOfMonth();
        if(localDateTime.getMonthValue() < 10) day = "0" + day;

        String minute = "" + localDateTime.getMinute();
        if(localDateTime.getMinute() < 10) minute = "0" + minute;

        String hour = "" + localDateTime.getHour();
        if(localDateTime.getHour() < 10) hour = "0" + hour;

        return localDateTime.getYear() + "-" + month + "-" + day + " " + hour + ":" + minute;
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
