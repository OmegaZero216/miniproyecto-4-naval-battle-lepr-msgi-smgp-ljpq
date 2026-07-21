package org.example.miniproyecto4navalbattleleprmsgismgpljpq;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point of the Battle Ship application.
 * <p>
 * This class only bootstraps JavaFX. It intentionally contains no game logic:
 * its single responsibility is to start the application and hand control
 * over to the first Controller, following the Single Responsibility Principle.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // La carga real de la primera vista (FXML) se implementará
        // en la etapa de GUI. Por ahora dejamos el Stage listo para
        // verificar que JavaFX arranca correctamente.
        primaryStage.setTitle("Battleship");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
