package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.ai;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;

import java.util.Random;

/**
 * Fires at a random, not-yet-shot coordinate. Used as the default
 * strategy when the AI has no active target (no pending hit to follow up).
 */
public class RandomStrategy implements AIStrategy {

    private final Random random = new Random();

    @Override
    public Coordinate selectNextShot(Board enemyBoard) {
        Coordinate coordinate;
        do {
            int row = random.nextInt(GameConfig.BOARD_SIZE);
            int col = random.nextInt(GameConfig.BOARD_SIZE);
            coordinate = new Coordinate(row, col);
        } while (enemyBoard.hasShotAt(coordinate));
        return coordinate;
    }
}
