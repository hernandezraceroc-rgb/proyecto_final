package vista;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static final String RUTA_VISTAS = "/com/mycompany/proyecto_final_virtual/";

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 1000, 750);
        stage.setTitle("UniShop");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static FXMLLoader cargarLoader(String fxml) {
        return new FXMLLoader(App.class.getResource(RUTA_VISTAS + fxml + ".fxml"));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return cargarLoader(fxml).load();
    }

    public static void main(String[] args) {
        launch();
    }

}
