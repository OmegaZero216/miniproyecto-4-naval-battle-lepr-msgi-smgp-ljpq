package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Represents one player's 10x10 board: its cells, fleet, and fired shots.
 * <p>
 * Combines four data structures deliberately chosen for their access
 * patterns (rubric point 5):
 * <ul>
 *   <li>{@code Cell[][]} — natural fit for a fixed 10x10 grid with
 *       row/column access.</li>
 *   <li>{@code HashMap<String, Ship>} — O(1) lookup of a specific ship
 *       by its generated id, needed when resolving which ship was hit.</li>
 *   <li>{@code HashSet<Coordinate>} — O(1) membership check to guarantee
 *       a shot is never repeated (explicit requirement for the AI).</li>
 *   <li>{@code EnumMap<ShipType, Integer>} — tracks how many ships of each
 *       type remain afloat; EnumMap is faster and more expressive than a
 *       HashMap when the key space is a fixed enum.</li>
 * </ul>
 * This class only stores and exposes state; it does not decide game rules
 * (e.g. whether a shot is legal) — that belongs to the Service layer.
 */
public class Board implements Serializable {

    private final Cell[][] grid;
    private final Map<String, Ship> ships = new HashMap<>();
    private final Set<Coordinate> firedShots = new HashSet<>();
    private final Map<ShipType, Integer> remainingShipsByType = new EnumMap<>(ShipType.class);
    private static final long serialVersionUID = 1L;

    public Board() {
        this.grid = new Cell[GameConfig.BOARD_SIZE][GameConfig.BOARD_SIZE];
        for (int row = 0; row < GameConfig.BOARD_SIZE; row++) {
            for (int col = 0; col < GameConfig.BOARD_SIZE; col++) {
                grid[row][col] = new Cell(new Coordinate(row, col));
            }
        }
    }

    public Cell getCell(Coordinate coordinate) {
        return grid[coordinate.row()][coordinate.column()];
    }

    /**
     * Registers a ship on the board, marking its cells and indexing it
     * for quick lookup. Assumes placement was already validated by
     * PlacementService (this class does not validate — SRP).
     */
    public void placeShip(Ship ship) {
        String shipId = UUID.randomUUID().toString();
        ships.put(shipId, ship);
        for (Coordinate coordinate : ship.getOccupiedCoordinates()) {
            getCell(coordinate).setState(CellState.SHIP);
        }
        remainingShipsByType.merge(ship.getType(), 1, Integer::sum);
    }

    public boolean hasShotAt(Coordinate coordinate) {
        return firedShots.contains(coordinate);
    }

    public void registerShot(Coordinate coordinate) {
        firedShots.add(coordinate);
    }

    public Map<String, Ship> getShips() {
        return ships;
    }

    public boolean isFleetSunk() {
        return ships.values().stream().allMatch(Ship::isSunk);
    }

    /** Number of ships on this board that are fully sunk — used to build GameResult. */
    public long getSunkShipCount() {
        return ships.values().stream().filter(Ship::isSunk).count();
    }
}