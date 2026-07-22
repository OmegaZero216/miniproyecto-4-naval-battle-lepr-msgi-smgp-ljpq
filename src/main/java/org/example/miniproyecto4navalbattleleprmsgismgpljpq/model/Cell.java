package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;

import java.io.Serializable;

/**
 * Represents a single cell of the 10x10 board.
 * Holds only data and simple state transitions — no game rules
 * (those belong to the Service layer), keeping this class cohesive
 * and easy to unit test in isolation (BoardTest, in a later stage).
 * <p>
 * Implements {@link Serializable} so a whole {@code Board} can be
 * written to a binary save file cell by cell.
 */
public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Coordinate coordinate;
    private CellState state;

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.state = CellState.EMPTY;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isOccupied() {
        return state == CellState.SHIP || state == CellState.HIT || state == CellState.SUNK;
    }
}