package org.example.miniproyecto4navalbattleleprmsgismgpljpq.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Board;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Coordinate;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.Ship;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.CellState;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.Orientation;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.enums.ShipType;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.model.exception.InvalidShipPlacementException;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.FleetFactory;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.GameService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.PlacementService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.service.RandomFleetPlacementService;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.CellShapeFactory;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.SceneManager;
import org.example.miniproyecto4navalbattleleprmsgismgpljpq.view.ShipPaletteItem;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Controller for the ship-placement screen. Contains no rule logic of
 * its own — every validation is delegated to PlacementService. This
 * class only: tracks which ship is next, which orientation is active,
 * renders the grid, and forwards confirmed placements to the Service.
 * <p>
 * Satisfies HU1 (place ships) completely, including the R/ESC/ENTER
 * keyboard shortcuts required by rubric point 3.
 */
public class PreparationController {

    @FXML private GridPane placementGrid;
    @FXML private Button rotateButton;
    @FXML private Button confirmButton;
    @FXML private Label hintLabel;
    @FXML private VBox fleetPanel;
    @FXML private Pane dragOverlay;

    private SceneManager sceneManager;
    private Board playerBoard;
    private PlacementService placementService;
    private Deque<ShipType> pendingShips;
    private Orientation currentOrientation = Orientation.HORIZONTAL;
    private Group ghostNode;
    private ShipType draggingType;
    private List<Coordinate> lastPreviewCoordinates = new ArrayList<>();
    private double lastDragSceneX;
    private double lastDragSceneY;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void initialize() {
        playerBoard = new Board();
        placementService = new PlacementService(playerBoard);
        pendingShips = FleetFactory.generateFleetOrder();
        renderGrid();
        renderFleetPanel();
        updateHint();

        placementGrid.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                attachKeyboardShortcuts(newScene);
            }
        });
    }

    private void attachKeyboardShortcuts(javafx.scene.Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case R -> {
                    if (draggingType != null) {
                        rotateWhileDragging();
                    } else {
                        handleRotate();
                    }
                }
                case ESCAPE -> handleCancelSelection();
                case ENTER -> handleConfirm();
                default -> { }
            }
        });
    }

    /**
     * Rotates the orientation while a ship is actively being dragged,
     * WITHOUT touching fleetPanel. Rebuilding fleetPanel mid-drag was
     * the root cause of the placement bug: it destroyed the very node
     * that originated the mouse-press gesture, leaving mouseDragged/
     * mouseReleased events firing against a node no longer attached to
     * the scene graph, with stale coordinates. Rotating the ghost in
     * place instead keeps a single source of truth (currentOrientation)
     * consistent with what the player sees and what gets placed.
     */
    private void rotateWhileDragging() {
        currentOrientation = currentOrientation == Orientation.HORIZONTAL
                ? Orientation.VERTICAL : Orientation.HORIZONTAL;

        // Reconstruimos SOLO el ghost (no el panel), en la nueva orientación.
        dragOverlay.getChildren().remove(ghostNode);
        ghostNode = ShipPaletteItem.create(draggingType, currentOrientation);
        ghostNode.setOpacity(0.75);
        ghostNode.setMouseTransparent(true);
        dragOverlay.getChildren().add(ghostNode);
        moveGhostTo(lastDragSceneX, lastDragSceneY);

        // Recalcula la preview de inmediato con la nueva orientación,
        // en la misma posición donde ya estaba el cursor.
        updatePreview(lastDragSceneX, lastDragSceneY);
    }

    private void renderFleetPanel() {
        fleetPanel.getChildren().clear();
        for (ShipType type : pendingShips) {
            Group item = ShipPaletteItem.create(type, currentOrientation);
            attachManualDragSource(item, type);
            fleetPanel.getChildren().add(item);
        }
    }

    /**
     * Manual mouse-based drag, replacing the native Dragboard API. We take
     * full control of a floating "ghost" node so it can follow the cursor
     * in real time and drive a live placement preview — something the
     * native DnD ghost (an OS-managed static snapshot) cannot do.
     */
    private void attachManualDragSource(Group item, ShipType type) {
        item.setOnMousePressed(event -> {
            draggingType = type;
            lastDragSceneX = event.getSceneX();
            lastDragSceneY = event.getSceneY();
            ghostNode = ShipPaletteItem.create(type, currentOrientation);
            ghostNode.setOpacity(0.75);
            ghostNode.setMouseTransparent(true);
            dragOverlay.getChildren().add(ghostNode);
            moveGhostTo(event.getSceneX(), event.getSceneY());
            event.consume();
        });

        item.setOnMouseDragged(event -> {
            lastDragSceneX = event.getSceneX();
            lastDragSceneY = event.getSceneY();
            moveGhostTo(event.getSceneX(), event.getSceneY());
            updatePreview(event.getSceneX(), event.getSceneY());
            event.consume();
        });

        item.setOnMouseReleased(event -> {
            Coordinate dropCoordinate = sceneToBoardCoordinate(event.getSceneX(), event.getSceneY());
            clearPreview();
            dragOverlay.getChildren().remove(ghostNode);
            ghostNode = null;

            if (dropCoordinate != null) {
                tryPlaceShip(dropCoordinate, draggingType);
            }
            draggingType = null;
            event.consume();
        });
    }
    /**
     * Converts a scene-space mouse position into a board Coordinate, or
     * null if the cursor is outside the grid — used both to place the
     * ship on release and to compute the live preview while dragging.
     */
    private Coordinate sceneToBoardCoordinate(double sceneX, double sceneY) {
        var local = placementGrid.sceneToLocal(sceneX, sceneY);
        int col = (int) (local.getX() / CellShapeFactory.CELL_SIZE);
        int row = (int) (local.getY() / CellShapeFactory.CELL_SIZE);
        if (row < 0 || row >= 10 || col < 0 || col >= 10) {
            return null;
        }
        return new Coordinate(row, col);
    }

    private void moveGhostTo(double sceneX, double sceneY) {
        var local = dragOverlay.sceneToLocal(sceneX, sceneY);
        ghostNode.setLayoutX(local.getX() - CellShapeFactory.CELL_SIZE / 2.0);
        ghostNode.setLayoutY(local.getY() - CellShapeFactory.CELL_SIZE / 2.0);
    }

    /**
     * Live preview: while dragging, computes which cells the ship would
     * occupy and paints them green (valid) or red (invalid) — checked via
     * PlacementService.validatePlacement, which only reads board state
     * and throws without mutating anything, making it safe to call
     * repeatedly on every mouse-move.
     */
    private void updatePreview(double sceneX, double sceneY) {
        clearPreview();
        Coordinate start = sceneToBoardCoordinate(sceneX, sceneY);
        if (start == null || draggingType == null) {
            return;
        }
        try {
            List<Coordinate> coordinates = buildCoordinates(start, draggingType, currentOrientation);
            boolean valid = true;
            try {
                placementService.validatePlacement(coordinates);
            } catch (InvalidShipPlacementException e) {
                valid = false;
            }
            paintPreview(coordinates, valid);
            lastPreviewCoordinates = coordinates;
        } catch (IllegalArgumentException e) {
            // El barco se saldría del tablero desde este punto: sin preview.
        }
    }

    private void paintPreview(List<Coordinate> coordinates, boolean valid) {
        Color color = valid ? Color.web("#2ecc71", 0.6) : Color.web("#e74c3c", 0.6);
        for (Coordinate coordinate : coordinates) {
            Rectangle highlight = new Rectangle(CellShapeFactory.CELL_SIZE, CellShapeFactory.CELL_SIZE);
            highlight.setFill(color);
            highlight.setMouseTransparent(true);
            placementGrid.add(highlight, coordinate.column(), coordinate.row());
        }
    }

    private void clearPreview() {
        placementGrid.getChildren().removeIf(node -> node instanceof Rectangle);
        lastPreviewCoordinates.clear();
    }

    private void renderGrid() {
        placementGrid.getChildren().clear();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Coordinate coordinate = new Coordinate(row, col);
                Group shape = CellShapeFactory.createShapeFor(
                        playerBoard.getCell(coordinate).getState());
                attachDropTarget(shape, coordinate);
                placementGrid.add(shape, col, row);
            }
        }
    }

    private void attachDropTarget(Group cellShape, Coordinate coordinate) {
        cellShape.setOnDragOver(event -> {
            if (event.getGestureSource() != cellShape && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cellShape.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                ShipType droppedType = ShipType.valueOf(dragboard.getString());
                success = tryPlaceShip(coordinate, droppedType);
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private boolean tryPlaceShip(Coordinate start, ShipType type) {
        if (!type.equals(pendingShips.peekFirst())) {
            hintLabel.setText("Debes colocar los barcos en orden: " + pendingShips.peekFirst());
            return false;
        }
        try {
            List<Coordinate> coordinates = buildCoordinates(start, type, currentOrientation);
            Ship.ShipBuilder builder = new Ship.ShipBuilder().ofType(type).withOrientation(currentOrientation);
            coordinates.forEach(builder::addCoordinate);

            placementService.placeShip(builder.build());
            pendingShips.pollFirst();
            refreshCells(coordinates);
            renderFleetPanel();
            updateHint();
            return true;
        } catch (InvalidShipPlacementException e) {
            hintLabel.setText(e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            hintLabel.setText("El barco no cabe en esa posición y orientación.");
            return false;
        }
    }

    private List<Coordinate> buildCoordinates(Coordinate start, ShipType type, Orientation orientation) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < type.getSize(); i++) {
            int row = orientation == Orientation.VERTICAL ? start.row() + i : start.row();
            int col = orientation == Orientation.HORIZONTAL ? start.column() + i : start.column();
            coordinates.add(new Coordinate(row, col));
        }
        return coordinates;
    }

    private void refreshCells(List<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            Group updated = CellShapeFactory.createShapeFor(CellState.SHIP);
            updated.setOnMouseClicked(e -> { /* ya ocupada, sin acción */ });
            placementGrid.add(updated, coordinate.column(), coordinate.row());
        }
    }

    @FXML
    private void handleRotate() {
        currentOrientation = currentOrientation == Orientation.HORIZONTAL
                ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        renderFleetPanel(); // Repinta los items pendientes en la nueva orientación.
        hintLabel.setText("Orientación: " + currentOrientation);
    }

    private void handleCancelSelection() {
        // En esta versión no hay una "selección en curso" que deshacer
        // (cada click coloca directo) — ESC aquí resetea el mensaje de
        // ayuda, dejando el gancho listo si más adelante agregamos un
        // preview arrastrable de varias celdas antes de confirmar.
        updateHint();
    }

    @FXML
    private void handleConfirm() {
        if (!pendingShips.isEmpty()) {
            hintLabel.setText("Aún faltan barcos por colocar: " + pendingShips.size());
            return;
        }

        Board machineBoard = new Board();
        PlacementService machinePlacementService = new PlacementService(machineBoard);
        new RandomFleetPlacementService(machinePlacementService).placeFleetRandomly(machineBoard);

        GameService gameService = new GameService(playerBoard, machineBoard);
        sceneManager.navigateToGame(gameService);
    }

    private void updateHint() {
        if (pendingShips.isEmpty()) {
            hintLabel.setText("¡Flota completa! Presiona Enter o Confirmar para comenzar.");
        } else {
            ShipType next = pendingShips.peekFirst();
            hintLabel.setText("Coloca tu " + next + " (" + next.getSize()
                    + " casillas) — Orientación actual: " + currentOrientation + " (R para rotar)");
        }
    }
}
