package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.persistence;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.PersistenceException;

/**
 * Contract for persisting and retrieving binary game snapshots.
 * <p>
 * Abstraction that enables flexible binary storage implementations
 * following the Dependency Inversion Principle.
 */
public interface BinarySaveRepository {

    /**
     * Serializes and writes the given snapshot, overwriting any previous save.
     *
     * @param snapshot The {@link GameSnapshot} instance to persist.
     * @throws PersistenceException If an error occurs during the save operation.
     */
    void save(GameSnapshot snapshot) throws PersistenceException;

    /**
     * Reads and deserializes the most recent game snapshot.
     *
     * @return The restored {@link GameSnapshot}.
     * @throws PersistenceException If the save file cannot be read or deserialized.
     */
    GameSnapshot load() throws PersistenceException;

    /**
     * Checks if a valid binary save file exists on storage.
     *
     * @return True if a save file exists and is accessible, false otherwise.
     */
    boolean saveExists();
}