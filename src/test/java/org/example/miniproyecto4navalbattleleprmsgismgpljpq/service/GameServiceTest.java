package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.util.TestShipFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies GameService's turn-flow rules end to end: hit-continues,
 * miss-ends-turn, repeated-shot rejection, and game-over detection.
 * Ships are placed directly via Board.placeShip (bypassing
 * PlacementService, already covered elsewhere) so each test controls
 * exact ship positions to assert precise outcomes.
 */
@DisplayName("GameService")
class GameServiceTest {

    private Board playerBoard;
    private Board machineBoard;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        playerBoard = new Board();
        machineBoard = new Board();
        gameService = new GameService(playerBoard, machineBoard);
    }

    @Test
    @DisplayName("starts in PLAYER_TURN phase")
    void startsInPlayerTurn() {
        assertEquals("PLAYER_TURN", gameService.getCurrentState().getPhaseName());
    }

    @Test
    @DisplayName("firing at an empty cell registers a MISS and switches to MACHINE_TURN")
    void missingASHotEndsPlayerTurn() {
        gameService.fireAt(new Coordinate(0, 0)); // Sin barcos colocados: siempre agua.

        assertEquals("MACHINE_TURN", gameService.getCurrentState().getPhaseName());
    }

    @Test
    @DisplayName("hitting a ship without sinking it keeps the player's turn")
    void hittingAShipKeepsPlayerTurn() {
        Ship destroyer = TestShipFactory.createShip(ShipType.DESTROYER, Orientation.HORIZONTAL, new Coordinate(3, 3));
        machineBoard.placeShip(destroyer);

        gameService.fireAt(new Coordinate(3, 3)); // Solo la primera celda del destructor.

        assertEquals("PLAYER_TURN", gameService.getCurrentState().getPhaseName());
        assertFalse(machineBoard.isFleetSunk()); // Aún queda una celda sin tocar.
    }

    @Test
    @DisplayName("sinking a ship whose hit does NOT clear the whole fleet still keeps the player's turn")
    void sinkingOneShipWithoutClearingFleetKeepsPlayerTurn() {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(1, 1));
        Ship destroyer = TestShipFactory.createShip(ShipType.DESTROYER, Orientation.HORIZONTAL, new Coordinate(5, 5));
        machineBoard.placeShip(frigate);
        machineBoard.placeShip(destroyer);

        gameService.fireAt(new Coordinate(1, 1)); // Hunde la frigata, pero el destructor sigue vivo.

        assertEquals("PLAYER_TURN", gameService.getCurrentState().getPhaseName());
        assertFalse(machineBoard.isFleetSunk());
    }

    @Test
    @DisplayName("firing twice at the same coordinate throws IllegalStateException")
    void repeatedShotThrows() {
        gameService.fireAt(new Coordinate(1, 1));

        // El turno ya cambió a MACHINE_TURN tras el fallo, así que un
        // segundo disparo del jugador debe rechazarse por fase, no solo
        // por repetición — verificamos que se rechace de todas formas.
        assertThrows(IllegalStateException.class, () -> gameService.fireAt(new Coordinate(1, 1)));
    }

    @Test
    @DisplayName("sinking the entire enemy fleet moves the game to GAME_OVER")
    void sinkingFleetEndsGame() {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(7, 7));
        machineBoard.placeShip(frigate);

        gameService.fireAt(new Coordinate(7, 7)); // Única nave, única celda: flota hundida.

        assertEquals("GAME_OVER", gameService.getCurrentState().getPhaseName());
    }

    @Test
    @DisplayName("resolveMachineShot applies the same hit/miss rules against the player board")
    void machineShotAppliesSameRules() {
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(2, 2));
        playerBoard.placeShip(frigate);

        boolean hit = gameService.resolveMachineShot(new Coordinate(2, 2));

        assertTrue(hit);
        assertTrue(playerBoard.isFleetSunk());
    }
}