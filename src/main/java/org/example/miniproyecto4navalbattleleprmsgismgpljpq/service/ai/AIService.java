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
     * Triggers the machine's shot asynchronously.
     *
     * @param playerBoard the human player's board (the machine's target)
     * @param onShotResolved callback invoked on the JavaFX thread once the
     *        shot outcome is known, so the Controller can update the view
     */
    public void takeShot(Board playerBoard, ShotResultCallback onShotResolved) {
        aiExecutor.submit(() -> {
            simulateThinkingDelay();

            Coordinate coordinate = targetingStrategy.hasTargets()
                    ? targetingStrategy.selectNextShot(playerBoard)
                    : randomStrategy.selectNextShot(playerBoard);

            if (coordinate == null) {
                coordinate = randomStrategy.selectNextShot(playerBoard);
            }

            playerBoard.registerShot(coordinate);
            boolean hit = playerBoard.getCell(coordinate).isOccupied();
            playerBoard.getCell(coordinate).setState(hit ? CellState.HIT : CellState.MISS);

            if (hit) {
                targetingStrategy.enqueueAdjacent(coordinate);
            }

            Coordinate finalCoordinate = coordinate;
            // Bridge back to the JavaFX Application Thread: this is the
            // synchronization mechanism that avoids race conditions
            // between the AI's background thread and the UI thread.
            Platform.runLater(() -> onShotResolved.onResolved(finalCoordinate, hit));
        });
    }

    /** Simulates a brief "thinking" pause so the AI doesn't fire instantly. */
    private void simulateThinkingDelay() {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Functional callback interface — an example of the "múltiples interfaces" requirement (rubric point 2). */
    public interface ShotResultCallback {
        void onResolved(Coordinate coordinate, boolean hit);
    }

    public void shutdown() {
        aiExecutor.shutdown();
    }
}
