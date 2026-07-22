package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;

/**
 * Controller for the ship-placement screen (HU1). Currently a skeleton:
 * ship placement interaction (drag/click + rotate) is implemented in the
 * next stage. Already wired to navigate forward once placement is done.
 */
public class PreparationController {

    @FXML
    private GridPane placementGrid;
    @FXML private Button rotateButton;
    @FXML private Button confirmButton;

    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void handleConfirm() {
        //validar flota completa antes de avanzar.
        Board playerBoard = new Board();   // placeholder — vendrá de la interacción real
        Board machineBoard = new Board();  // placeholder — la IA colocará su flota aquí
        GameService gameService = new GameService(playerBoard, machineBoard);
        sceneManager.navigateToGame(gameService);
    }
}
