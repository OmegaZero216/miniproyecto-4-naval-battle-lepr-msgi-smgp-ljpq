package org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;

/**
 * Construye el visual arrastrable de un barco pendiente en el panel de
 * flota, mostrando todas sus celdas juntas (en vez de un ícono único)
 * para que el jugador vea el tamaño real del barco antes de colocarlo.
 * <p>
 * A diferencia de la versión anterior (rectángulos sueltos idénticos),
 * el segmento de proa se dibuja con un Polygon en punta, para que se
 * lea como un barco real y no como una fila de bloques.
 */
public final class ShipPaletteItem {

    private static final int SEGMENT_SIZE = 24;

    private ShipPaletteItem() {
    }

    public static Group create(ShipType type, Orientation orientation) {
        javafx.scene.layout.Pane hull;
        if (orientation == Orientation.HORIZONTAL) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            hull = hbox;
        } else {
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            hull = vbox;
        }

        for (int i = 0; i < type.getSize(); i++) {
            boolean isBow = (i == 0); // primer segmento = proa
            hull.getChildren().add(segment(isBow, orientation));
        }

        Label nameLabel = new Label(displayName(type));
        nameLabel.getStyleClass().add("ship-label");

        VBox container = new VBox(4, nameLabel, hull);
        container.setAlignment(Pos.CENTER);

        Group group = new Group(container);
        group.setUserData(type); // Permite recuperar el ShipType al soltar en el tablero.
        return group;
    }

    private static Group segment(boolean isBow, Orientation orientation) {
        javafx.scene.paint.LinearGradient metal = new javafx.scene.paint.LinearGradient(
                0, 0, 0, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0, Color.web("#b0bec5")),
                new javafx.scene.paint.Stop(0.5, Color.web("#78909c")),
                new javafx.scene.paint.Stop(1, Color.web("#455a64"))
        );

        if (!isBow) {
            Rectangle rect = new Rectangle(SEGMENT_SIZE, SEGMENT_SIZE);
            rect.setArcWidth(4);
            rect.setArcHeight(4);
            rect.setFill(metal);
            rect.setStroke(Color.web("#263238"));
            rect.setStrokeWidth(1.5);
            rect.setEffect(new DropShadow(3, Color.web("#000000", 0.45)));

            Rectangle porthole = new Rectangle(SEGMENT_SIZE / 2.0 - 3, SEGMENT_SIZE / 2.0 - 3, 6, 6);
            porthole.setArcWidth(6);
            porthole.setArcHeight(6);
            porthole.setFill(Color.web("#1b8ca8"));
            porthole.setStroke(Color.web("#0d3b47"));

            return new Group(rect, porthole);
        }

        Polygon bow;
        if (orientation == Orientation.HORIZONTAL) {
            bow = new Polygon(
                    0, 0,
                    SEGMENT_SIZE * 0.6, 0,
                    SEGMENT_SIZE, SEGMENT_SIZE / 2.0,
                    SEGMENT_SIZE * 0.6, SEGMENT_SIZE,
                    0, SEGMENT_SIZE
            );
        } else {
            bow = new Polygon(
                    0, 0,
                    SEGMENT_SIZE, 0,
                    SEGMENT_SIZE, SEGMENT_SIZE * 0.6,
                    SEGMENT_SIZE / 2.0, SEGMENT_SIZE,
                    0, SEGMENT_SIZE * 0.6
            );
        }
        bow.setFill(metal);
        bow.setStroke(Color.web("#263238"));
        bow.setStrokeWidth(1.5);
        bow.setEffect(new DropShadow(4, Color.web("#000000", 0.45)));
        return new Group(bow);
    }

    private static String displayName(ShipType type) {
        return switch (type) {
            case AIRCRAFT_CARRIER -> "Portaaviones";
            case SUBMARINE -> "Submarino";
            case DESTROYER -> "Destructor";
            case FRIGATE -> "Fragata";
        };
    }
}