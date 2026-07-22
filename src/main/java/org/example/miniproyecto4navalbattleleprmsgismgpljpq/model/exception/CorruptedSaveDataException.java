package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception;

/**
 * Thrown when a save file exists and is readable, but its contents do
 * not correspond to a valid game state (wrong object type, unreadable
 * class version, unexpected data format).
 * <p>
 * Unlike {@link PersistenceException}, this is a programming/data
 * integrity error rather than a normal, expected outcome — there is no
 * sensible way for the caller to "try again" with the same file, so it
 * is modeled as an unchecked {@link RuntimeException}. It typically
 * means the save file was manually edited/corrupted or comes from an
 * incompatible version of the game.
 */

public class CorruptedSaveDataException extends RuntimeException {
    public CorruptedSaveDataException(String message, Throwable cause) {
        super(message, cause);
    }
}