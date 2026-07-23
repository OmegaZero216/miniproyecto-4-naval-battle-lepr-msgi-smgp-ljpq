package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception;

/**
 * Checked exception wrapping any I/O failure during save/load. It's
 * checked on purpose: a persistence failure (disk full, permissions)
 * is an expected, recoverable situation the Controller must handle —
 * e.g. show "could not save" and let the player keep playing —
 * rather than crash the application.
 */
public class GamePersistenceException extends Exception {
    public GamePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}