package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.persistence;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameState;

import java.io.Serializable;

public class GameSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Board playerBoard;
    private final Board machineBoard;
    private final GameState currentState;

    public GameSnapshot(Board playerBoard, Board machineBoard, GameState currentState) {
        this.playerBoard = playerBoard;
        this.machineBoard = machineBoard;
        this.currentState = currentState;
    }

    public Board getPlayerBoard() { return playerBoard; }
    public Board getMachineBoard() { return machineBoard; }
    public GameState getCurrentState() { return currentState; }
}