package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;

/**
 * Behavior while it is the machine's turn: the player cannot fire.
 * The actual machine shot is driven asynchronously by AIService
 * so handleShot here simply rejects player input.
 */
public class MachineTurnState implements GameState {

    @Override
    public void handleShot(GameService context, Coordinate coordinate) {
        throw new IllegalStateException("Cannot fire during the machine's turn.");
    }

    @Override
    public String getPhaseName() {
        return "MACHINE_TURN";
    }
}
