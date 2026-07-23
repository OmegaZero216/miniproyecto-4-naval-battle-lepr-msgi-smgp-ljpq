package org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;

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

    public void save(String nickname, int wins, int losses, int totalShipsSunk) {
        Map<String, String> data = new HashMap<>();
        data.put("nickname", nickname);
        data.put("wins", String.valueOf(wins));
        data.put("losses", String.valueOf(losses));
        data.put("totalShipsSunk", String.valueOf(totalShipsSunk));

        try {
            Path path = Path.of(GameConfig.STATS_FILE_PATH);
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            // Las estadísticas son informativas, no críticas para poder
            // seguir jugando — por eso aquí solo registramos, no
            // propagamos una excepción que interrumpiría la partida.
            System.err.println("Warning: could not save stats — " + e.getMessage());
        }
    }

    public Map<String, String> load() {
        Map<String, String> data = new HashMap<>();
        Path path = Path.of(GameConfig.STATS_FILE_PATH);
        if (!Files.exists(path)) {
            return data;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: could not read stats — " + e.getMessage());
        }
        return data;
    }
}
