import javax.xml.transform.Result;
import java.sql.*;

public class SQLServer {
    String url = "jdbc:mysql://localhost:3306/pigeondb";
    String userName = "root";
    String password = "Iamme113";

    private Connection connection;
    private PreparedStatement signUpStatement;
    private PreparedStatement checkLogin;
    private PreparedStatement checkPassword;
    private PreparedStatement getUserFromDB;
    private final Statement statement;

    SQLServer(){
        try {
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();

            signUpStatement = connection.prepareStatement("Insert into user(login, email, hashedpass, recoverypass) " +
                    "values(?, ?, ?, ?)");

            checkLogin = connection.prepareStatement("Select id from user where login = ?");
            checkPassword = connection.prepareStatement("Select hashedPass from user where login = ?");
            getUserFromDB = connection.prepareStatement("Select * from user where login = ?");
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


    public User SignUp(String login, String email, String hashedPass, String recoveryPass){
        try {
            signUpStatement.setString(1, login);
            signUpStatement.setString(2, email);
            signUpStatement.setString(3, hashedPass);
            signUpStatement.setString(4, recoveryPass);

            signUpStatement.execute();


            getUserFromDB.setString(1, login);
            ResultSet user = getUserFromDB.executeQuery();
            user.next();
            int id = user.getInt("id");

            return new User(id, login, email);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
