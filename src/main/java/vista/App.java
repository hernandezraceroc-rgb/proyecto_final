package vista;

import aplicacion.Sesion;
import java.io.IOException;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicación UniShop.
 * Carga login.fxml al iniciar y cambia a catalogo.fxml cuando el login es correcto.
 */
public class App extends Application {

    private static final String RUTA_FXML = "/com/mycompany/proyecto_final_virtual/";
    private static Stage escenario;

    @Override
    public void start(Stage stage) throws IOException {
        escenario = stage;
        stage.setTitle("UniShop");
        mostrarLogin();
        stage.show();
    }

    /** Muestra la pantalla de inicio de sesión. */
    public static void mostrarLogin() {
        cargar("login.fxml", controller -> { }, 849, 557);
    }

    /** Muestra el catálogo para el usuario que acaba de iniciar sesión. */
    public static void abrirCatalogo(Sesion sesion) {
        cargar("catalogo.fxml",
                controller -> ((CatalogoController) controller).setSesion(sesion),
                900, 760);
    }

    private static void cargar(String archivo, Consumer<Object> configurar,
                               double ancho, double alto) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(RUTA_FXML + archivo));
            Parent raiz = loader.load();
            configurar.accept(loader.getController());
            escenario.setScene(new Scene(raiz, ancho, alto));
            escenario.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("UniShop");
            alerta.setHeaderText("No se pudo abrir la pantalla " + archivo);
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
