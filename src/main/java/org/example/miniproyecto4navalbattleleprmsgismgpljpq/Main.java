package org.example.miniproyecto4navalbattleleprmsgismgpljpq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller.GameController;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;

import java.io.IOException;

/**
 * Entry point of the Battleship application.
 * <p>
 * Responsibilities kept intentionally minimal: load the FXML, build the
 * initial GameService with manually-injected Boards, and hand the
 * reference to the Controller. This is the one place in the whole
 * project where "new Board()" / "new GameService(...)" is allowed to
 * appear directly — everywhere else, dependencies arrive via
 * constructor or setter injection. This is what "Dependency Injection
 * manual" means in practice: one composition root, not a framework.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/miniproyecto4navalbattleleprmsgismgpljpq/fxml/game-view.fxml"));
        Parent root = loader.load();

        // Composition root: construimos el grafo de dependencias aquí,
        // no dentro de las clases que las usan.
        Board playerBoard = new Board();
        Board machineBoard = new Board();
        GameService gameService = new GameService(playerBoard, machineBoard);

        GameController controller = loader.getController();
        controller.setGameService(gameService);

        primaryStage.setTitle("Battleship");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
