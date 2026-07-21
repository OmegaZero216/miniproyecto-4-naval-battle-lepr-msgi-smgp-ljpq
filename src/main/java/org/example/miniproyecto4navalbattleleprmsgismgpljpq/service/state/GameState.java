package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;

/**
 * State pattern: defines what actions are valid during a given game phase.
 * <p>
 * Instead of scattering "if (phase == X)" checks across GameService, each
 * phase gets its own class implementing this interface. This satisfies
 * OCP (Open/Closed Principle): adding a new phase means adding a new
 * class, never modifying existing state classes or GameService's core
 * logic.
 */
public interface GameState {

    /**
     * Handles a shot attempt during this phase.
     *
     * @throws UnsupportedOperationException if shooting is not a valid
     *         action during this phase (e.g. during PLACING_SHIPS).
     */
    void handleShot(GameService context, Coordinate coordinate);

    /** @return a human-readable phase name, useful for debug/logging. */
    String getPhaseName();
}
