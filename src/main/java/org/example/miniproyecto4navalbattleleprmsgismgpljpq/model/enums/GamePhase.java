package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums;

/**
 * Represents the overall phase of the game, used by the State pattern
 * to control what actions are valid at any given moment
 * (e.g. you cannot shoot while still placing ships).
 */
public enum GamePhase {
    PLACING_SHIPS,
    PLAYER_TURN,
    MACHINE_TURN,
    GAME_OVER
}