import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {
    private int id;
    private String login;
    private String email;

    ArrayList<Email> receivedEmails;

    User(int id, String login, String email){
        setId(id);
        setLogin(login);
        setEmail(email);
    }

    public static User login(String login, String password) {
        return null;
    }



    public static boolean validateLogin(String login) {
        String regex1 = "[^a-zA-Z0-9_-]";
        boolean valid = true;


        validateLoop:
        for (int i = 0; i < login.length(); i++) {
            if((login.charAt(i) + "").matches(regex1)) {valid = false; break validateLoop;}
        }
        if(valid) return login.length() >= 4 && login.length() <= 20;
        else return false;
    }

    public static boolean validateEmail(String email) {
        String regex1 = "[^a-zA-Z0-9_-]";
        boolean valid = true;


        validateLoop:
        for (int i = 0; i < email.length(); i++) {
            if((email.charAt(i) + "").matches(regex1)) {valid = false; break validateLoop;}
        }
        if(valid) return email.length() >= 4 && email.length() <= 20;
        else return false;
    }

    public static boolean validatePassword(String password) {
        String regex1 = "[^a-zA-Z0-9_!@#$%^&*()+/{}~<>,.:-]";
        boolean valid = true;

        validateLoop:
        for (int i = 0; i < password.length(); i++) {
            if((password.charAt(i) + "").matches(regex1)) {valid = false; break validateLoop;}
        }
        if(valid) return password.length() >= 8 && password.length() <= 25;
        else return false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) throws NegativeIDException{
        if(id < 0) {
            throw new NegativeIDException("User ID cannot be negative!");
        }
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
