package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameResult;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;

/** Controller for the end-of-game results screen. */
public class ResultsController {

    @FXML private Label outcomeLabel;
    @FXML private Label statsLabel;
    @FXML private Button playAgainButton;
    @FXML private Button titleButton;

    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setResult(GameResult result) {
        outcomeLabel.setText(result.playerWon() ? "¡VICTORIA!" : "DERROTA");
        outcomeLabel.getStyleClass().removeAll("victory", "defeat");
        outcomeLabel.getStyleClass().add(result.playerWon() ? "victory" : "defeat");
        statsLabel.setText("Disparos realizados: " + result.shotsFired());
    }

    @FXML
    private void handlePlayAgain() {
        sceneManager.navigateToPreparation();
    }

    @FXML
    private void handleBackToTitle() {
        sceneManager.navigateToTitle();
    }
}
