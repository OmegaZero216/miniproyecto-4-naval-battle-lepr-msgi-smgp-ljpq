package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameResult;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.PlayerStats;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;

/** Controller for the end-of-game results screen. */
public class ResultsController {

    @FXML private Label outcomeLabel;
    @FXML private Label gameStatsLabel;
    @FXML private Label historicalStatsLabel;
    @FXML private Button playAgainButton;
    @FXML private Button titleButton;

    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /** Receives both this game's result and the nickname's updated historical totals. */
    public void setResult(GameResult result, PlayerStats stats) {
        outcomeLabel.setText(result.playerWon() ? "¡Victoria, " + stats.getNickname() + "!" : "Derrota");
        gameStatsLabel.setText(
                "Esta partida — Disparos: " + result.shotsFired()
                        + " | Barcos hundidos por ti: " + result.shipsSunkByPlayer()
                        + " | Barcos hundidos por la máquina: " + result.shipsSunkByMachine()
        );
        historicalStatsLabel.setText(
                "Historial de " + stats.getNickname() + " — Ganadas: " + stats.getGamesWon()
                        + " | Perdidas: " + stats.getGamesLost()
                        + " | Total disparos: " + stats.getTotalShotsFired()
                        + " | Total barcos hundidos: " + stats.getTotalShipsSunkByPlayer()
        );
    }

    @FXML
    private void handlePlayAgain() {
        sceneManager.navigateToTitle(); // Pide nickname de nuevo — puede ser otro jugador.
    }

    @FXML
    private void handleBackToTitle() {
        sceneManager.navigateToTitle();
    }
}
