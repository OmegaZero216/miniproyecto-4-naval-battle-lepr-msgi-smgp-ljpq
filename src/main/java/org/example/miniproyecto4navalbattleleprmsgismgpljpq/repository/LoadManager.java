package org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameSaveData;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.GamePersistenceException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.SaveNotFoundException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads back a GameSaveData snapshot written by SaveManager. Kept as a
 * separate class from SaveManager (rather than one "PersistenceManager"
 * doing both) so each has a single, focused reason to change — reading
 * and writing evolve independently in most real persistence layers
 * (e.g. a future save-file version migration would only touch here).
 */
public class LoadManager {

    public GameSaveData load() throws GamePersistenceException {
        Path path = Path.of(GameConfig.SAVE_FILE_PATH);
        if (!Files.exists(path)) {
            throw new SaveNotFoundException("No saved game found at " + path);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (GameSaveData) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new GamePersistenceException("Could not load the saved game.", e);
        }
    }
}
