package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ConnectFourController {

    @FXML
    private GridPane cfbGrid;
    
    @FXML
    private Circle player1piece;
    
    @FXML
    private Circle player2piece;
    
    @FXML
    private Label turnLabel;
    
    private static final int ROWS = 7;
    private static final int COLUMNS = 6;
    private static final int CELL_SIZE = 15;

    private Circle[][] circles = new Circle[COLUMNS][ROWS];
    private Circle draggedCircle;
    

    private boolean playerTurn = true; // true for Player 1, false for Player 2

    public ConnectFourController() {
        // Initialize the circles array
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                circles[col][row] = new Circle(CELL_SIZE, Color.TRANSPARENT);
            }
        }
    }

    @FXML
    private void initialize() {
        // Populate the game grid with circles and set up drag-and-drop event handlers
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                final int finalCol = col; // Declare col as effectively final
                final int finalRow = row; // Declare row as effectively final

                Circle circle = circles[finalCol][finalRow];
                
                circle.setOnMouseClicked(event -> handleCellClick(finalCol, finalRow));
                circle.setOnMousePressed(event -> handleMousePressed(circle, event));
                circle.setOnMouseDragged(event -> handleMouseDragged(circle, event));
                
                cfbGrid.add(circle, finalCol, finalRow);
                
                //switch player
                switchPlayerTurn();
                
                // Set up the initial turn label
                updateTurnLabel();
            }
        }
    }

    
    private void updateTurnLabel() {
		// TODO Auto-generated method stub
    	String playerTurnText = playerTurn ? "Player 1's Turn" : "Player 2's Turn";
        turnLabel.setText(playerTurnText);
		
	}
    
    
    private void switchPlayerTurn() {
        playerTurn = !playerTurn;
        updateTurnLabel();
    }

	private void handleCellClick(int col, int row) {
        if (circles[col][row].getFill().equals(Color.TRANSPARENT)) {
            Color discColor = playerTurn ? Color.RED : Color.YELLOW;
            circles[col][row].setFill(discColor);
            playerTurn = !playerTurn;

            // Add any game logic you need here based on the click
            // For example, check for a win condition or switch player turns
            switchPlayerTurn();
        }
    }

    private void handleMousePressed(Circle circle, MouseEvent event) {
        draggedCircle = circle;
        // Set the circle color to transparent before dragging
        draggedCircle.setFill(Color.GRAY);
    }
    
    private void handleMouseDragged(Circle circle, MouseEvent event) {
        if (draggedCircle == circle) {
            // Update the circle position during drag
            draggedCircle.setCenterX(event.getX());
            draggedCircle.setCenterY(event.getY());
        }
    }
    
    @FXML
    private void handleMouseReleased(MouseEvent event) {
        if (draggedCircle != null) {
            // Determine the grid cell where the circle is released
            int col = (int) (event.getX() / CELL_SIZE);
            int row = (int) (event.getY() / CELL_SIZE);

            // Ensure the column is within bounds
            col = Math.min(Math.max(col, 0), COLUMNS - 1);

            // Set the circle's position to the center of the grid cell
            draggedCircle.setCenterX(col * CELL_SIZE + CELL_SIZE / 2);
            draggedCircle.setCenterY(row * CELL_SIZE + CELL_SIZE / 2);

            // Update the shadow data structure or handle dropping logic
            handleCellClick(col, row);
            
            // Switch player turn
            switchPlayerTurn();

            // Reset draggedCircle to null
            draggedCircle = null;
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
