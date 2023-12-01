package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class SampleController {
	
	@FXML
	private GridPane cfbGrid;
	
	@FXML
	private Circle player1piece;
	
	@FXML
	private Circle player2piece;
	
	@FXML
	private Circle draggedCircle;
	
	//Shadow Data Members
	private static int ROWS = 7;
	private static int COLUMNS = 6;
	
	//2D Array of bools will serve for "if piece is at that position" flag
	private static boolean[][] cfbData = new boolean[COLUMNS][ROWS];
	
	public void initialize() {
        // Populate the Connect Four board for testing visuals
		// Populate the shadow data structure as as well

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
            	//False
            	cfbData[col][row] = false;
            	//System.out.println( cfbData[col][row] );
            	//System.out.println( col );
            	//System.out.println( row );
            	
            	// Create a circle as a marker
                Circle draggedCircle = createCircle(row, col);

            	// Set up event handlers for drag-and-drop
                setDragAndDropHandlers(draggedCircle);
            	
                // Create a label as a marker
                //Label markerLabel = createMarkerLabel(row, col);
                //Circle markerCircle = createCircle(row, col);
                
                // Add the label/circle to the GridPane
                //cfbGrid.add(markerLabel, col, row);
                //cfbGrid.add(draggedCircle, col, row);
            }
        }
    }
	
	private Circle createCircle(int row, int col) {
		Circle circle = new Circle(20);
		circle.setStyle("fx-stroke: black; fx-fill: red;");
		
		return circle;
	}
	
	private void setDragAndDropHandlers(Circle circle) {
	    circle.setOnMousePressed(event -> onMousePressed(circle, event));
	    circle.setOnMouseDragged(event -> onMouseDragged(circle, event));
	    circle.setOnMouseReleased(event -> onMouseReleased(circle, event));
	}

	private void onMouseReleased(Circle circle, MouseEvent event) {
		draggedCircle = null;
	}

	private void onMousePressed(Circle circle, MouseEvent event) {
		draggedCircle = circle;
        // Add any additional logic you need when the circle is pressed
    
	}

	private void onMouseDragged(Circle circle, MouseEvent event) {
		if (draggedCircle == circle) {
            // Update the circle position during drag
            circle.setCenterX(event.getX());
            circle.setCenterY(event.getY());
        }
		
	}

	private void dragPlayPiece() {
		
	}

	@SuppressWarnings("unused")
	private Label createMarkerLabel(int row, int col) {
        Label label = new Label();
        label.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        label.setText("[" + row + "," + col + "]");
        return label;
    }
	
	
}
