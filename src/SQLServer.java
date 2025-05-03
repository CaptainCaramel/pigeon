import java.sql.*;

public class SQLServer {
    String url = "jdbc:mysql://localhost:3306/pigeonDB";
    String userName = "root";
    String password = "Iamme113";

    private Connection connection;
    private PreparedStatement signUpStatement;
    private final Statement statement;

    SQLServer(){
        try {
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();

            signUpStatement = connection.prepareStatement("Insert into user " +
                    "values(?, ?, ?, ?)");


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

    public void changeSignUpStatement(String login, String email, String hashedPass, String recoveryPass){
        try {
            signUpStatement.setString(1, login);
            signUpStatement.setString(2, email);
            signUpStatement.setString(3, hashedPass);
            signUpStatement.setString(4, recoveryPass);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
