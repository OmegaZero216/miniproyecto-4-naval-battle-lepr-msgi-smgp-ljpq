package org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Factory pattern: crea la representación visual (un Node de JavaFX)
 * para un CellState dado, usando únicamente Shapes — nunca imágenes,
 * según el punto 12 de la rúbrica. Sigue el lenguaje visual de la
 * referencia del enunciado: agua celeste uniforme, X roja para fallado,
 * fondo blanco + ícono para tocado/hundido.
 */
public final class CellShapeFactory {

    public static final int CELL_SIZE = 32;

    private static final Color WATER = Color.web("#aee3f2");
    private static final Color GRID_LINE = Color.web("#6fb8d1");

    private CellShapeFactory() {
    }

    public static Group createShapeFor(CellState state) {
        return switch (state) {
            case EMPTY -> water();
            case SHIP -> shipCell();
            case MISS -> miss();
            case HIT -> hit();
            case SUNK -> sunk();
        };
    }

    private static Rectangle background(Color fill) {
        Rectangle bg = new Rectangle(CELL_SIZE, CELL_SIZE);
        bg.setFill(fill);
        bg.setStroke(GRID_LINE);
        bg.setStrokeWidth(1);
        return bg;
    }

    private static Group water() {
        return new Group(background(WATER));
    }

    private static Group shipCell() {
        Group group = new Group(background(WATER));

        Rectangle hull = new Rectangle(3, 6, CELL_SIZE - 6, CELL_SIZE - 12);
        hull.setArcWidth(10);
        hull.setArcHeight(10);
        hull.setFill(new javafx.scene.paint.LinearGradient(
                0, 0, 0, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0, Color.web("#b0bec5")),
                new javafx.scene.paint.Stop(0.5, Color.web("#78909c")),
                new javafx.scene.paint.Stop(1, Color.web("#455a64"))
        ));
        hull.setStroke(Color.web("#263238"));
        hull.setStrokeWidth(1.5);
        hull.setEffect(new DropShadow(4, Color.web("#000000", 0.5)));

        Rectangle highlight = new Rectangle(6, 8, CELL_SIZE - 12, 2.5);
        highlight.setArcWidth(4);
        highlight.setArcHeight(4);
        highlight.setFill(Color.web("#eceff1", 0.55));

        Rectangle deckLine = new Rectangle(6, CELL_SIZE / 2.0 - 1, CELL_SIZE - 12, 2);
        deckLine.setFill(Color.web("#263238"));

        group.getChildren().addAll(hull, highlight, deckLine);
        return group;
    }

    private static Group miss() {
        Group group = new Group(background(WATER));

        Line l1 = new Line(8, 8, CELL_SIZE - 8, CELL_SIZE - 8);
        Line l2 = new Line(CELL_SIZE - 8, 8, 8, CELL_SIZE - 8);
        l1.setStroke(Color.web("#c0392b"));
        l2.setStroke(Color.web("#c0392b"));
        l1.setStrokeWidth(3.5);
        l2.setStrokeWidth(3.5);
        l1.setStrokeLineCap(StrokeLineCap.ROUND);
        l2.setStrokeLineCap(StrokeLineCap.ROUND);

        group.getChildren().addAll(l1, l2);
        return group;
    }

    private static Group hit() {
        Group group = new Group(background(Color.WHITE));

        Circle bomb = new Circle(CELL_SIZE / 2.0, CELL_SIZE / 2.0 + 3, 7);
        bomb.setFill(Color.web("#1c1c1c"));

        Line fuse = new Line(CELL_SIZE / 2.0 + 4, CELL_SIZE / 2.0 - 4,
                CELL_SIZE / 2.0 + 9, CELL_SIZE / 2.0 - 10);
        fuse.setStroke(Color.web("#1c1c1c"));
        fuse.setStrokeWidth(2);

        Circle spark = new Circle(CELL_SIZE / 2.0 + 10, CELL_SIZE / 2.0 - 11, 2.5);
        spark.setFill(Color.web("#f1c40f"));
        spark.setEffect(new DropShadow(6, Color.web("#f39c12")));

        group.getChildren().addAll(bomb, fuse, spark);
        return group;
    }

    private static Group sunk() {
        Group group = new Group(background(Color.WHITE));

        javafx.scene.shape.Path flame = new javafx.scene.shape.Path();
        flame.getElements().addAll(
                new javafx.scene.shape.MoveTo(16, 5),
                new javafx.scene.shape.CubicCurveTo(24, 13, 22, 20, 16, 27),
                new javafx.scene.shape.CubicCurveTo(10, 21, 9, 16, 12.5, 17.5),
                new javafx.scene.shape.CubicCurveTo(9.5, 13, 11, 8, 16, 5)
        );
        flame.setFill(Color.web("#f39c12"));
        flame.setStroke(Color.web("#c0392b"));
        flame.setStrokeWidth(1);
        flame.setStrokeLineJoin(StrokeLineJoin.ROUND);
        flame.setEffect(new DropShadow(6, Color.web("#e74c3c")));

        group.getChildren().add(flame);
        return group;
    }
}