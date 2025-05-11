import com.ldal.pigeonapp.PigeonApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class WelcomeScene
{
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void LogInButton(ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(PigeonApplication.class.getResource("LoginScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void SignUpButton(ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(PigeonApplication.class.getResource("SignUp.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
