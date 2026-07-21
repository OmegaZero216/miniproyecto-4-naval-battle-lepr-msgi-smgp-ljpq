package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;

/** Terminal state: no further shots are accepted once the game ends. */
public class GameOverState implements GameState {

    @Override
    public void handleShot(GameService context, Coordinate coordinate) {
        throw new IllegalStateException("The game has already ended.");
    }

    @Override
    public String getPhaseName() {
        return "GAME_OVER";
    }
}
