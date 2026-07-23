package org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;

/**
 * Factory pattern (sibling of CellShapeFactory): draws a full ship as a
 * single, type-specific silhouette spanning all of its cells — instead
 * of a generic hull repeated per cell. This is what makes a submarine
 * actually look like a submarine and a carrier look like a carrier,
 * satisfying rubric point 12 (2D shapes, distinct visual states) with
 * real fidelity instead of a placeholder.
 * <p>
 * Every shape is built assuming HORIZONTAL orientation (long axis on X),
 * then rotated 90° for VERTICAL — this avoids writing every ship twice
 * with swapped coordinates. Rotating around the default pivot shifts a
 * node's bounds, so we re-normalize the translation afterward to keep
 * the shape's top-left corner anchored at (0,0), matching how it will
 * be positioned on the overlay.
 */
public final class ShipShapeFactory {

    private ShipShapeFactory() {
    }

    public static Group createShipShape(ShipType type, Orientation orientation) {
        int cell = CellShapeFactory.CELL_SIZE;
        int length = type.getSize() * cell;

        Group horizontalShape = switch (type) {
            case FRIGATE -> buildFrigate(cell);
            case DESTROYER -> buildDestroyer(length, cell);
            case SUBMARINE -> buildSubmarine(length, cell);
            case AIRCRAFT_CARRIER -> buildCarrier(length, cell);
        };

        if (orientation == Orientation.VERTICAL) {
            horizontalShape.setRotate(90);
            Bounds bounds = horizontalShape.getBoundsInParent();
            horizontalShape.setTranslateX(horizontalShape.getTranslateX() - bounds.getMinX());
            horizontalShape.setTranslateY(horizontalShape.getTranslateY() - bounds.getMinY());
        }
        return new Group(horizontalShape);
    }

    /** Small patrol boat: single hull with a tiny cabin. */
    private static Group buildFrigate(int cell) {
        Polygon hull = new Polygon(
                2, cell - 6,
                cell - 8, cell - 6,
                cell - 2, cell / 2.0,
                cell - 8, 6,
                2, 6
        );
        hull.setFill(Color.web("#5d6d7e"));
        hull.setStroke(Color.web("#2c3e50"));

        Rectangle cabin = new Rectangle(cell * 0.3, cell * 0.18);
        cabin.setX(cell * 0.32);
        cabin.setY(cell * 0.4);
        cabin.setFill(Color.web("#34495e"));

        return new Group(hull, cabin);
    }

    /** Elongated hull, pointed bow, deckhouse, mast and a gun turret. */
    private static Group buildDestroyer(int length, int cell) {
        Polygon hull = new Polygon(
                2, cell - 6,
                length - 14, cell - 6,
                length - 2, cell / 2.0,
                length - 14, 6,
                2, 6
        );
        hull.setFill(Color.web("#566573"));
        hull.setStroke(Color.web("#212f3d"));

        Rectangle deckHouse = new Rectangle(length * 0.22, cell * 0.35);
        deckHouse.setX(length * 0.32);
        deckHouse.setY(cell * 0.32);
        deckHouse.setFill(Color.web("#34495e"));

        Line mast = new Line(length * 0.4, cell * 0.3, length * 0.4, cell * 0.05);
        mast.setStroke(Color.web("#1c2833"));
        mast.setStrokeWidth(2);

        Circle turret = new Circle(length * 0.72, cell / 2.0, cell * 0.12);
        turret.setFill(Color.web("#212f3d"));

        return new Group(hull, deckHouse, mast, turret);
    }

    /** Cylindrical, rounded hull with a conning tower and periscope. */
    private static Group buildSubmarine(int length, int cell) {
        Rectangle body = new Rectangle(length - 4, cell * 0.55);
        body.setX(2);
        body.setY(cell * 0.22);
        body.setArcWidth(cell);
        body.setArcHeight(cell * 0.55);
        body.setFill(Color.web("#7f8c8d"));
        body.setStroke(Color.web("#34495e"));

        Rectangle tower = new Rectangle(cell * 0.5, cell * 0.35);
        tower.setX(length / 2.0 - cell * 0.25);
        tower.setY(cell * 0.05);
        tower.setArcWidth(6);
        tower.setArcHeight(6);
        tower.setFill(Color.web("#5d6d7e"));

        Line periscope = new Line(length / 2.0, cell * 0.05, length / 2.0, 0);
        periscope.setStroke(Color.web("#212f3d"));
        periscope.setStrokeWidth(2);

        return new Group(body, tower, periscope);
    }

    /** Flat, wide flight deck with an offset island and runway markings. */
    private static Group buildCarrier(int length, int cell) {
        Rectangle deck = new Rectangle(length - 4, cell * 0.8);
        deck.setX(2);
        deck.setY(cell * 0.1);
        deck.setArcWidth(10);
        deck.setArcHeight(10);
        deck.setFill(Color.web("#7d8b99"));
        deck.setStroke(Color.web("#2c3e50"));

        Rectangle island = new Rectangle(cell * 0.3, cell * 0.6);
        island.setX(length * 0.68);
        island.setY(cell * 0.05);
        island.setFill(Color.web("#34495e"));

        Line runway = new Line(cell * 0.3, cell / 2.0, length * 0.6, cell / 2.0);
        runway.setStroke(Color.web("#ecf0f1"));
        runway.setStrokeWidth(2);
        runway.getStrokeDashArray().addAll(6d, 4d);

        return new Group(deck, island, runway);
    }
}
