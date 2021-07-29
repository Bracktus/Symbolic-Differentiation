import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL resource = getClass().getClassLoader().getResource("Main.fxml");
        Parent root = FXMLLoader.load(resource);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Symbolic Differentiator");
        stage.show();
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
