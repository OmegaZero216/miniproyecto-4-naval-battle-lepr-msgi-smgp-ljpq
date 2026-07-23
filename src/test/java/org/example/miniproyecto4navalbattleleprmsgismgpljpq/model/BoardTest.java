package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.util.TestShipFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Board} class.
 * <p>
 * Verifies ship placement, shot registration, and fleet sinking logic.
 * These tests ensure that the board maintains a consistent state
 * after each operation and correctly reflects the game rules.
 */
@DisplayName("Board")
class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    @DisplayName("all cells start as EMPTY")
    void allCellsStartEmpty() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(CellState.EMPTY, board.getCell(new Coordinate(row, col)).getState());
            }
        }
    }

    @Test
    @DisplayName("placing a ship marks its cells as SHIP and indexes it")
    void placingShipMarksCellsAndIndexesIt() {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(0, 0));
        board.placeShip(frigate);

        assertEquals(CellState.SHIP, board.getCell(new Coordinate(0, 0)).getState());
        assertEquals(1, board.getShips().size());
    }

    @Test
    @DisplayName("a coordinate is only reported as shot after registerShot is called")
    void tracksFiredShots() {
        Coordinate target = new Coordinate(3, 3);
        assertFalse(board.hasShotAt(target));

        board.registerShot(target);

        assertTrue(board.hasShotAt(target));
    }

    @Test
    @DisplayName("isFleetSunk is false while any ship has unhit cells")
    void fleetNotSunkWhileAnyShipAlive() {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(0, 0));
        board.placeShip(frigate);

        assertFalse(board.isFleetSunk());
    }

    @Test
    @DisplayName("isFleetSunk becomes true once every placed ship is sunk")
    void fleetSunkWhenAllShipsSunk() {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(0, 0));
        board.placeShip(frigate);

        frigate.registerHit(new Coordinate(0, 0)); // Frigata: 1 sola celda.

        assertTrue(board.isFleetSunk());
    }

    @Test
    @DisplayName("getSunkShipCount reflects only fully-sunk ships, not partially hit ones")
    void sunkShipCountCountsOnlyFullySunkShips() {
        Ship destroyer = TestShipFactory.createShip(ShipType.DESTROYER, Orientation.HORIZONTAL, new Coordinate(0, 0));
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(5, 5));
        board.placeShip(destroyer);
        board.placeShip(frigate);

        destroyer.registerHit(new Coordinate(0, 0)); // Solo 1 de 2 celdas: no hundido.
        frigate.registerHit(new Coordinate(5, 5));   // 1 de 1 celda: hundido.

        assertEquals(1, board.getSunkShipCount());
    }
}