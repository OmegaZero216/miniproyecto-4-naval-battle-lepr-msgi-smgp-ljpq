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
 */
public record Coordinate(int row, int column) implements Serializable {

    public Coordinate {
        if (row < 0 || row >= 10 || column < 0 || column >= 10) {
            throw new IllegalArgumentException(
                    "Coordinate out of bounds: (" + row + ", " + column + ")");
        }
    }
}