package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

/**
 * Immutable snapshot of a finished game's outcome, passed from
 * GameController to ResultsController via SceneManager. A record fits
 * here for the same reason as Coordinate: pure data, no behavior,
 * value-based equality not even needed but immutability is.
 */
public record GameResult(boolean playerWon, int shotsFired, int shipsSunkByPlayer, int shipsSunkByMachine) {
}