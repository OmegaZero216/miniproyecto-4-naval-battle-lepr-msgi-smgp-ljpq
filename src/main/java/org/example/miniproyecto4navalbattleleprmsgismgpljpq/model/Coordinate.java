package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;
import java.io.Serializable;

/**
 * Immutable representation of a board position (row, column).
 * Implemented as a record (Java 17 feature) because a coordinate
 * has no behavior of its own — only identity and equality by value,
 * which records provide automatically (equals/hashCode/toString).
 * <p>
 * This immutability is essential: Coordinates are stored in a
 * {@code HashSet} to track shots already fired, so their hashCode
 * must never change after creation.
 * <p>
 * Implements {@link Serializable}: Coordinates are stored inside
 * Board/Ship/Cell, so the whole graph needs to be serializable for the
 * game state to be saved to a binary file.
 */
public record Coordinate(int row, int column) implements Serializable {

    private  static final long serialVersionUID = 1L;

    public Coordinate {
        if (row < 0 || row >= 10 || column < 0 || column >= 10) {
            throw new IllegalArgumentException(
                    "Coordinate out of bounds: (" + row + ", " + column + ")");
        }
    }
}