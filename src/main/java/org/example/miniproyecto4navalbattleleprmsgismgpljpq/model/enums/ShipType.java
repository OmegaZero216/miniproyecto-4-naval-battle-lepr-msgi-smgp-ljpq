package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums;

/**
 * Defines the four ship types required by the assignment, each with
 * its fixed size. Centralizing sizes here avoids magic numbers
 * scattered across the codebase (rubric requirement: no magic numbers).
 */
public enum ShipType {
    AIRCRAFT_CARRIER(4),
    SUBMARINE(3),
    DESTROYER(2),
    FRIGATE(1);

    private final int size;

    ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}