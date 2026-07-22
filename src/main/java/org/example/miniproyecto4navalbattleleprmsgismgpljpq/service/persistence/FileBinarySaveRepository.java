package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.persistence;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.CorruptedSaveDataException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.PersistenceException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Binary implementation of {@link BinarySaveRepository} for saving and loading
 * the serialized {@link GameSnapshot} object graph to disk.
 * <p>
 * Uses file paths defined in {@link GameConfig} to avoid magic strings.
 */
public class FileBinarySaveRepository implements BinarySaveRepository {

    private final Path savePath;

    /**
     * Default constructor using path configured in {@link GameConfig}.
     */
    public FileBinarySaveRepository() {
        this(Path.of(GameConfig.SAVE_FILE_PATH));
    }

    /**
     * Constructor injection allowing custom file paths for testing.
     *
     * @param savePath The destination {@link Path} for saving binary data.
     */
    public FileBinarySaveRepository(Path savePath) {
        this.savePath = savePath;
    }

    /**
     * Serializes and writes the game snapshot to a binary file.
     *
     * @param snapshot The {@link GameSnapshot} to save.
     * @throws PersistenceException If an error occurs during writing.
     */
    @Override
    public void save(GameSnapshot snapshot) throws PersistenceException {
        try {
            Path parent = savePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (ObjectOutputStream out =
                         new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(savePath)))) {
                out.writeObject(snapshot);
            }
        } catch (IOException e) {
            throw new PersistenceException("No se pudo guardar la partida en: " + savePath, e);
        }
    }

    /**
     * Reads and deserializes the game snapshot from the binary save file.
     *
     * @return The restored {@link GameSnapshot}.
     * @throws PersistenceException If reading or deserializing fails.
     */
    @Override
    public GameSnapshot load() throws PersistenceException {
        if (!saveExists()) {
            throw new PersistenceException("No se encontró ningún archivo de guardado en: " + savePath);
        }
        try (ObjectInputStream in =
                     new ObjectInputStream(new BufferedInputStream(Files.newInputStream(savePath)))) {
            Object data = in.readObject();
            if (!(data instanceof GameSnapshot snapshot)) {
                throw new CorruptedSaveDataException(
                        "El archivo " + savePath + " no contiene un estado de juego válido.", null);
            }
            return snapshot;
        } catch (IOException e) {
            throw new PersistenceException("Error de lectura al intentar cargar la partida desde: " + savePath, e);
        } catch (ClassNotFoundException e) {
            throw new CorruptedSaveDataException(
                    "El archivo " + savePath + " fue creado con una versión incompatible del programa.", e);
        }
    }

    /**
     * Checks whether a valid binary save file exists on disk.
     *
     * @return True if the file exists and is readable, false otherwise.
     */
    @Override
    public boolean saveExists() {
        return Files.exists(savePath) && Files.isRegularFile(savePath);
    }
}