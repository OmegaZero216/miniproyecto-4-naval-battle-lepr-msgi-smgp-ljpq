package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.state;

/**
 * Factory pattern (reused from Stages 4/7): reconstructs a GameState
 * instance from its persisted phase name. Needed because GameState
 * implementations are stateless singletons-in-spirit but must be
 * re-instantiated after deserialization by name, not by object
 * reference (LoadManager reads a String, not a live object graph
 * for this particular field).
 */
public final class GameStateFactory {

    private GameStateFactory() {
    }

    public static GameState fromPhaseName(String phaseName) {
        return switch (phaseName) {
            case "PLACING_SHIPS" -> new PlacingShipsState();
            case "PLAYER_TURN" -> new PlayerTurnState();
            case "MACHINE_TURN" -> new MachineTurnState();
            case "GAME_OVER" -> new GameOverState();
            default -> throw new IllegalArgumentException("Unknown phase: " + phaseName);
        };
    }
}
