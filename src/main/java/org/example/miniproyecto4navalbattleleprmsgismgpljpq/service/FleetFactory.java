package org.example.miniproyecto4navalbattleleprmsgismgpljpq.service;

import org.example.miniproyecto4navalbattleleprmsgismgpljpq.config.GameConfig;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Factory pattern: produces the ordered queue of ships a fleet must
 * contain, based on GameConfig's counts. Neither PreparationController
 * nor the AI placement logic hard-codes "1 carrier, 2 submarines...";
 * they simply consume whatever this factory produces — if the fleet
 * composition changes in GameConfig, this is the only class affected
 * (Open/Closed Principle).
 * <p>
 * A Deque is used (rather than a List) so both placement flows can
 * simply pollFirst() the next ship to place, without needing an index.
 */
public final class FleetFactory {

    private FleetFactory() {
    }

    public static Deque<ShipType> generateFleetOrder() {
        Deque<ShipType> fleet = new ArrayDeque<>();
        addN(fleet, ShipType.AIRCRAFT_CARRIER, GameConfig.AIRCRAFT_CARRIER_COUNT);
        addN(fleet, ShipType.SUBMARINE, GameConfig.SUBMARINE_COUNT);
        addN(fleet, ShipType.DESTROYER, GameConfig.DESTROYER_COUNT);
        addN(fleet, ShipType.FRIGATE, GameConfig.FRIGATE_COUNT);
        return fleet;
    }

    private static void addN(Deque<ShipType> fleet, ShipType type, int count) {
        for (int i = 0; i < count; i++) {
            fleet.addLast(type);
        }
    }
}
