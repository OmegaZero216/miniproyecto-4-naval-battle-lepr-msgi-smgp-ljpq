package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameSaveData;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.PlayerTurnState;

/**
 * Orchestrates a full game: owns both boards, the current game state
 * (via the State pattern), and delegates shot resolution rules.
 * <p>
 * This is the class a JavaFX Controller talks to — the Controller never
 * touches Board or Ship directly, only GameService's public methods.
 * That boundary is what keeps game logic out of the Controller
 * (architectural rule fixed in Stage 1).
 */
public class GameService {

    private final Board playerBoard;
    private final Board machineBoard;
    private GameState currentState;
    private boolean debugRevealUsed = false;
    private final java.util.List<GameStateListener> listeners = new java.util.ArrayList<>();

    /** Manual DI: both boards are injected, GameService does not build them. */
    public GameService(Board playerBoard, Board machineBoard) {
        this.playerBoard = playerBoard;
        this.machineBoard = machineBoard;
        this.currentState = new PlayerTurnState();
    }

    public void addListener(GameStateListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (GameStateListener listener : listeners) {
            listener.onStateChanged(currentState);
        }
    }

    public void markDebugRevealUsed() {
        this.debugRevealUsed = true;
    }

    public boolean isDebugRevealUsed() {
        return debugRevealUsed;
    }

    /**
     * Entry point the Controller calls when the player clicks a cell on
     * the machine's board. Delegates to the current GameState — the
     * Controller never decides whether shooting is currently allowed.
     */
    public void fireAt(Coordinate coordinate) {
        currentState.handleShot(this, coordinate);
    }

    /**
     * Resolves a shot fired by the player against the machine's board.
     * Package-visible-by-convention: called only by GameState
     * implementations, never directly by the Controller.
     */
    public void resolvePlayerShot(Coordinate coordinate) {
        if (machineBoard.hasShotAt(coordinate)) {
            throw new IllegalStateException("Coordinate already shot at: " + coordinate);
        }
        boolean hit = resolveShot(machineBoard, coordinate);

        if (machineBoard.isFleetSunk()) {
            currentState = new org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameOverState();
        } else if (!hit) {
            currentState = new org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.MachineTurnState();
        }
        notifyListeners();
    }

    /**
     * Resolves a shot fired BY the machine against the player's board.
     * Reuses the exact same resolveShot() logic as the player's shots —
     * this was the missing piece: previously AIService mutated the
     * board directly, duplicating rules that must live in exactly one
     * place (this class) to stay consistent.
     */
    public boolean resolveMachineShot(Coordinate coordinate) {
        boolean hit = resolveShot(playerBoard, coordinate);

        if (playerBoard.isFleetSunk()) {
            currentState = new org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameOverState();
        } else if (!hit) {
            currentState = new org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.PlayerTurnState();
        }
        // Si hit==true y la flota no está hundida, currentState sigue
        // siendo MACHINE_TURN — la IA dispara de nuevo (misma regla que
        // aplicamos al jugador: tocado/hundido continúa disparando).
        notifyListeners();
        return hit;
    }

    /** Shared resolution logic, extracted to avoid duplicating rules between player and machine shots. */
    private boolean resolveShot(Board targetBoard, Coordinate coordinate) {
        targetBoard.registerShot(coordinate);
        boolean hit = targetBoard.getCell(coordinate).isOccupied();
        targetBoard.getCell(coordinate).setState(hit ? CellState.HIT : CellState.MISS);
        if (hit) {
            registerHitOnShip(targetBoard, coordinate);
        }
        return hit;
    }

    private void registerHitOnShip(Board board, Coordinate coordinate) {
        for (Ship ship : board.getShips().values()) {
            if (ship.getOccupiedCoordinates().contains(coordinate)) {
                ship.registerHit(coordinate);
                if (ship.isSunk()) {
                    markShipCellsAsSunk(board, ship);
                }
                break;
            }
        }
    }

    private void markShipCellsAsSunk(Board board, Ship ship) {
        for (Coordinate coordinate : ship.getOccupiedCoordinates()) {
            board.getCell(coordinate).setState(CellState.SUNK);
        }
    }

    /** Produces an immutable snapshot suitable for persistence. */
    public GameSaveData createSnapshot() {
        return new GameSaveData(playerBoard, machineBoard, currentState.getPhaseName(), debugRevealUsed);
    }

    /**
     * Restores this GameService's boards and phase from a previously
     * saved snapshot. Package-private-by-convention: called only right
     * after construction, from Main/SceneManager's loading flow.
     */
    public static GameService fromSnapshot(GameSaveData data) {
        GameService restored = new GameService(data.playerBoard(), data.machineBoard());
        restored.currentState = org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameStateFactory
                .fromPhaseName(data.currentPhaseName());
        restored.debugRevealUsed = data.debugRevealUsed();
        return restored;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getMachineBoard() {
        return machineBoard;
    }
}
