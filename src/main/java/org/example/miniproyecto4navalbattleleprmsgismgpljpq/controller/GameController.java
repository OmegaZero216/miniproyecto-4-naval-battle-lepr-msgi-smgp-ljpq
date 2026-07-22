package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.event.CellClickAdapter;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.event.CellHoverAdapter;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameResult;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.CellShapeFactory;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;

import java.util.HashMap;
import java.util.Map;

/**
 * JavaFX Controller for the main game screen.
 * <p>
 * Contains ZERO game logic — it only: (1) renders GridPanes from Board
 * state, (2) forwards user input to GameService, (3) re-renders after
 * GameService responds. Every decision (is it a hit? did the fleet sink?)
 * happens inside GameService/Board, never here — the architectural rule
 * fixed since Stage 1.
 */
public class GameController {

    @FXML private HBox statusBar;
    @FXML private Label phaseLabel;
    @FXML private Label turnLabel;
    @FXML private Label debugModeLabel;
    @FXML private Label hintLabel;
    @FXML private GridPane playerGrid;
    @FXML private GridPane machineGrid;
    @FXML private StackPane machineBoardContainer;
    @FXML private AnchorPane gameOverOverlay;
    @FXML private Label gameOverLabel;
    @FXML private Button rotateButton;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button debugButton;

    private GameService gameService;
    private SceneManager sceneManager;
    private boolean debugMode = false;

    // Referencia a los nodos renderizados por coordenada, para poder
    // actualizar solo la celda que cambió sin redibujar todo el tablero.
    private final Map<Coordinate, Group> machineCellNodes = new HashMap<>();

    /**
     * Injected after FXMLLoader creates this controller (manual DI —
     * FXML controllers cannot use constructor injection, so this setter
     * is our compromise while keeping the dependency explicit).
     */

    public void setSceneManager (SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
        renderMachineBoard(gameService.getMachineBoard());
        renderPlayerBoard(gameService.getPlayerBoard());
    }

    private void renderMachineBoard(Board board) {
        machineGrid.getChildren().clear();
        machineCellNodes.clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Coordinate coordinate = new Coordinate(row, col);
                Group shape = CellShapeFactory.createShapeFor(
                        board.getCell(coordinate).getState());
                attachMachineCellEvents(shape, coordinate);
                machineCellNodes.put(coordinate, shape);
                machineGrid.add(shape, col, row);
            }
        }
    }

    private void renderPlayerBoard(Board board) {
        playerGrid.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Coordinate coordinate = new Coordinate(row, col);
                Group shape = CellShapeFactory.createShapeFor(
                        board.getCell(coordinate).getState());
                playerGrid.add(shape, col, row);
            }
        }
    }

    private void attachMachineCellEvents(Group shape, Coordinate coordinate) {
        // Click delega en GameService — el Controller no decide si es agua o impacto.
        shape.setOnMouseClicked(new CellClickAdapter(coordinate, this::handleCellClicked));
        // Hover da feedback inmediato (heurística de Nielsen: visibilidad de estado).
        shape.setOnMouseEntered(new CellHoverAdapter(shape, true));
        shape.setOnMouseExited(new CellHoverAdapter(shape, false));
    }

    private void handleCellClicked(Coordinate coordinate) {
        try {
            gameService.fireAt(coordinate);
            refreshMachineCell(coordinate);
            updateStatusLabels();
            checkGameOver();
        } catch (IllegalStateException e) {
            // Disparo inválido (ya disparado / fase incorrecta): feedback
            // visual al usuario en vez de un fallo silencioso — heurística
            // "ayudar a los usuarios a reconocer y recuperarse de errores".
            hintLabel.setText(e.getMessage());
        }
    }

    private void refreshMachineCell(Coordinate coordinate) {
        Group updated = CellShapeFactory.createShapeFor(
                gameService.getMachineBoard().getCell(coordinate).getState());
        attachMachineCellEvents(updated, coordinate);
        Group old = machineCellNodes.get(coordinate);
        int col = GridPane.getColumnIndex(old);
        int row = GridPane.getRowIndex(old);
        machineGrid.getChildren().remove(old);
        machineGrid.add(updated, col, row);
        machineCellNodes.put(coordinate, updated);
    }

    private void updateStatusLabels() {
        phaseLabel.setText("Fase: " + gameService.getCurrentState().getPhaseName());
    }

    private void checkGameOver() {
        if (gameService.getMachineBoard().isFleetSunk()) {
            GameResult result = new GameResult(true, /* shotsFired */ 0, 0, 0);
            sceneManager.navigateToResults(result);
        }
        // TODO: rama simétrica cuando gane la máquina (Etapa de IA/turnos completa)
    }
}