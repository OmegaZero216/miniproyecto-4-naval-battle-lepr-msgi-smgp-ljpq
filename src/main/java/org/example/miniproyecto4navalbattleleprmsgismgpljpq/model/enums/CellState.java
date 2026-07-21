package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums;

/**
 * Represents the visual and logical state of a single board cell.
 * Used by the State pattern to determine how a cell should render
 * and behave when clicked.
 */
public enum CellState {
    EMPTY,      // Agua sin disparar
    SHIP,       // Contiene parte de un barco, aún no disparado
    HIT,        // Tocado
    SUNK,       // Hundido (barco completo destruido)
    MISS        // Disparo al agua
}