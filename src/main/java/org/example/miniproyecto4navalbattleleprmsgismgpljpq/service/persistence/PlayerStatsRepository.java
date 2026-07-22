package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.persistence;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Player;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.PersistenceException;

/**
 * Defines how {@link Player} data (nickname, sunk ships) is persisted
 * as a flat file, per HU-5's explicit requirement that this
 * information — unlike the board — is stored in plain text rather
 * than serialized binary.
 */
public interface PlayerStatsRepository {

    void save(Player player) throws PersistenceException;

    Player load() throws PersistenceException;

    boolean statsExist();
}
