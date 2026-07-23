package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.InvalidShipPlacementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PlacementService}.
 * <p>
 * Validates that the service correctly enforces placement rules:
 * no overlapping, no adjacency, and within board boundaries.
 * Uses a checked exception for expected failures.
 */
class PlacementServiceTest {

    @Test
    @DisplayName("Should place a ship when coordinates are valid")
    void testValidPlacement() throws InvalidShipPlacementException {
        Board board = new Board();
        PlacementService service = new PlacementService(board);
        Ship ship = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();

        service.placeShip(ship);

        assertTrue(board.getCell(new Coordinate(0, 0)).isOccupied());
    }

    @Test
    @DisplayName("Should throw InvalidShipPlacementException when placing overlapping ships")
    void testInvalidPlacementOverlap() {
        Board board = new Board();
        PlacementService service = new PlacementService(board);
        Ship ship1 = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();
        Ship ship2 = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();

        assertThrows(InvalidShipPlacementException.class, () -> {
            service.placeShip(ship1);
            service.placeShip(ship2);
        });
    }

    @Test
    @DisplayName("Should throw InvalidShipPlacementException when ships are adjacent")
    void testInvalidPlacementAdjacent() {
        Board board = new Board();
        PlacementService service = new PlacementService(board);
        Ship ship1 = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();
        Ship ship2 = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 1))
                .build();

        assertThrows(InvalidShipPlacementException.class, () -> {
            service.placeShip(ship1);
            service.placeShip(ship2);
        });
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when creating a coordinate out of bounds")
    void testCoordinateOutOfBounds() {
        // The Coordinate constructor validates bounds, so this test covers
        // the unchecked exception thrown for invalid coordinates.
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(10, 0));
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(-1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(5, 10));
    }
}