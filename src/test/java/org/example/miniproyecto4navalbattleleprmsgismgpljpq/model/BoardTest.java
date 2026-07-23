package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
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
class BoardTest {

    @Test
    @DisplayName("Should place a ship and mark its cells as occupied")
    void testPlaceShip() {
        // given
        Board board = new Board();
        Ship ship = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();

        // when
        board.placeShip(ship);

        // then
        assertTrue(board.getCell(new Coordinate(0, 0)).isOccupied());
        assertEquals(CellState.SHIP, board.getCell(new Coordinate(0, 0)).getState());
    }

    @Test
    @DisplayName("Should register a hit and mark the ship as sunk when all cells are hit")
    void testRegisterShotHit() {
        // given
        Board board = new Board();
        Ship ship = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();
        board.placeShip(ship);

        // when
        board.registerShot(new Coordinate(0, 0));
        board.getCell(new Coordinate(0, 0)).setState(CellState.HIT);
        ship.registerHit(new Coordinate(0, 0));

        // then
        assertTrue(ship.isSunk());
        assertTrue(board.isFleetSunk());
    }

    @Test
    @DisplayName("Should register a miss and mark the cell as MISS")
    void testRegisterShotMiss() {
        // given
        Board board = new Board();

        // when
        board.registerShot(new Coordinate(0, 0));
        board.getCell(new Coordinate(0, 0)).setState(CellState.MISS);

        // then
        assertEquals(CellState.MISS, board.getCell(new Coordinate(0, 0)).getState());
        assertTrue(board.hasShotAt(new Coordinate(0, 0)));
    }

    @Test
    @DisplayName("Should detect fleet is not sunk until all ships are sunk")
    void testFleetSunkWithMultipleShips() {
        // given
        Board board = new Board();
        Ship ship1 = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();
        Ship ship2 = new Ship.ShipBuilder()
                .ofType(ShipType.DESTROYER)
                .withOrientation(Orientation.VERTICAL)
                .addCoordinate(new Coordinate(1, 0))
                .addCoordinate(new Coordinate(2, 0))
                .build();
        board.placeShip(ship1);
        board.placeShip(ship2);

        // when – sink only ship1
        board.registerShot(new Coordinate(0, 0));
        board.getCell(new Coordinate(0, 0)).setState(CellState.HIT);
        ship1.registerHit(new Coordinate(0, 0));

        // then
        assertFalse(board.isFleetSunk());

        // when – sink ship2
        board.registerShot(new Coordinate(1, 0));
        board.getCell(new Coordinate(1, 0)).setState(CellState.HIT);
        ship2.registerHit(new Coordinate(1, 0));
        board.registerShot(new Coordinate(2, 0));
        board.getCell(new Coordinate(2, 0)).setState(CellState.HIT);
        ship2.registerHit(new Coordinate(2, 0));

        // then
        assertTrue(board.isFleetSunk());
    }
}