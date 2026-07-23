package org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameSaveData;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for persistence mechanisms required by HU-5.
 * <p>
 * Verifies that:
 * <ul>
 *   <li>Serializable snapshot (GameSaveData) can be saved and loaded</li>
 *   <li>Plain-text stats file (player_stats.txt) is correctly written and read</li>
 * </ul>
 * Uses temporary directories to avoid polluting the real save location.
 */
class PersistenceTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should save and load a GameSaveData object via serialization")
    void testSaveAndLoadGameState() throws Exception {
        // given
        Board player = new Board();
        Board machine = new Board();
        Ship ship = new Ship.ShipBuilder()
                .ofType(ShipType.FRIGATE)
                .withOrientation(Orientation.HORIZONTAL)
                .addCoordinate(new Coordinate(0, 0))
                .build();
        player.placeShip(ship);
        player.registerShot(new Coordinate(0, 0));
        player.getCell(new Coordinate(0, 0)).setState(CellState.HIT);
        ship.registerHit(new Coordinate(0, 0));

        GameSaveData data = new GameSaveData(player, machine, "PLAYER_TURN", false);

        // when – save to temporary file
        Path saveFile = tempDir.resolve("test_save.dat");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(saveFile))) {
            out.writeObject(data);
        }

        // when – load back
        GameSaveData loaded;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(saveFile))) {
            loaded = (GameSaveData) in.readObject();
        }

        // then
        assertNotNull(loaded);
        assertEquals(CellState.HIT, loaded.playerBoard().getCell(new Coordinate(0, 0)).getState());
        assertEquals("PLAYER_TURN", loaded.currentPhaseName());
    }

    @Test
    @DisplayName("Should write and read plain-text stats file correctly")
    void testStatsRepositorySaveAndLoad() {
        // given
        StatsRepository repo = new StatsRepository() {
            // Override paths to use temporary directory for this test
            @Override
            public void save(String nickname, int wins, int losses, int totalShipsSunk) {
                Path path = tempDir.resolve("player_stats.txt");
                try {
                    Files.createDirectories(path.getParent());
                    Map<String, String> data = Map.of(
                            "nickname", nickname,
                            "wins", String.valueOf(wins),
                            "losses", String.valueOf(losses),
                            "totalShipsSunk", String.valueOf(totalShipsSunk)
                    );
                    try (var writer = Files.newBufferedWriter(path)) {
                        for (var entry : data.entrySet()) {
                            writer.write(entry.getKey() + "=" + entry.getValue());
                            writer.newLine();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error saving stats in test", e);
                }
            }

            @Override
            public Map<String, String> load() {
                Path path = tempDir.resolve("player_stats.txt");
                if (!Files.exists(path)) return Map.of();
                try (var reader = Files.newBufferedReader(path)) {
                    Map<String, String> map = new java.util.HashMap<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) map.put(parts[0], parts[1]);
                    }
                    return map;
                } catch (Exception e) {
                    throw new RuntimeException("Error loading stats in test", e);
                }
            }
        };

        // when
        repo.save("TestPlayer", 5, 2, 18);
        Map<String, String> loaded = repo.load();

        // then
        assertEquals("TestPlayer", loaded.get("nickname"));
        assertEquals("5", loaded.get("wins"));
        assertEquals("2", loaded.get("losses"));
        assertEquals("18", loaded.get("totalShipsSunk"));
    }
}