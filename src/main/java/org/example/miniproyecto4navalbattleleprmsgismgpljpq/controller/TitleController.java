package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.GamePersistenceException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.SaveNotFoundException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository.LoadManager;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;
import javafx.scene.control.Label;

/**
 * Controller for the title screen: Start (new game) and Load (saved game).
 * Contains no game logic — only navigation triggers, delegated to
 * SceneManager, and (later) loading a saved game via LoadManager.
 */
public class TitleController {

    @FXML private TextField nicknameField;
    @FXML private Label errorLabel;
    @FXML private Button startButton;
    @FXML private Button loadButton;
    @FXML private Label titleHintLabel;

    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void handleStart() {
        String nickname = readValidNickname();
        if (nickname == null) return;
        sceneManager.navigateToPreparation(nickname);
    }

    @FXML
    private void handleLoad() {
        String nickname = readValidNickname();
        if (nickname == null) return;
        try {
            var data = new LoadManager().load();
            GameService restored = GameService.fromSnapshot(data);
            sceneManager.navigateToGame(restored, nickname);
        } catch (GamePersistenceException | SaveNotFoundException e) {
            errorLabel.setText("No se pudo cargar: " + e.getMessage());
        }
    }

    /**
     * Validates the nickname is non-blank before letting the player
     * proceed — prevention of errors (Nielsen heuristic already
     * documented in Stage 6) applied to stat-tracking: an empty
     * nickname would otherwise silently corrupt/merge stats files.
     */
    private String readValidNickname() {
        String nickname = nicknameField.getText() == null ? "" : nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            errorLabel.setText("Por favor ingresa un nickname.");
            return null;
        }
        return nickname;
    }
}