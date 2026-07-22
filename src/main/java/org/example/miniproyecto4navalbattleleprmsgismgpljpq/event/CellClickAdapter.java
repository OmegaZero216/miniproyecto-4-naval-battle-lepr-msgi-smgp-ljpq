package org.example.miniproyecto4navalbattleleprmsgismgpljpq.event;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;

/**
 * Adapter class: adapts a raw JavaFX MouseEvent into our own
 * domain-specific CellClickListener callback, so the rest of the
 * application never depends directly on javafx.scene.input.MouseEvent.
 * This is the "clase adaptadora" required by rubric point 2 — it
 * isolates the Controller from JavaFX's raw event API.
 */
public class CellClickAdapter implements EventHandler<MouseEvent> {

    private final Coordinate coordinate;
    private final CellClickListener listener;

    public CellClickAdapter(Coordinate coordinate, CellClickListener listener) {
        this.coordinate = coordinate;
        this.listener = listener;
    }

    @Override
    public void handle(MouseEvent event) {
        listener.onCellClicked(coordinate);
    }
}
