package org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;

/**
 * Builds the draggable visual for one pending ship in the fleet panel,
 * showing all of its cells together (rather than a single icon) so the
 * player sees the ship's real size before placing it — this directly
 * improves the "coincidencia entre el sistema y el mundo real" and
 * "visibilidad del estado del sistema" Nielsen heuristics already
 * documented in Stage 6.
 * <p>
 * Reuses the same visual language as CellShapeFactory (Rectangle-based
 * hull segments) instead of introducing a second, inconsistent way of
 * drawing ships — keeping "consistencia y estándares" intact too.
 */
public final class ShipPaletteItem {

    private static final int SEGMENT_SIZE = 22;

    private ShipPaletteItem() {
    }

    public static Group create(ShipType type, Orientation orientation) {
        var container = orientation == Orientation.HORIZONTAL ? new HBox() : new VBox();
        for (int i = 0; i < type.getSize(); i++) {
            Rectangle segment = new Rectangle(SEGMENT_SIZE, SEGMENT_SIZE);
            segment.setFill(Color.web("#34495e"));
            segment.setStroke(Color.web("#2c3e50"));
            container.getChildren().add(segment);
        }
        Group group = new Group(container);
        group.setUserData(type); // Permite recuperar el ShipType al soltar en el tablero.
        return group;
    }
}
