package com.ldal.pigeonapp;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SQLServer {
    String url = "jdbc:mysql://localhost:3306/pigeondb";
    String userName = "root";
    String password = "Iamme113";

    private Connection connection;
    private final PreparedStatement signUpStatement;
    private final PreparedStatement checkLogin;
    private final PreparedStatement checkPassword;
    private final PreparedStatement getUserFromLogin;
    private final PreparedStatement getUserFromEmail;
    private final PreparedStatement sendEmailNoAttachment;
    private final PreparedStatement getEmailFromID;
    private final PreparedStatement getUserFromID;
    private final Statement statement;

    public SQLServer(){
        try {
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();

            signUpStatement = connection.prepareStatement("Insert into user(login, email, hashedpass, recoverypass) " +
                    "values(?, ?, ?, ?)");

            checkLogin = connection.prepareStatement("Select id from user where login = ?");
            checkPassword = connection.prepareStatement("Select hashedPass from user where login = ?");
            getUserFromLogin = connection.prepareStatement("Select * from user where login = ?");
            getUserFromEmail = connection.prepareStatement("Select * from user where email = ?");
            getUserFromID = connection.prepareStatement("Select * from user where id = ?");

            sendEmailNoAttachment = connection.prepareStatement("Insert into mails(senderID, receiverID, emailText, sendTime, subject) " +
                    "values(?, ?, ?, ?, ?)");
            getEmailFromID = connection.prepareStatement("Select * from mails where receiverID = ?");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sqlStatement(String query){

        try {
            statement.execute(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean validateLogin(String login){
        try{
            checkLogin.setString(1, login);
            ResultSet dbResult = checkLogin.executeQuery();

            return dbResult.isBeforeFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validatePassword(String login, String c_hashedPass){
        try{
            checkPassword.setString(1, login);
            ResultSet dbResult = checkPassword.executeQuery();
            dbResult.next();
            return c_hashedPass.matches(dbResult.getString("hashedPass"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(int sender, int receiver, String text, String subject) {
        try {
            sendEmailNoAttachment.setInt(1, sender);
            sendEmailNoAttachment.setInt(2, receiver);
            sendEmailNoAttachment.setString(3, text);

            LocalDateTime localDateTime = LocalDateTime.now();
            String dateTime = Email.dateTimeToString(localDateTime);

            sendEmailNoAttachment.setString(4, String.valueOf(dateTime));
            sendEmailNoAttachment.setString(5, subject);
            sendEmailNoAttachment.execute();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public User userFromEmail(String email){
        try {
            getUserFromEmail.setString(1, email);
            ResultSet user = getUserFromEmail.executeQuery();
            user.next();
            if(!user.getBoolean("isAdmin"))return new User(user.getInt("id"), user.getString("login"), user.getString("email"));
            else return new Admin(user.getInt("id"), user.getString("login"), user.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public User userFromID(int id){
        try {
            getUserFromID.setInt(1, id);
            ResultSet user = getUserFromID.executeQuery();
            user.next();
            if(!user.getBoolean("isAdmin"))return new User(user.getInt("id"), user.getString("login"), user.getString("email"));
            else return new Admin(user.getInt("id"), user.getString("login"), user.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User userFromLogin(String login){
        try {
            getUserFromLogin.setString(1, login);
            ResultSet user = getUserFromLogin.executeQuery();
            user.next();
            if(!user.getBoolean("isAdmin"))return new User(user.getInt("id"), user.getString("login"), user.getString("email"));
            else return new Admin(user.getInt("id"), user.getString("login"), user.getString("email"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User SignUp(String login, String email, String hashedPass, String recoveryPass){
        try {
            signUpStatement.setString(1, login);
            signUpStatement.setString(2, email);
            signUpStatement.setString(3, hashedPass);
            signUpStatement.setString(4, recoveryPass);

            signUpStatement.execute();


            getUserFromLogin.setString(1, login);
            ResultSet user = getUserFromLogin.executeQuery();
            user.next();
            int id = user.getInt("id");

            return new User(id, login, email);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public User logIn(String login){
        try {
            getUserFromLogin.setString(1, login);
            ResultSet user = getUserFromLogin.executeQuery();
            user.next();
            int id = user.getInt("id");
            String email = user.getString("email");
            if(!user.getBoolean("isAdmin")) return new User(id, login, email);
            else return new Admin(id, login, email);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean validateUser(String email)
    {
        try
        {
            getUserFromEmail.setString(1, email);
            ResultSet dbResult = getUserFromEmail.executeQuery();
            return dbResult.isBeforeFirst();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Boolean validateID(int ID)
    {
        try
        {
            getUserFromID.setInt(1, ID);
            ResultSet dbResult = getUserFromID .executeQuery();
            return dbResult.isBeforeFirst();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Email> getInbox(int id){
        try {
            getEmailFromID.setInt(1, id);

            ResultSet dbResult = getEmailFromID.executeQuery();

            ArrayList<Email> inbox = new ArrayList<>();

            while(dbResult.next()){
                User sender = userFromID(dbResult.getInt("senderID"));
                User receiver = userFromID(dbResult.getInt("receiverID"));
                String text = dbResult.getString("emailText");
                String subject = dbResult.getString("subject");
                //Blob attachment = dbResult.getBlob("Attachemnt");
                Timestamp dateTime = dbResult.getTimestamp("sendTime");

                Email email = new Email(sender, receiver, text, subject);
                email.setDateTime(Email.dateTimeToString(dateTime.toLocalDateTime()));

                inbox.add(email);
            }

            return inbox;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
