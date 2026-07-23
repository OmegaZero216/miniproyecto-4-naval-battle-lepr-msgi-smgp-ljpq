package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception;

/**
 * Thrown when a ship placement violates game rules
 * (out of bounds, overlapping another ship, or adjacent to one).
 * <p>
 * This is a checked exception on purpose: placement failures are an
 * expected, recoverable situation the caller (PlacementService/Controller)
 * must explicitly handle — e.g. to show the user a message and let them
 * try a different cell, rather than crashing the application.
 */
public class InvalidShipPlacementException extends Exception {

    public InvalidShipPlacementException(String message) {
        super(message);
    }
}