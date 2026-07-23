package org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameSaveData;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.GamePersistenceException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes a GameSaveData snapshot to disk using Java serialization
 * (Serializable), as required by rubric point 13. Its single
 * responsibility is persistence mechanics — it knows nothing about
 * GameService, GameState transitions, or game rules (SRP).
 */
public class SaveManager {

    public void save(GameSaveData data) throws GamePersistenceException {
        try {
            Path path = Path.of(GameConfig.SAVE_FILE_PATH);
            Files.createDirectories(path.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(path.toFile()))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            throw new GamePersistenceException("Could not save the game.", e);
        }
    }

    public boolean saveExists() {
        return Files.exists(Path.of(GameConfig.SAVE_FILE_PATH));
    }
}
