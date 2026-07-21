package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;

/**
 * Behavior while ships are still being placed. Shooting is not a valid
 * action yet — attempting it is a programming error (the Controller
 * must disable shot input during this phase), hence the unchecked
 * exception rather than a checked one.
 */
public class PlacingShipsState implements GameState {

    @Override
    public void handleShot(GameService context, Coordinate coordinate) {
        throw new IllegalStateException("Cannot fire while ships are still being placed.");
    }

    @Override
    public String getPhaseName() {
        return "PLACING_SHIPS";
    }
}