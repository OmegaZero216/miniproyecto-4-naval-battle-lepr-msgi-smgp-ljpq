package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.factory;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.InvalidShipPlacementException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.PlacementService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Factory class that centralizes how {@link Ship} instances are assembled
 * and how a complete, rule-compliant fleet of 10 ships is randomly generated.
 * <p>
 * Centralizes fleet composition according to {@link GameConfig} to ensure
 * single responsibility and avoid duplicate placement logic.
 */
public final class ShipFactory {

    private ShipFactory() {
        // Prevents instantiation — pure utility factory class.
    }

    /**
     * Builds a single {@link Ship} starting at the given origin coordinate
     * and extending according to its orientation.
     *
     * @param type        The type of ship to create.
     * @param orientation The layout orientation (HORIZONTAL or VERTICAL).
     * @param origin      The starting coordinate.
     * @return A new {@link Ship} instance populated with coordinates.
     */
    public static Ship createShip(ShipType type, Orientation orientation, Coordinate origin) {
        Ship.ShipBuilder builder = new Ship.ShipBuilder()
                .ofType(type)
                .withOrientation(orientation);

        for (int i = 0; i < type.getSize(); i++) {
            int row = origin.row();
            int column = origin.column();

            if (orientation == Orientation.VERTICAL) {
                row += i;
            } else {
                column += i;
            }

            builder.addCoordinate(new Coordinate(row, column));
        }

        return builder.build();
    }

    /**
     * Builds and places a complete, rule-compliant fleet of 10 ships
     * using random positions and orientations.
     *
     * @param placementService Service used to validate and execute ship placement.
     * @return A list containing all successfully placed ships for the fleet.
     * @throws InvalidShipPlacementException If any required ship cannot be placed.
     */
    public static List<Ship> createRandomFleet(PlacementService placementService)
            throws InvalidShipPlacementException {
        List<Ship> fleet = new ArrayList<>();

        fleet.addAll(placeShipsOfType(ShipType.AIRCRAFT_CARRIER, GameConfig.AIRCRAFT_CARRIER_COUNT, placementService));
        fleet.addAll(placeShipsOfType(ShipType.SUBMARINE, GameConfig.SUBMARINE_COUNT, placementService));
        fleet.addAll(placeShipsOfType(ShipType.DESTROYER, GameConfig.DESTROYER_COUNT, placementService));
        fleet.addAll(placeShipsOfType(ShipType.FRIGATE, GameConfig.FRIGATE_COUNT, placementService));

        return fleet;
    }

    /**
     * Places the specified quantity of ships of a given type.
     */
    private static List<Ship> placeShipsOfType(ShipType type, int count, PlacementService placementService)
            throws InvalidShipPlacementException {
        List<Ship> ships = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Ship placedShip = tryToPlaceSingleRandomShip(type, placementService);
            ships.add(placedShip);
        }

        return ships;
    }

    /**
     * Attempts to place a single ship using random coordinates and orientation,
     * retrying up to 200 times if collisions or out-of-bounds errors occur.
     *
     * @param type             The type of ship to place.
     * @param placementService The placement service for validation.
     * @return The successfully placed ship.
     * @throws InvalidShipPlacementException If the placement fails after max attempts.
     */
    private static Ship tryToPlaceSingleRandomShip(ShipType type, PlacementService placementService)
            throws InvalidShipPlacementException {
        Random random = new Random();
        final int maxAttempts = 200;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Orientation orientation = getRandomOrientation(random);
            Coordinate origin = getRandomOriginCoordinate(random, type, orientation);

            Ship candidate = createShip(type, orientation, origin);

            try {
                placementService.placeShip(candidate);
                return candidate;
            } catch (InvalidShipPlacementException retryable) {
                // Expected when overlapping — silently retry with a new position.
            }
        }

        throw new InvalidShipPlacementException(
                "Could not find a valid random placement for ship type " + type
                        + " after " + maxAttempts + " attempts.");
    }

    /**
     * Randomly selects a ship orientation.
     */
    private static Orientation getRandomOrientation(Random random) {
        if (random.nextBoolean()) {
            return Orientation.HORIZONTAL;
        } else {
            return Orientation.VERTICAL;
        }
    }

    /**
     * Calculates a random origin coordinate ensuring the ship stays within board bounds.
     */
    private static Coordinate getRandomOriginCoordinate(Random random, ShipType type, Orientation orientation) {
        int rowBound;
        int colBound;

        if (orientation == Orientation.VERTICAL) {
            rowBound = GameConfig.BOARD_SIZE - type.getSize();
            colBound = GameConfig.BOARD_SIZE - 1;
        } else {
            rowBound = GameConfig.BOARD_SIZE - 1;
            colBound = GameConfig.BOARD_SIZE - type.getSize();
        }

        int randomRow = random.nextInt(rowBound + 1);
        int randomCol = random.nextInt(colBound + 1);

        return new Coordinate(randomRow, randomCol);
    }
}