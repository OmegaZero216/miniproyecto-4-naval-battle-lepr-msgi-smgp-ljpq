package org.example.miniproyecto4navalbattleleprmsgismgpljpq;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;


/**
 * Entry point. Its only job now is to create the SceneManager (the
 * composition root for navigation) and tell it to show the first screen.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.navigateToTitle();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
