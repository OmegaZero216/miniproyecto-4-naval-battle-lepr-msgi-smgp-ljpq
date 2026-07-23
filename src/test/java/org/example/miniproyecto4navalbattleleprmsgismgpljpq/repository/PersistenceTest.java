package org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.*;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.GamePersistenceException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.SaveNotFoundException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.util.TestShipFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the SaveManager/LoadManager round trip: what's saved must
 * come back equivalent (same ship count, same phase, same debug flag),
 * and loading with no save present must fail predictably.
 * <p>
 * Note: these tests write to the real GameConfig.SAVE_FILE_PATH (the
 * same file the running application uses), since the path isn't
 * currently injectable. Each test cleans up its own save file
 * afterward so it never leaks into a real play session or between
 * test runs — a known simplification, flagged rather than hidden.
 */
@DisplayName("Persistence (SaveManager + LoadManager)")
class PersistenceTest {

    private final SaveManager saveManager = new SaveManager();
    private final LoadManager loadManager = new LoadManager();

    @AfterEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(Path.of(GameConfig.SAVE_FILE_PATH));
    }

    @Test
    @DisplayName("loading with no save file present throws SaveNotFoundException")
    void loadingWithNoSaveThrows() throws IOException {
        Files.deleteIfExists(Path.of(GameConfig.SAVE_FILE_PATH));

        assertThrows(SaveNotFoundException.class, loadManager::load);
    }

    @Test
    @DisplayName("saveExists reflects whether a save file is currently present")
    void saveExistsReflectsFileState() throws GamePersistenceException, IOException {
        Files.deleteIfExists(Path.of(GameConfig.SAVE_FILE_PATH));
        assertFalse(saveManager.saveExists());

        saveManager.save(buildSampleSaveData());

        assertTrue(saveManager.saveExists());
    }

    @Test
    @DisplayName("a saved game's ship placement survives a save/load round trip")
    void shipPlacementSurvivesRoundTrip() throws GamePersistenceException {
        GameSaveData original = buildSampleSaveData();
        saveManager.save(original);

        GameSaveData loaded = loadManager.load();

        assertEquals(1, loaded.playerBoard().getShips().size());
        assertEquals(
                original.playerBoard().getCell(new Coordinate(0, 0)).getState(),
                loaded.playerBoard().getCell(new Coordinate(0, 0)).getState()
        );
    }

    @Test
    @DisplayName("the current game phase survives a save/load round trip")
    void gamePhaseSurvivesRoundTrip() throws GamePersistenceException {
        GameSaveData original = buildSampleSaveData();
        saveManager.save(original);

        GameSaveData loaded = loadManager.load();

        assertEquals(original.currentPhaseName(), loaded.currentPhaseName());
    }

    @Test
    @DisplayName("the one-time debug-reveal flag survives a save/load round trip when already used")
    void debugRevealFlagSurvivesRoundTrip() throws GamePersistenceException {
        GameSaveData original = new GameSaveData(new Board(), new Board(), "PLAYER_TURN", true);
        saveManager.save(original);

        GameSaveData loaded = loadManager.load();

        assertTrue(loaded.debugRevealUsed());
    }

    private GameSaveData buildSampleSaveData() {
        Board playerBoard = new Board();
        Ship frigate = TestShipFactory.createShip(ShipType.FRIGATE, Orientation.HORIZONTAL, new Coordinate(0, 0));
        playerBoard.placeShip(frigate);
        Board machineBoard = new Board();

        return new GameSaveData(playerBoard, machineBoard, "PLAYER_TURN", false);
    }
}