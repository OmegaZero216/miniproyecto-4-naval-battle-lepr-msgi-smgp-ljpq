package org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Factory pattern: creates the visual representation (a JavaFX Node) for
 * a given CellState, using only Shapes — never images, per rubric point 12.
 * <p>
 * Centralizing this here means the Controller never decides "which shape
 * looks like a hit" — it just asks the factory for the current state's
 * representation. Adding a new visual state later means editing only
 * this class (OCP at the rendering layer).
 */
public final class CellShapeFactory {

    public static final int CELL_SIZE = 32;

    private CellShapeFactory() {
    }

    public static Group createShapeFor(CellState state) {
        Group group = new Group();
        Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
        background.setStroke(Color.web("#2c3e50"));
        background.setStrokeWidth(1);

        switch (state) {
            case EMPTY -> background.setFill(Color.web("#aed9e0")); // agua
            case SHIP -> {
                background.setFill(Color.web("#7f8c8d"));
                Polygon hull = new Polygon(
                        4, CELL_SIZE - 6,
                        CELL_SIZE - 4, CELL_SIZE - 6,
                        CELL_SIZE - 10, 6,
                        10, 6
                );
                hull.setFill(Color.web("#34495e"));
                group.getChildren().addAll(background, hull);
                return group;
            }
            case MISS -> {
                background.setFill(Color.web("#aed9e0"));
                Circle splash = new Circle(CELL_SIZE / 2.0, CELL_SIZE / 2.0, 5);
                splash.setFill(Color.web("#2980b9"));
                group.getChildren().addAll(background, splash);
                return group;
            }
            case HIT -> {
                background.setFill(Color.web("#e67e22"));
                Line l1 = new Line(6, 6, CELL_SIZE - 6, CELL_SIZE - 6);
                Line l2 = new Line(CELL_SIZE - 6, 6, 6, CELL_SIZE - 6);
                l1.setStroke(Color.web("#c0392b"));
                l2.setStroke(Color.web("#c0392b"));
                l1.setStrokeWidth(3);
                l2.setStrokeWidth(3);
                group.getChildren().addAll(background, l1, l2);
                return group;
            }
            case SUNK -> {
                background.setFill(Color.web("#c0392b"));
                Line l1 = new Line(6, 6, CELL_SIZE - 6, CELL_SIZE - 6);
                Line l2 = new Line(CELL_SIZE - 6, 6, 6, CELL_SIZE - 6);
                l1.setStroke(Color.web("#2c3e50"));
                l2.setStroke(Color.web("#2c3e50"));
                l1.setStrokeWidth(4);
                l2.setStrokeWidth(4);
                group.getChildren().addAll(background, l1, l2);
                return group;
            }
        }
        group.getChildren().add(background);
        return group;
    }
}