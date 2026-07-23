package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
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
        try {
            var data = new org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository.LoadManager().load();
            GameService restored = GameService.fromSnapshot(data);
            sceneManager.navigateToGame(restored);
        } catch (org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.GamePersistenceException
                 | org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.SaveNotFoundException e) {
            // TODO Etapa GUI final: mostrar un Label de error en Title
            // en vez de solo loguear — placeholder honesto por ahora.
            System.err.println("No se pudo cargar: " + e.getMessage());
        }
    }
}
