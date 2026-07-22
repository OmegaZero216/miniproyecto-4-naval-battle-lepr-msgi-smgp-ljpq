package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;

/**
 * Controller for the title screen: Start (new game) and Load (saved game).
 * Contains no game logic — only navigation triggers, delegated to
 * SceneManager, and (later) loading a saved game via LoadManager.
 */
public class TitleController {

    @FXML private Button startButton;
    @FXML private Button loadButton;

    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void handleStart() {
        sceneManager.navigateToPreparation();
    }

    @FXML
    private void handleLoad() {
        // TODO Etapa de persistencia: cargar partida guardada vía LoadManager
        // y navegar directo a GAME con el GameService reconstruido.
    }
}
