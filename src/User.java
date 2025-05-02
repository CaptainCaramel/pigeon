import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User{
    private int id;
    private String login;
    private String email;

    User(int id, String login, String email){
        setId(id);
        setLogin(login);
        setEmail(email);
    }

    public static User login(String login, String password) {
        return null;
    }

    public static User signup(String login, String email) {
        return null;
    }


    public static boolean validateLogin(String login) {
        //String regex1 = "[^a-zA-Z0-9_]";

        //Pattern p = Pattern.compile(regex1);
        //Matcher m = p.matcher(login);

        //return !(m.matches()) && (login.length() >= 3 && login.length() <= 12);
        return false;
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

    //        if(login.matches(".[^1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_]") || login.length() < 4 || login.length() > 20)
    //        {
    //            throw new InvaildLoginException("Invalid Login! Login can only contain : a-z, A-Z, 0-9, _ and be 4-20 characters long");
    //        }

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
