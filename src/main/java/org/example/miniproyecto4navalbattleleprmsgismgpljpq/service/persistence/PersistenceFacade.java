package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.persistence;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Player;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.PersistenceException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;

/**
 * Facade providing a unified interface for game state and player statistics persistence.
 * <p>
 * Coordinates operations between {@link BinarySaveRepository} and {@link PlayerStatsRepository}
 * to shield controllers from persistence details.
 */
public class PersistenceFacade {

    private final BinarySaveRepository binarySaveRepository;
    private final PlayerStatsRepository playerStatsRepository;

    /**
     * Default constructor initializing default file-based repository implementations.
     */
    public PersistenceFacade() {
        this(new FileBinarySaveRepository(), new FilePlayerStatsRepository());
    }

    /**
     * Constructor injection allowing custom repository implementations or test doubles.
     *
     * @param binarySaveRepository  Repository for handling binary game snapshots.
     * @param playerStatsRepository Repository for handling flat-text player statistics.
     */
    public PersistenceFacade(BinarySaveRepository binarySaveRepository, PlayerStatsRepository playerStatsRepository) {
        this.binarySaveRepository = binarySaveRepository;
        this.playerStatsRepository = playerStatsRepository;
    }

    /**
     * Automatically saves both the current game state snapshot and player statistics.
     *
     * @param gameService The active {@link GameService} holding current boards and state.
     * @param player      The {@link Player} instance containing updated statistics.
     * @throws PersistenceException If either save operation fails.
     */
    public void autoSave(GameService gameService, Player player) throws PersistenceException {
        GameSnapshot snapshot = new GameSnapshot(
                gameService.getPlayerBoard(),
                gameService.getMachineBoard(),
                gameService.getCurrentState());
        binarySaveRepository.save(snapshot);
        playerStatsRepository.save(player);
    }

    /**
     * Checks if both a binary save file and player statistics exist on storage.
     *
     * @return True if both save sources are available, false otherwise.
     */
    public boolean hasSavedGame() {
        return binarySaveRepository.saveExists() && playerStatsRepository.statsExist();
    }

    /**
     * Loads the persisted binary game snapshot.
     *
     * @return The saved {@link GameSnapshot}.
     * @throws PersistenceException If loading or deserialization fails.
     */
    public GameSnapshot loadGame() throws PersistenceException {
        return binarySaveRepository.load();
    }

    /**
     * Loads the persisted player statistics.
     *
     * @return The restored {@link Player} instance.
     * @throws PersistenceException If reading or parsing statistics fails.
     */
    public Player loadPlayer() throws PersistenceException {
        return playerStatsRepository.load();
    }
}