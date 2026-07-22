package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception;

/**
 * Thrown when saving or loading game data fails for a recoverable, I/O
 * related reason (disk full, missing permissions, save file not created
 * yet, etc.).
 * <p>
 * Declared as a checked exception, mirroring {@code InvalidShipPlacementException}: a failed
 * save/load is an expected
 * situation the caller (Controller) must explicitly handle — for
 * example, by starting a new game if no save exists, or warning the
 * user that progress could not be saved — rather than crashing.
 */

public class PersistenceException extends Exception {
    public PersistenceException(String message) { super(message); }
    public PersistenceException(String message, Throwable cause) { super(message, cause); }
}