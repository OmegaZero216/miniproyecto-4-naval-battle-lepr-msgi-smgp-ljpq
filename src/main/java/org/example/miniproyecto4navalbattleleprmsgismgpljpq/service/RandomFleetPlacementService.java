package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.InvalidShipPlacementException;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Places an entire fleet randomly on a Board — used exclusively for the
 * machine's board, since the player places ships interactively via
 * PreparationController instead.
 * <p>
 * Reuses PlacementService for validation rather than duplicating the
 * overlap/adjacency rules: this is exactly why we extracted those rules
 * into their own class in Stage 4 (DRY, low coupling).
 */
public class RandomFleetPlacementService {

    private final PlacementService placementService;
    private final Random random = new Random();

    public RandomFleetPlacementService(PlacementService placementService) {
        this.placementService = placementService;
    }

    public void placeFleetRandomly(Board board) {
        Deque<ShipType> fleetOrder = FleetFactory.generateFleetOrder();
        for (ShipType type : fleetOrder) {
            placeOneShipRandomly(board, type);
        }
    }

    private void placeOneShipRandomly(Board board, ShipType type) {
        boolean placed = false;
        while (!placed) {
            Orientation orientation = random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            int maxStart = GameConfig.BOARD_SIZE - type.getSize();
            int row = random.nextInt(GameConfig.BOARD_SIZE);
            int col = random.nextInt(GameConfig.BOARD_SIZE);

            // Ajusta el punto de inicio para que el barco no se salga del tablero.
            if (orientation == Orientation.HORIZONTAL) {
                col = Math.min(col, maxStart);
            } else {
                row = Math.min(row, maxStart);
            }

            try {
                Ship ship = buildShipAt(type, orientation, row, col);
                placementService.placeShip(ship);
                placed = true;
            } catch (InvalidShipPlacementException ignored) {
                // Intento inválido (choque/adyacencia) — se reintenta con
                // otra posición aleatoria. Esperado y normal, no es un error real.
            }
        }
    }

    private Ship buildShipAt(ShipType type, Orientation orientation, int startRow, int startCol) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < type.getSize(); i++) {
            int row = orientation == Orientation.VERTICAL ? startRow + i : startRow;
            int col = orientation == Orientation.HORIZONTAL ? startCol + i : startCol;
            coordinates.add(new Coordinate(row, col));
        }
        Ship.ShipBuilder builder = new Ship.ShipBuilder().ofType(type).withOrientation(orientation);
        coordinates.forEach(builder::addCoordinate);
        return builder.build();
    }
}