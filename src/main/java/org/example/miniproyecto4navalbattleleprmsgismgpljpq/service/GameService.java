package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameOverState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.MachineTurnState;
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

    /** Manual DI: both boards are injected, GameService does not build them. */
    public GameService(Board playerBoard, Board machineBoard) {
        this.playerBoard = playerBoard;
        this.machineBoard = machineBoard;
        this.currentState = new PlayerTurnState();
    }

    public GameService(Board playerBoard, Board machineBoard, GameState restoredState) {
        this.playerBoard = playerBoard;
        this.machineBoard = machineBoard;
        this.currentState = restoredState;
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
        machineBoard.registerShot(coordinate);

        boolean hit = machineBoard.getCell(coordinate).isOccupied();
        machineBoard.getCell(coordinate).setState(hit ? CellState.HIT : CellState.MISS);

        if (hit) {
            registerHitOnShip(machineBoard, coordinate);
        }

        if (machineBoard.isFleetSunk()) {
            currentState = new GameOverState();
        } else if (!hit) {
            currentState = new MachineTurnState();
        }
        // Si "hit" es true y la flota no está hundida, el turno CONTINÚA
        // (regla del enunciado: tocado/hundido → sigue disparando), por
        // lo tanto currentState NO cambia.
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
