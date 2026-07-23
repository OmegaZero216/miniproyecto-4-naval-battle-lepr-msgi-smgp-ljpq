package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state.GameState;

import java.io.Serializable;

/**
 * Immutable snapshot of everything needed to resume a game exactly
 * where it was left off. A record fits perfectly here: pure data,
 * no behavior — the same rationale as Coordinate/GameResult.
 * <p>
 * Deliberately does NOT include AIService's internal TargetingStrategy
 * queue (a known, documented simplification): resuming a save restarts
 * the AI's hunting memory. This is an honest trade-off, not a bug —
 * flagged here rather than silently lost.
 */
public record GameSaveData(Board playerBoard, Board machineBoard, String currentPhaseName, Boolean debugRevealUsed)
        implements Serializable {
}
