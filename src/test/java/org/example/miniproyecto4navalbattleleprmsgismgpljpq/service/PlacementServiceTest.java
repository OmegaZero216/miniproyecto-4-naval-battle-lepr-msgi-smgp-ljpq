package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.InvalidShipPlacementException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.util.TestShipFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PlacementService}.
 * <p>
 * Validates that the service correctly enforces placement rules:
 * no overlapping, no adjacency, and within board boundaries.
 * Uses a checked exception for expected failures.
 */
@DisplayName("PlacementService")
class PlacementServiceTest {

    private Board board;
    private PlacementService placementService;

    @BeforeEach
    void setUp() {
        board = new Board();
        placementService = new PlacementService(board);
    }

    @Test
    @DisplayName("a ship placed on empty, non-adjacent cells is accepted")
    void acceptsValidPlacement() throws InvalidShipPlacementException {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(0, 0));

        assertDoesNotThrow(() -> placementService.placeShip(frigate));
        assertEquals(1, board.getShips().size());
    }

    @Test
    @DisplayName("rejects a ship overlapping another already-placed ship")
    void rejectsOverlappingShip() throws InvalidShipPlacementException {
        Ship first = TestShipFactory.createShip(ShipType.DESTROYER, Orientation.HORIZONTAL, new Coordinate(4, 4));
        placementService.placeShip(first);

        Ship overlapping = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(4, 4));

        assertThrows(InvalidShipPlacementException.class, () -> placementService.placeShip(overlapping));
    }

    @Test
    @DisplayName("rejects a ship directly adjacent to another ship (no-touching rule)")
    void rejectsAdjacentShip() throws InvalidShipPlacementException {
        Ship first = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(5, 5));
        placementService.placeShip(first);

        // (5,6) es adyacente diagonal/horizontal a (5,5): debe rechazarse.
        Ship adjacent = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(5, 6));

        assertThrows(InvalidShipPlacementException.class, () -> placementService.placeShip(adjacent));
    }

    @Test
    @DisplayName("accepts two ships that are far apart on the board")
    void acceptsTwoSeparatedShips() throws InvalidShipPlacementException {
        Ship first = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(0, 0));
        Ship second = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(9, 9));

        placementService.placeShip(first);
        assertDoesNotThrow(() -> placementService.placeShip(second));
        assertEquals(2, board.getShips().size());
    }

    @Test
    @DisplayName("validatePlacement does not mutate the board when it throws")
    void validationFailureDoesNotMutateBoard() throws InvalidShipPlacementException {
        Ship first = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(2, 2));
        placementService.placeShip(first);

        List<Coordinate> overlappingCoordinates = List.of(new Coordinate(2, 2));

        assertThrows(InvalidShipPlacementException.class,
                () -> placementService.validatePlacement(overlappingCoordinates));
        assertEquals(1, board.getShips().size()); // Sigue habiendo solo el barco original.
    }
}