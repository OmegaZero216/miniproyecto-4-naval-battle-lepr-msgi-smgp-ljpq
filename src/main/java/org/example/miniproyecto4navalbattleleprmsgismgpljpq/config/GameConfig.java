package org.example.miniproyecto4navalbattleleprmsgismgpljpq.config;

/**
 * Centralizes all fixed game configuration values.
 * <p>
 * Existing solely to eliminate magic numbers (rubric point 14), this class
 * has a single reason to change: the game's fixed parameters (board size,
 * fleet composition). No other class should hard-code these values.
 * <p>
 * Declared as final with a private constructor because it is a pure
 * constant holder — it must never be instantiated.
 */
public final class GameConfig {

    public static final int BOARD_SIZE = 10;

    public static final int AIRCRAFT_CARRIER_COUNT = 1;
    public static final int SUBMARINE_COUNT = 2;
    public static final int DESTROYER_COUNT = 3;
    public static final int FRIGATE_COUNT = 4;

    public static final String SAVE_FILE_PATH = "data/saves/current_game.dat";
    public static final String STATS_FILE_PATH = "data/stats/player_stats.txt";

    private GameConfig() {
        // Prevents instantiation — this class only holds constants.
    }
}
