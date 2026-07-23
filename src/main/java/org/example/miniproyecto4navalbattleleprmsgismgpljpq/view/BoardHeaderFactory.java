package org.example.miniproyecto4navalbattleleprmsgismgpljpq.view;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Agrega las cabeceras de coordenadas (A-J arriba, 1-10 a la izquierda)
 * a un GridPane de tablero. Las celdas del tablero se desplazan una
 * fila y una columna (offset +1) para dejarles espacio.
 */
public final class BoardHeaderFactory {

    private BoardHeaderFactory() {
    }

    public static void addHeaders(GridPane grid) {
        // Esquina vacía (fila 0, columna 0)
        grid.add(new StackPane(), 0, 0);

        // Columnas A-J
        for (int col = 0; col < 10; col++) {
            char letter = (char) ('A' + col);
            grid.add(headerCell(String.valueOf(letter)), col + 1, 0);
        }

        // Filas 1-10
        for (int row = 0; row < 10; row++) {
            grid.add(headerCell(String.valueOf(row + 1)), 0, row + 1);
        }
    }

    private static StackPane headerCell(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("coord-header");
        StackPane cell = new StackPane(label);
        cell.setPrefSize(CellShapeFactory.CELL_SIZE, CellShapeFactory.CELL_SIZE);
        return cell;
    }
}