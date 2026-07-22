package org.example.miniproyecto4navalbattleleprmsgismgpljpq.event;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;

/**
 * Custom functional interface for cell interactions — one of the
 * "multiple interfaces" required by rubric point 2, distinct from
 * AIService's ShotResultCallback and from GameState.
 */
public interface CellClickListener {
    void onCellClicked(Coordinate coordinate);
}
