package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;

/**
 * Behavior while it is the human player's turn: shots are allowed and
 * routed to the machine's board.
 */
public class PlayerTurnState implements GameState {

    @Override
    public void handleShot(GameService context, Coordinate coordinate) {
        context.resolvePlayerShot(coordinate);
    }

    @Override
    public String getPhaseName() {
        return "PLAYER_TURN";
    }
}
