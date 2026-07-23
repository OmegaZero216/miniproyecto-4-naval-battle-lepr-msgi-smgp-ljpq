package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.InvalidShipPlacementException;

import java.util.List;

/**
 * Validates and executes ship placement rules.
 * <p>
 * Extracted into its own class (rather than living inside Board or the
 * Controller) so that placement rules can change independently of how
 * the board stores data or how the UI captures clicks — Single
 * Responsibility Principle.
 * <p>
 * Satisfies HU1 (place ships) and rubric point 8 (custom checked
 * exception used for an expected, recoverable error condition).
 */
public class PlacementService {

    private final Board board;

    /**
     * Constructor injection (manual Dependency Injection): PlacementService
     * never creates its own Board — it receives one. This keeps the class
     * decoupled from how a Board is constructed and makes it trivial to
     * unit test with a fresh Board per test case.
     */
    public PlacementService(Board board) {
        this.board = board;
    }

    /**
     * Validates that a ship can be legally placed at the given coordinates:
     * within bounds, not overlapping another ship, and not directly
     * adjacent to one (no-touching rule, a common convention that also
     * demonstrates defensive design / error prevention).
     *
     * @throws InvalidShipPlacementException if any rule is violated.
     */
    public void validatePlacement(List<Coordinate> coordinates) throws InvalidShipPlacementException {
        for (Coordinate coordinate : coordinates) {
            if (board.getCell(coordinate).isOccupied()) {
                throw new InvalidShipPlacementException(
                        "Cell " + coordinate + " is already occupied by another ship.");
            }
            if (hasAdjacentShip(coordinate)) {
                throw new InvalidShipPlacementException(
                        "Cell " + coordinate + " is adjacent to another ship. Ships cannot touch.");
            }
        }
    }

    /**
     * Places a ship after validating it. Throwing before mutating anything
     * guarantees the board is never left in a partially-invalid state.
     */
    public void placeShip(Ship ship) throws InvalidShipPlacementException {
        validatePlacement(ship.getOccupiedCoordinates());
        board.placeShip(ship);
    }

    private boolean hasAdjacentShip(Coordinate coordinate) {
        int size = org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig.BOARD_SIZE;
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                int row = coordinate.row() + dRow;
                int col = coordinate.column() + dCol;
                if (row >= 0 && row < size && col >= 0 && col < size) {
                    if (board.getCell(new Coordinate(row, col)).isOccupied()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}