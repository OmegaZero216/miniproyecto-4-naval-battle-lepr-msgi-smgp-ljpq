package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;
import javafx.scene.control.Label;

/**
 * Controller for the title screen: Start (new game) and Load (saved game).
 * Contains no game logic — only navigation triggers, delegated to
 * SceneManager, and (later) loading a saved game via LoadManager.
 */
public class TitleController {

    @FXML private Button startButton;
    @FXML private Button loadButton;
    @FXML private Label titleHintLabel;

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
        try {
            var data = new org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository.LoadManager().load();
            GameService restored = GameService.fromSnapshot(data);
            sceneManager.navigateToGame(restored);
        } catch (org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.GamePersistenceException
                 | org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.SaveNotFoundException e) {
            titleHintLabel.setText("No hay partida guardada para cargar.");
        }
    }
}