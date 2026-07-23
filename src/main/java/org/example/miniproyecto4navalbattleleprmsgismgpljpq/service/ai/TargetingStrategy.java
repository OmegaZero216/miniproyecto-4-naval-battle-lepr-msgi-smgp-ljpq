package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.ai;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Intelligent hunting strategy: after a hit, queues the four adjacent
 * cells and tries them before falling back to random shots. This is the
 * Deque<Coordinate> structure we committed to in Stage 1.
 * <p>
 * A Deque (not a plain Queue) was chosen because it lets us push newly
 * discovered candidates to the front (LIFO-like priority for the most
 * recent hit) while still being able to drain it in order — flexibility
 * a simple Queue does not offer.
 */
public class TargetingStrategy implements AIStrategy {

    private final Deque<Coordinate> targetQueue = new ArrayDeque<>();

    /**
     * Called by AIService after a confirmed hit, to feed this strategy
     * new candidates around the hit coordinate.
     */
    public void enqueueAdjacent(Coordinate hit) {
        int size = GameConfig.BOARD_SIZE;
        int[][] deltas = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] delta : deltas) {
            int row = hit.row() + delta[0];
            int col = hit.column() + delta[1];
            if (row >= 0 && row < size && col >= 0 && col < size) {
                targetQueue.addLast(new Coordinate(row, col));
            }
        }
    }

    public boolean hasTargets() {
        return !targetQueue.isEmpty();
    }

    @Override
    public Coordinate selectNextShot(Board enemyBoard) {
        Coordinate next;
        do {
            if (targetQueue.isEmpty()) {
                return null; // Signals AIService to fall back to RandomStrategy.
            }
            next = targetQueue.pollFirst();
        } while (enemyBoard.hasShotAt(next));
        return next;
    }
}
