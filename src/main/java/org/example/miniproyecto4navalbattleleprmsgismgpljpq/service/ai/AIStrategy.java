package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.ai;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;

/**
 * Strategy pattern: defines how the machine chooses its next shot.
 * <p>
 * Decoupling "how to choose a coordinate" from AIService itself means
 * we can switch between random shooting and intelligent hunting (or add
 * a future difficulty level) without touching AIService's orchestration
 * logic — Open/Closed Principle.
 */
public interface AIStrategy {
    Coordinate selectNextShot(Board enemyBoard);
}
