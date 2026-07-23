package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.event.CellClickAdapter;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.event.CellHoverAdapter;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameResult;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.GamePersistenceException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository.SaveManager;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.ai.AIService;
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

    @FXML private Label phaseLabel;
    @FXML private Label turnLabel;
    @FXML private Label debugModeLabel;
    @FXML private Label hintLabel;
    @FXML private GridPane playerGrid;
    @FXML private GridPane machineGrid;
    @FXML private Button saveButton;
    @FXML private Button debugButton;

    private GameService gameService;
    private SceneManager sceneManager;
    private final SaveManager saveManager = new SaveManager();
    private boolean debugMode = false;
    private final AIService aiService = new AIService();


    // Referencia a los nodos renderizados por coordenada, para poder
    // actualizar solo la celda que cambió sin redibujar todo el tablero.
    private final Map<Coordinate, Group> machineCellNodes = new HashMap<>();

    @FXML
    private void initialize() {
        debugButton.setOnAction(e -> handleDebugReveal());
    }

    @FXML
    private void handleSaveButton() {
        autoSave();
        hintLabel.setText("Partida guardada manualmente.");
    }

    /**
     * Injected after FXMLLoader creates this controller (manual DI —
     * FXML controllers cannot use constructor injection, so this setter
     * is our compromise while keeping the dependency explicit).
     */

    public void setSceneManager (SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    public void shutdown() {
        aiService.shutdown();
    }
    /**
     * The ONLY entry point for a GameService into this screen — whether
     * it's a brand-new game (from Preparation) or a restored one (from
     * Title's "Cargar Partida"). Loading mid-game was deliberately
     * removed: AIService's TargetingStrategy queue is in-memory only
     * (documented simplification since Stage 8), so resuming a save
     * must always go through a fresh AIService and a single, explicit
     * state-check right after wiring the listener — never through an
     * ad-hoc reload while a machine turn might already be in flight on
     * the background thread.
     */
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
        renderMachineBoard(gameService.getMachineBoard());
        renderPlayerBoard(gameService.getPlayerBoard());
        debugButton.setDisable(gameService.isDebugRevealUsed());

        gameService.addListener(this::onGameStateChanged);
        onGameStateChanged(gameService.getCurrentState());
    }

    /** HU3: toggles visibility of the enemy fleet, satisfying the "modo de depuración" requirement explicitly asked in the assignment. */
    private void handleDebugReveal() {
        if (gameService.isDebugRevealUsed()) {
            return; // Defensa extra: el botón ya debería estar disabled.
        }

        debugMode = true;
        debugButton.setDisable(true); // Inutilizable de inmediato, no solo al terminar el timer.
        renderMachineBoard(gameService.getMachineBoard());
        debugModeLabel.setText("MODO DEBUG activo — ocultando en 5s...");

        gameService.markDebugRevealUsed();
        autoSave(); // Persistimos el "ya se usó" de inmediato, no solo al final del turno.

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(5));
        pause.setOnFinished(e -> {
            debugMode = false;
            renderMachineBoard(gameService.getMachineBoard());
            debugModeLabel.setText("");
        });
        pause.play();
    }

    private void onGameStateChanged(org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameState newState) {
        updateStatusLabels();
        if ("MACHINE_TURN".equals(newState.getPhaseName())) {
            triggerMachineTurn();
        }
    }

    private void triggerMachineTurn() {
        aiService.takeShot(gameService.getPlayerBoard(), coordinate -> {
            boolean hit = gameService.resolveMachineShot(coordinate);
            aiService.registerShotOutcome(coordinate, hit);
            refreshPlayerCell(coordinate);
            checkGameOver();
            autoSave();
            // onGameStateChanged ya fue llamado dentro de resolveMachineShot
            // (vía notifyListeners), así que si sigue en MACHINE_TURN,
            // triggerMachineTurn() se vuelve a disparar solo — no hace
            // falta un bucle manual aquí.
        });
    }

    /**
     * Maps a Board's real CellState to what should actually be rendered
     * for the machine's board: an un-hit SHIP must look like water
     * (EMPTY) unless debug mode is active. This is the single place
     * that decides visibility, so every render path (initial draw,
     * per-cell refresh, debug toggle) stays consistent — a duplicated
     * "if not debug, hide ships" check in three places would have been
     * exactly how this kind of bug slips in.
     */
    private CellState toVisibleState(CellState realState) {
        if (realState == CellState.SHIP && !debugMode) {
            return CellState.EMPTY;
        }
        return realState;
    }
    private void renderMachineBoard(Board board) {
        machineGrid.getChildren().clear();
        machineCellNodes.clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Coordinate coordinate = new Coordinate(row, col);
                CellState visible = toVisibleState(board.getCell(coordinate).getState());
                Group shape = CellShapeFactory.createShapeFor(visible);
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

    private void refreshPlayerCell(Coordinate coordinate) {
        Group updated = CellShapeFactory.createShapeFor(
                gameService.getPlayerBoard().getCell(coordinate).getState());
        playerGrid.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) == coordinate.row() && GridPane.getColumnIndex(node) == coordinate.column());
        playerGrid.add(updated, coordinate.column(), coordinate.row());
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
            autoSave(); // Guardado automático tras cada movimiento (requisito explícito).
        } catch (IllegalStateException e) {
            hintLabel.setText(e.getMessage());
        }
    }

    /**
     * Auto-saves after every resolved move, as explicitly required by
     * the assignment ("cada movimiento debe guardar automáticamente").
     * Failures are shown to the player but never interrupt gameplay —
     * losing the ability to resume later is recoverable; crashing
     * mid-game is not.
     */
    private void autoSave() {
        try {
            saveManager.save(gameService.createSnapshot());
        } catch (GamePersistenceException e) {
            hintLabel.setText("No se pudo guardar automáticamente: " + e.getMessage());
        }
    }

    private void refreshMachineCell(Coordinate coordinate) {
        CellState visible = toVisibleState(gameService.getMachineBoard().getCell(coordinate).getState());
        Group updated = CellShapeFactory.createShapeFor(visible);
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

    private void attachKeyboardShortcuts(javafx.scene.Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                autoSave();
                hintLabel.setText("Partida guardada manualmente.");
            }
        });
    }
}