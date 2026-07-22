package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.persistence;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameState;

import java.io.Serializable;

/**
 * Represents an immutable snapshot of the game state for binary persistence.
 * <p>
 * Implements the Memento pattern to encapsulate both player boards and the current
 * game state without exposing internal logic to persistence services.
 */
public class GameSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Board playerBoard;
    private final Board machineBoard;
    private final GameState currentState;

    /**
     * Creates a new immutable game snapshot.
     *
     * @param playerBoard  The human player's board.
     * @param machineBoard The machine player's board.
     * @param currentState The current state/phase of the game.
     */
    public GameSnapshot(Board playerBoard, Board machineBoard, GameState currentState) {
        this.playerBoard = playerBoard;
        this.machineBoard = machineBoard;
        this.currentState = currentState;
    }

    /**
     * Gets the human player's board.
     *
     * @return The player's {@link Board}.
     */
    public Board getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Gets the machine player's board.
     *
     * @return The machine's {@link Board}.
     */
    public Board getMachineBoard() {
        return machineBoard;
    }

    /**
     * Gets the current game state.
     *
     * @return The active {@link GameState}.
     */
    public GameState getCurrentState() {
        return currentState;
    }
}