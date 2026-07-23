package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameState;

/**
 * Observer pattern: notified whenever GameService's phase changes.
 * Decouples "what happens on a phase change" (e.g. triggering the AI's
 * turn, refreshing UI labels) from GameService itself, which should
 * only know THAT its state changed, not WHO cares or WHY.
 */
public interface GameStateListener {
    void onStateChanged(GameState newState);
}