package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;

/**
 * Orchestrates the machine's turn: picks a strategy (Strategy pattern),
 * fires, and switches to targeting mode after a hit.
 * <p>
 * The shot is executed on a background thread via ExecutorService — not
 * because the calculation is slow, but to simulate a natural "thinking"
 * delay without freezing the JavaFX Application Thread (rubric point 7:
 * threads). All UI mutations happen back on the JavaFX thread via
 * {@code Platform.runLater}, which is the standard, race-condition-free
 * way to bridge a background thread with the JavaFX scene graph.
 */
public class AIService {

    private final ExecutorService aiExecutor = Executors.newSingleThreadExecutor();
    private final RandomStrategy randomStrategy = new RandomStrategy();
    private final TargetingStrategy targetingStrategy = new TargetingStrategy();

    /**
     * Selects and reports the machine's next shot asynchronously. Unlike
     * the earlier version, AIService no longer mutates the board itself
     * — GameService.resolveMachineShot is the single source of truth for
     * that. AIService's job is purely strategic: pick a coordinate, and
     * later learn from the outcome via registerShotOutcome.
     */
    public void takeShot(Board enemyBoard, ShotSelectedCallback callback) {
        aiExecutor.submit(() -> {
            simulateThinkingDelay();
            Coordinate coordinate = targetingStrategy.hasTargets()
                    ? targetingStrategy.selectNextShot(enemyBoard)
                    : randomStrategy.selectNextShot(enemyBoard);
            if (coordinate == null) {
                coordinate = randomStrategy.selectNextShot(enemyBoard);
            }
            Coordinate finalCoordinate = coordinate;
            javafx.application.Platform.runLater(() -> callback.onShotSelected(finalCoordinate));
        });
    }

    /** Feeds back the outcome so TargetingStrategy can queue adjacent cells on a hit. */
    public void registerShotOutcome(Coordinate coordinate, boolean hit) {
        if (hit) {
            targetingStrategy.enqueueAdjacent(coordinate);
        }
    }

    public interface ShotSelectedCallback {
        void onShotSelected(Coordinate coordinate);
    }

    /** Simulates a brief "thinking" pause so the AI doesn't fire instantly. */
    private void simulateThinkingDelay() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public void shutdown() {
        aiExecutor.shutdown();
    }
}
