package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameResult;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.PlayerStats;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository.StatsRepository;

/**
 * Orchestrates stats persistence: loads a nickname's history, folds a
 * new GameResult into it, and saves. Kept separate from StatsRepository
 * (which only knows file mechanics) so "what a result means for the
 * totals" (PlayerStats.registerGame) stays independent of "how it's
 * written to disk" — same separation of concerns as SaveManager/
 * LoadManager vs GameSaveData.
 */
public class StatsService {

    private final StatsRepository repository;

    public StatsService(StatsRepository repository) {
        this.repository = repository;
    }

    public PlayerStats loadStats(String nickname) {
        return repository.load(nickname);
    }

    /** Records a finished game's result for the given nickname and persists it. */
    public PlayerStats recordGameResult(String nickname, GameResult result) {
        PlayerStats stats = repository.load(nickname);
        stats.registerGame(result);
        repository.save(stats);
        return stats;
    }
}
