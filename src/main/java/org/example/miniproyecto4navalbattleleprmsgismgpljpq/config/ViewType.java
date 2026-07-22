package org.example.miniproyecto4navalbattleleprmsgismgpljpq.config;

/**
 * Identifies each screen of the application. Used by SceneManager to
 * know which FXML/CSS pair to load, avoiding magic strings scattered
 * across the codebase (rubric point 14).
 */
public enum ViewType {
    TITLE("/com/university/battleship/fxml/title-view.fxml", "Battleship"),
    PREPARATION("/com/university/battleship/fxml/preparation-view.fxml", "Battleship - Prepara tu flota"),
    GAME("/com/university/battleship/fxml/game-view.fxml", "Battleship - Combate"),
    RESULTS("/com/university/battleship/fxml/results-view.fxml", "Battleship - Resultados");

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
