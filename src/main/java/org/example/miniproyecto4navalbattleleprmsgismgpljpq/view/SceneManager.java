package org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.ViewType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller.GameController;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller.PreparationController;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller.ResultsController;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller.TitleController;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.GameResult;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.PlayerStats;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.repository.StatsRepository;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.StatsService;

import java.io.IOException;

/**
 * Owns the single Stage and swaps its Scene content between screens.
 * <p>
 * This is the ONLY class in the project allowed to call FXMLLoader for
 * navigation purposes. Every Controller that needs to move to another
 * screen receives a SceneManager reference via setter injection (manual
 * DI, consistent with the rest of the project — no Singleton), instead
 * of instantiating its own FXMLLoader. This keeps navigation logic out
 * of Controllers entirely, the same architectural boundary we've kept
 * since Stage 1 for game logic.
 * <p>
 * Data that must travel between screens (a built GameService, a final
 * GameResult) is passed explicitly through typed navigateTo* methods —
 * never through static/global state, which would reintroduce the
 * Singleton problem we're avoiding.
 */
public class SceneManager {

    private final Stage stage;
    private final StatsService statsService = new StatsService(new StatsRepository());


    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void navigateToTitle() {
        TitleController controller = (TitleController) loadAndShow(ViewType.TITLE);
        controller.setSceneManager(this);
    }

    public void navigateToPreparation(String nickname) {
        PreparationController controller = (PreparationController) loadAndShow(ViewType.PREPARATION);
        controller.setSceneManager(this);
        controller.setNickname(nickname);
    }

    public void navigateToGame(GameService gameService, String nickname) {
        GameController controller = (GameController) loadAndShow(ViewType.GAME);
        controller.setSceneManager(this);
        controller.setNickname(nickname);
        controller.setGameService(gameService);
    }

    public void navigateToResults(GameResult result, String nickname) {
        PlayerStats updatedStats = statsService.recordGameResult(nickname, result);
        ResultsController controller = (ResultsController) loadAndShow(ViewType.RESULTS);
        controller.setSceneManager(this);
        controller.setResult(result, updatedStats);
    }

    public StatsService getStatsService() {
        return statsService;
    }

    /**
     * Loads the given view's FXML, installs it as the Stage's Scene,
     * and returns its Controller so the caller can inject dependencies
     * specific to that screen.
     */
    private Object loadAndShow(ViewType viewType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewType.getFxmlPath()));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(viewType.getWindowTitle());
            stage.show();
            return loader.getController();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load view: " + viewType, e);
        }
    }
}
