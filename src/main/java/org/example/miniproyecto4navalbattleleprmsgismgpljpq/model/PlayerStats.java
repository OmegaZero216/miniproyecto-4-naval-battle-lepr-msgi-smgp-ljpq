package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

/**
 * Mutable accumulator of a nickname's stats across multiple games.
 * Unlike Coordinate/GameResult (immutable snapshots of a single fact),
 * this class genuinely has behavior over time — registering a new game
 * result — so a plain class fits better here than a record.
 */
public class PlayerStats {

    private final String nickname;
    private int gamesWon;
    private int gamesLost;
    private int totalShotsFired;
    private int totalShipsSunkByPlayer;

    public PlayerStats(String nickname) {
        this.nickname = nickname;
    }

    public PlayerStats(String nickname, int gamesWon, int gamesLost,
                       int totalShotsFired, int totalShipsSunkByPlayer) {
        this.nickname = nickname;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.totalShotsFired = totalShotsFired;
        this.totalShipsSunkByPlayer = totalShipsSunkByPlayer;
    }

    /** Folds one finished game's result into the running totals. */
    public void registerGame(GameResult result) {
        if (result.playerWon()) {
            gamesWon++;
        } else {
            gamesLost++;
        }
        totalShotsFired += result.shotsFired();
        totalShipsSunkByPlayer += result.shipsSunkByPlayer();
    }

    public String getNickname() {
        return nickname;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getTotalShotsFired() {
        return totalShotsFired;
    }

    public int getTotalShipsSunkByPlayer() {
        return totalShipsSunkByPlayer;
    }
}
