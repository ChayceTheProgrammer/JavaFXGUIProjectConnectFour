package application;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ConnectFourController {

    @FXML
    private GridPane cfbGrid;
    
    @FXML
    private Circle player1piece;

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int CELL_SIZE = 50;

    private Circle[][] circles = new Circle[COLUMNS][ROWS];

    private boolean playerTurn = true; // true for Player 1, false for Player 2

    public ConnectFourController() {
        // Initialize the circles array
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                circles[col][row] = new Circle(CELL_SIZE / 4, Color.TRANSPARENT);
            }
        }
    }

    @FXML
    private void initialize() {
        // Populate the game grid with circles
    	for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
            	//Set black at first then change color to indicate piece is selected
                Circle circle = new Circle(CELL_SIZE / 4, Color.BLACK);
                
                circles[col][row] = circle;
                cfbGrid.add(circle, col, row);
            }
        }
    }

    @FXML
    private void dropDisc() {
        int col = findAvailableRow();
        if (col != -1) {
            Color discColor = playerTurn ? Color.RED : Color.YELLOW;
            circles[col][0].setFill(discColor);
            playerTurn = !playerTurn;
        }
    }

    private int findAvailableRow() {
        // Find the first available row in the selected column
        for (int row = ROWS - 1; row >= 0; row--) {
            if (circles[0][row].getFill().equals(Color.RED)) {
                return row;
            }
        }
        return -1; // Column is full
    }
}
