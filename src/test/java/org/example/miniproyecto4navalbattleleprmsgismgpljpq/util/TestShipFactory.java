package org.example.miniproyecto4navalbattleleprmsgismgpljpq.util;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;

import java.util.ArrayList;
import java.util.List;

/**
 * Test-only helper that builds a valid Ship from a start coordinate,
 * type, and orientation. Exists purely to keep test classes focused on
 * what they're actually verifying, instead of repeating ship-assembly
 * boilerplate in every @Test method — the same DRY principle we apply
 * to production code, applied here to test code.
 */
public final class TestShipFactory {

    private TestShipFactory() {
    }

    public static Ship createShip(ShipType type, Orientation orientation, Coordinate start) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < type.getSize(); i++) {
            int row = orientation == Orientation.VERTICAL ? start.row() + i : start.row();
            int col = orientation == Orientation.HORIZONTAL ? start.column() + i : start.column();
            coordinates.add(new Coordinate(row, col));
        }
        Ship.ShipBuilder builder = new Ship.ShipBuilder().ofType(type).withOrientation(orientation);
        coordinates.forEach(builder::addCoordinate);
        return builder.build();
    }
}