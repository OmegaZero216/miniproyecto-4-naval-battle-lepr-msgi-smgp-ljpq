package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.util.TestShipFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies Ship's own invariants: the Builder's validation rules, and
 * hit/sunk tracking. Distinct from PlacementServiceTest, which verifies
 * board-level rules (overlap, adjacency) — this class only cares about
 * a Ship's internal consistency, independent of any Board.
 */

@DisplayName("Ship (Builder + hit tracking)")
class ShipPlacementTest {

    @Test
    @DisplayName("build() succeeds when coordinate count matches the ship type's size")
    void buildsSuccessfullyWithCorrectCoordinateCount() {
        Ship carrier = TestShipFactory.createShip(
                ShipType.AIRCRAFT_CARRIER, Orientation.HORIZONTAL, new Coordinate(0, 0));

        assertEquals(4, carrier.getOccupiedCoordinates().size());
        assertEquals(ShipType.AIRCRAFT_CARRIER, carrier.getType());
    }

    @Test
    @DisplayName("build() throws when coordinate count does not match the ship type's size")
    void buildThrowsOnCoordinateCountMismatch() {
        Ship.ShipBuilder builder = new Ship.ShipBuilder()
                .ofType(ShipType.AIRCRAFT_CARRIER) // requiere 4
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .addCoordinate(new Coordinate(0, 1)); // solo 2

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("build() throws when type or orientation is missing")
    void buildThrowsWhenTypeOrOrientationMissing() {
        Ship.ShipBuilder missingOrientation = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .addCoordinate(new Coordinate(0, 0));

        assertThrows(IllegalStateException.class, missingOrientation::build);
    }

    @Test
    @DisplayName("a ship is not sunk until every one of its coordinates has been hit")
    void notSunkUntilAllCoordinatesHit() {
        Ship destroyer = TestShipFactory.createShip(ShipType.DESTROYER, Orientation.HORIZONTAL, new Coordinate(0, 0));

        destroyer.registerHit(new Coordinate(0, 0));
        assertFalse(destroyer.isSunk());

        destroyer.registerHit(new Coordinate(0, 1));
        assertTrue(destroyer.isSunk());
    }

    @Test
    @DisplayName("registering a hit on the same coordinate twice does not double-count it")
    void duplicateHitIsIgnored() {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(2, 2));

        frigate.registerHit(new Coordinate(2, 2));
        frigate.registerHit(new Coordinate(2, 2)); // repetido a propósito

        assertTrue(frigate.isSunk()); // Debe seguir hundido, no romperse por el duplicado.
    }
}
