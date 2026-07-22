package org.example.miniproyecto4navalbattleleprmsgismgpljpq.model;

import java.io.Serializable;

/**
 * Represents the human player's identity and running statistics.
 * <p>
 * This is intentionally separate from {@link Board}: a Board describes
 * the state of a grid, while a Player describes who is playing and how
 * they are doing. Game requires persisting the nickname and sunk-ship
 * count in a flat file independently of the (binary) board state, so
 * keeping this as its own small class — rather than adding fields to
 * Board or GameService — keeps each class's reason to change separate.
 * <p>
 * Implements {@link Serializable} defensively (a Player could in the
 * future travel inside a {@code GameSnapshot}), even though the current
 * persistence design writes it to a plain text file via
 * {@code FilePlayerStatsRepository}.
 */

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickname;
    private int sunkShipsCount;
    private int shotsFired;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getSunkShipsCount() { return sunkShipsCount; }


    /** Called by the Service layer whenever the player sinks an enemy ship. */
    public void incrementSunkShips() { this.sunkShipsCount++; }

    public int getShotsFired() { return shotsFired; }

    /** Called by the Service layer after every shot the player fires. */
    public void incrementShotsFired() { this.shotsFired++; }
}