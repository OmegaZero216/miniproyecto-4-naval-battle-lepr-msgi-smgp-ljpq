package org.example.miniproyecto4navalbattleleprmsgismgpljpq.event;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Adapter for hover feedback (Nielsen heuristic: "visibility of system
 * status" / immediate feedback). Kept separate from click handling so
 * each event type has a single, focused responsibility.
 */
public class CellHoverAdapter implements EventHandler<MouseEvent> {

    private final Node node;
    private final boolean entering;

    public CellHoverAdapter(Node node, boolean entering) {
        this.node = node;
        this.entering = entering;
    }

    @Override
    public void handle(MouseEvent event) {
        node.setOpacity(entering ? 0.7 : 1.0);
    }
}
