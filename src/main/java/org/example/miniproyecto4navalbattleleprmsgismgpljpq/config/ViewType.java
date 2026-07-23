package org.example.miniproyecto4navalbattleleprmsgismgpljpq.config;

/**
 * Identifies each screen of the application. Used by SceneManager to
 * know which FXML/CSS pair to load, avoiding magic strings scattered
 * across the codebase (rubric point 14).
 */
public enum ViewType {
    TITLE("/org/example/miniproyecto4navalbattleleprmsgismgpljpq/fxml/title-view.fxml", "Battleship"),
    PREPARATION("/org/example/miniproyecto4navalbattleleprmsgismgpljpq/fxml/preparation-view.fxml", "Battleship - Prepara tu flota"),
    GAME("/org/example/miniproyecto4navalbattleleprmsgismgpljpq/fxml/game-view.fxml", "Battleship - Combate"),
    RESULTS("/org/example/miniproyecto4navalbattleleprmsgismgpljpq/fxml/results-view.fxml", "Battleship - Resultados");

    private final String fxmlPath;
    private final String windowTitle;

    ViewType(String fxmlPath, String windowTitle) {
        this.fxmlPath = fxmlPath;
        this.windowTitle = windowTitle;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public String getWindowTitle() {
        return windowTitle;
    }
}
