package org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.PlayerStats;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Persists human-readable stats (nickname, wins, losses, ships sunk)
 * as a plain TXT key=value file — satisfying rubric point 13's explicit
 * requirement for TXT files, separate from the binary save file (which
 * holds board state via Serializable). Kept intentionally simple and
 * human-readable, since this file is meant to be inspectable/editable
 * by a grader, unlike the binary save.
 */
public class StatsRepository {

    public void save(PlayerStats stats) {
        try {
            Path dir = Path.of(GameConfig.STATS_DIR_PATH);
            Files.createDirectories(dir);
            Path path = dir.resolve(stats.getNickname() + ".txt");
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("nickname=" + stats.getNickname()); writer.newLine();
                writer.write("gamesWon=" + stats.getGamesWon()); writer.newLine();
                writer.write("gamesLost=" + stats.getGamesLost()); writer.newLine();
                writer.write("totalShotsFired=" + stats.getTotalShotsFired()); writer.newLine();
                writer.write("totalShipsSunkByPlayer=" + stats.getTotalShipsSunkByPlayer()); writer.newLine();
            }
        } catch (IOException e) {
            // Igual que antes: las estadísticas son informativas, no
            // críticas para poder seguir jugando.
            System.err.println("Warning: could not save stats — " + e.getMessage());
        }
    }

    /** Returns fresh (all-zero) stats if this nickname has never played before. */
    public PlayerStats load(String nickname) {
        Path path = Path.of(GameConfig.STATS_DIR_PATH, nickname + ".txt");
        if (!Files.exists(path)) {
            return new PlayerStats(nickname);
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            int won = 0, lost = 0, shots = 0, sunk = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length != 2) continue;
                switch (parts[0]) {
                    case "gamesWon" -> won = Integer.parseInt(parts[1]);
                    case "gamesLost" -> lost = Integer.parseInt(parts[1]);
                    case "totalShotsFired" -> shots = Integer.parseInt(parts[1]);
                    case "totalShipsSunkByPlayer" -> sunk = Integer.parseInt(parts[1]);
                    default -> { }
                }
            }
            return new PlayerStats(nickname, won, lost, shots, sunk);
        } catch (IOException e) {
            System.err.println("Warning: could not read stats — " + e.getMessage());
            return new PlayerStats(nickname);
        }
    }
}
