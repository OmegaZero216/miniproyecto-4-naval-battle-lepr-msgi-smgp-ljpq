package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single ship placed on the board.
 * <p>
 * Instances are created exclusively through {@link ShipBuilder}, never via
 * a public constructor with many parameters. This is the Builder pattern:
 * it lets us assemble a Ship step by step (type, orientation, occupied
 * coordinates) while keeping the class itself immutable once built,
 * which prevents an inconsistent ship (e.g. wrong number of coordinates
 * for its type) from ever existing.
 * <p>
 * Satisfies HU1 (place ships): a Ship is the core object HU1 manipulates.
 * Implements {@link Serializable}
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ShipType type;
    private final Orientation orientation;
    private final List<Coordinate> occupiedCoordinates;
    private final List<Coordinate> hitCoordinates = new ArrayList<>();

    private Ship(ShipBuilder builder) {
        this.type = builder.type;
        this.orientation = builder.orientation;
        this.occupiedCoordinates = Collections.unmodifiableList(builder.coordinates);
    }

    public ShipType getType() {
        return type;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public List<Coordinate> getOccupiedCoordinates() {
        return occupiedCoordinates;
    }

    /**
     * Registers a hit at the given coordinate. Called by the Service layer
     * only — Ship never decides on its own when it is shot at.
     */
    public void registerHit(Coordinate coordinate) {
        if (occupiedCoordinates.contains(coordinate) && !hitCoordinates.contains(coordinate)) {
            hitCoordinates.add(coordinate);
        }
    }

    public boolean isSunk() {
        return hitCoordinates.size() == occupiedCoordinates.size();
    }

    /** Builder for {@link Ship}. See class-level Javadoc for rationale. */
    public static class ShipBuilder {
        private ShipType type;
        private Orientation orientation;
        private final List<Coordinate> coordinates = new ArrayList<>();

        public ShipBuilder ofType(ShipType type) {
            this.type = type;
            return this;
        }

        public ShipBuilder withOrientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public ShipBuilder addCoordinate(Coordinate coordinate) {
            this.coordinates.add(coordinate);
            return this;
        }

        /**
         * Builds the immutable Ship, validating consistency first.
         *
         * @throws IllegalStateException if the number of coordinates does not
         *         match the expected size for the given ship type.
         */
        public Ship build() {
            if (type == null || orientation == null) {
                throw new IllegalStateException("Ship type and orientation are required.");
            }
            if (coordinates.size() != type.getSize()) {
                throw new IllegalStateException(
                        "Ship of type " + type + " requires " + type.getSize()
                                + " coordinates, got " + coordinates.size());
            }
            return new Ship(this);
        }
    }
}