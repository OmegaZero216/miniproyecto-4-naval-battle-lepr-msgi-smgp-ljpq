package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.persistence;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Player;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.CorruptedSaveDataException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.PersistenceException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Flat-text implementation of {@link PlayerStatsRepository} for managing player
 * statistics using key-value pairs (HU-5).
 * <p>
 * Stores nickname and game metrics in a human-readable plain text file.
 */
public class FilePlayerStatsRepository implements PlayerStatsRepository {

    private static final String NICKNAME_KEY = "nickname";
    private static final String SUNK_SHIPS_KEY = "sunkShips";
    private static final String SHOTS_FIRED_KEY = "shotsFired";

    private final Path statsPath;

    /**
     * Default constructor using the path configured in {@link GameConfig}.
     */
    public FilePlayerStatsRepository() {
        this(Path.of(GameConfig.STATS_FILE_PATH));
    }

    /**
     * Constructor injection allowing custom file paths for testing.
     *
     * @param statsPath The destination {@link Path} for saving player statistics.
     */
    public FilePlayerStatsRepository(Path statsPath) {
        this.statsPath = statsPath;
    }

    /**
     * Writes player statistics to a plain text file.
     *
     * @param player The {@link Player} instance containing statistics to persist.
     * @throws PersistenceException If an error occurs during writing.
     */
    @Override
    public void save(Player player) throws PersistenceException {
        try {
            Path parent = statsPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(statsPath)) {
                writer.write(NICKNAME_KEY + "=" + player.getNickname());
                writer.newLine();
                writer.write(SUNK_SHIPS_KEY + "=" + player.getSunkShipsCount());
                writer.newLine();
                writer.write(SHOTS_FIRED_KEY + "=" + player.getShotsFired());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PersistenceException("No se pudieron guardar las estadísticas del jugador en: " + statsPath, e);
        }
    }

    /**
     * Reads and parses player statistics from the plain text file.
     *
     * @return A restored {@link Player} instance with populated stats.
     * @throws PersistenceException If reading fails or the file format is invalid.
     */
    @Override
    public Player load() throws PersistenceException {
        if (!statsExist()) {
            throw new PersistenceException("No se encontró el archivo de estadísticas en: " + statsPath);
        }

        String nickname = null;
        int sunkShips = 0;
        int shotsFired = 0;

        try (BufferedReader reader = Files.newBufferedReader(statsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length != 2) {
                    continue;
                }
                switch (parts[0]) {
                    case NICKNAME_KEY -> nickname = parts[1];
                    case SUNK_SHIPS_KEY -> sunkShips = parseIntOrThrow(parts[1]);
                    case SHOTS_FIRED_KEY -> shotsFired = parseIntOrThrow(parts[1]);
                    default -> {}
                }
            }
        } catch (IOException e) {
            throw new PersistenceException("Error de lectura al acceder al archivo de estadísticas: " + statsPath, e);
        }

        if (nickname == null) {
            throw new CorruptedSaveDataException(
                    "El archivo de estadísticas " + statsPath + " no contiene el campo obligatorio '" + NICKNAME_KEY + "'.", null);
        }

        Player player = new Player(nickname);
        for (int i = 0; i < sunkShips; i++) {
            player.incrementSunkShips();
        }
        for (int i = 0; i < shotsFired; i++) {
            player.incrementShotsFired();
        }
        return player;
    }

    private int parseIntOrThrow(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new CorruptedSaveDataException(
                    "El archivo " + statsPath + " contiene un valor numérico inválido: '" + value + "'", e);
        }
    }

    /**
     * Checks whether the statistics file exists on disk.
     *
     * @return True if the file exists and is readable, false otherwise.
     */
    @Override
    public boolean statsExist() {
        return Files.exists(statsPath) && Files.isRegularFile(statsPath);
    }
}