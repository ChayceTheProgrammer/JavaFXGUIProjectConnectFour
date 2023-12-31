package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Controller implements Initializable {
	
	//number of columns
    private static final int COLUMNS = 6;
    private static final int ROWS = 7;
    
    //pretty self explanatory if you took 8th grade geometry
    private static final int diameterOfCircle = 80;
    
    //hexadecimal (0-255) color of player pieces (had to learn some color theory for this part)
    //Common Color Values:
    // RED    : #FF0000
    // BLUE   : #0000FF
    // Green  : #00FF00
    // BLACK  : #000000
    // WHITE  : #FFFFFF
    
    //Example Color 1: "#24303E"  	//dark blue-gray   - R:24 G:30 B:3
    													 //36, 48, 62
    //Example Color 2: "#4CAA88"	//light green-blue - R:4C G:AA B:88 
	   													 //76, 170, 136
    
    // Developer Note: Background grid color is light turqoise, choose color scheme wisely
    // Edit the FXML (Line 7) Directly or Use SceneBuilder to Modify the hex color
    private static final Color discColor1 = Color.valueOf("#0000FF"); 

    private static final Color discColor2 = Color.valueOf("#FF0000"); 
    //Names For Player
    private static String FirstPlayer = "Player One";
    private static String SecondPlayer = "Player Two";

    //AI player name, members, methods
    private static String AiPlayer = "Player AI";
    
    //true/false flag for whose turn it is
    private boolean isPlayerOneTurn = true;
    
    // Flag to avoid same color disc being added.
    private boolean isAllowedToInsert = true;   

    //shadow data structure of player pieces AKA discs
    private Disc[][] insertedDiscsArray = new Disc[ROWS][COLUMNS];
    
    //pseudorandom distinct extender for the log file name,
    //Cause nearly an endless amount of log files (approx: 2^31) 
    private static Integer RandomFileNameExtender = new Random().nextInt();
    private static String fileExtStr = RandomFileNameExtender.toString();
    
    //log file name
    private static final String LOG_FILE_PATH = "connet4gamelog-" + fileExtStr + "-.txt";
    
    //class level logWriter file to have access from various methods
    //usually these may be a bit harder to access but this project is taking 6ever
    private BufferedWriter logWriterAlpha;
    
    //Advanced AI
    // Define an enumeration for AI difficulty levels
    enum AiDifficulty {
        QUASIRANDOM,   //Implemented
        THOUGHTFUL,    //In development
        ACTUAL_TRYHARD //Future
    }

    //declare a blank GridPane object
    @FXML
    public GridPane rootGridPane;

    //graphical representation of connect four grid
    @FXML
    public Pane insertedDiscsPane;

    //graphical component of whose player it is on window
    @FXML
    public Label playerNameLabel;
    
    //graphical component of Ai player prompt
    @FXML
    public Button aiButton;
    
    @FXML
    //standard naming convention for handling UI events in JavaFX
    private void handleAiButtonClick() {
    	
    	//remove multiplayer
    	SecondPlayer = AiPlayer;
    	
    	//Developer Note: Visual Console FeedBack
    	System.out.println("AI Mode Activated");
    	
        //what happens when button is clicked
        aiButton.setOnAction(event -> {
        	
        	if (isAllowedToInsert && !isPlayerOneTurn) {
        		//pause ability to prevent issues
        		isAllowedToInsert = !isAllowedToInsert;
        		
        		//Set Ai Difficulty
        		AiDifficulty AiDiff = AiDifficulty.QUASIRANDOM;
        		
        		switch (AiDiff) {
        		case QUASIRANDOM:
        			//obtain a random column value
	        		Integer AiMoveColumn = generateAiMoveColumn();
	        		//Developer Debugging Tool
	        		System.out.println("Quasirandom AiMoveColumn:" + AiMoveColumn);
	        		
	            	//Graphically Causing the move to occur
	        		//insert disc to plane at the generated column index
	        		//at quasi-randomly generated position
	        		// Run the graphical update on the JavaFX Application Thread
	                Platform.runLater(() -> {
	                    Disc disc = new Disc(!isPlayerOneTurn, discColor2);
	                    try {
	                        insertDisc(disc, AiMoveColumn);
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                });
	        		break;
				
        		case THOUGHTFUL:
        			   // Calculate the best move 
        		    int thoughtfulMove = calculateThoughtfulMove(insertedDiscsArray, isPlayerOneTurn);
        		    
        		    // Developer Debugging Tool
        		    System.out.println("Thoughtful AiMoveColumn: " + thoughtfulMove);
        		    
        		    // Graphically cause the move to occur
        		    // Run the graphical update on the JavaFX Application Thread
        		    Platform.runLater(() -> {
        		        Disc disc = new Disc(!isPlayerOneTurn, discColor2);
        		        try {
        		            insertDisc(disc, thoughtfulMove);
        		        } catch (IOException e) {
        		            e.printStackTrace();
        		        }
        		    });
        		    break;
        		case ACTUAL_TRYHARD:
        			break;
					
				default:
					break;
        		}
        		
        		//revert ability to prevent issues
        		isAllowedToInsert = !isAllowedToInsert;
        	}
        });
    }
    
    private int calculateThoughtfulMove(Disc[][] insertedDiscsArray2, boolean isPlayerOneTurn2) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int getLastEmptyRow(int column) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (getDiscIfPresent(row, column) == null) {
                return row;
            }
        }
        // If the column is full, return -1 or handle it as appropriate for your game logic
        return -1;
    }

    //this method generates a random AI move, programatically,
    //method is used in the createPlayground() method
    private int generateAiMoveColumn() {
    	//generate a number between 0 and (COLUMNS : 6) inclusive
    	return new Random().nextInt(COLUMNS);
    }
    
    //Gaming fr
    /* 
	This method serves as the entry point for creating the game board. 
     * It calls other helper methods: createGameStructuralGrid and createClickableColumns
     * to set up different components of the graphical connect four game board.
    */
    public void createPlayground() {

    	//initialize gameboard
        Shape rectangleWithHoles = createGameStructuralGrid();
        
        //add grid to window (specifically the rootGridPane)
        rootGridPane.add(rectangleWithHoles, 0, 1);

        //creating a list of clickable columns
        List<Rectangle> rectangleList = createClickableColumns();

        //Add columns to window -> rootgridPane
        for (Rectangle rectangle: rectangleList) {
            rootGridPane.add(rectangle, 0, 1);
        }
        
        //button for AI player
        aiButton.setText("Ai Player");
        
        //Add Ai Button to the rootGridPane
        rootGridPane.add(aiButton, 1, 1); 
        
        //Game State of AI Move is pressed first
        if(isAllowedToInsert) {
        	if(isPlayerOneTurn) {
        		return;
        	}
        	
        	//first column choice of Ai
        	int initialAiMoveColumn = generateAiMoveColumn();
        	
        	Disc disc = new Disc(!isPlayerOneTurn, discColor2);
        	try {
				insertDisc(disc, initialAiMoveColumn);
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }   
    }
    
    /*
	This method uses lists of points representing the positions in vertical, 
    horizontal, and diagonal directions around the recently inserted disc. 
    It then calls the checkCombinations method to determine if any winning 
    combinations exist in these directions. The result boolean (isEnded) is then returned 
    to indicate whether the game has ended.
    */
    private boolean gameEnded(int row, int column) {
    	// Generate points for vertical, horizontal, and diagonal directions
    	
    	/*
    	IntStream.rangeClosed(row - 3, row + 3) generates a range of integer values 
    	from row - 3 to row + 3 (inclusive). For each value r in this range, a new 
    	Point2D object is created with coordinates (r, column). The resulting points
    	are collected into a list, forming a vertical line of points around the given position.
    	*/
    	
    	//graphical 2D points like (x, y)
        List<Point2D> verticalPoints = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(r, column))
                .collect(Collectors.toList());

        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(col -> new Point2D(row, col))
                .collect(Collectors.toList());

        /*diagonal1Points represents a list of points along a diagonal line in the 2D space. 
        The starting point of the diagonal is startPoint1, and it extends to the right and 
        upwards. Each point in the list is displaced by a certain amount along both the x 
        and y axes, creating a diagonal line.*/
        
        //creating both diagonals from a center of the board
        Point2D startPoint1 = new Point2D(row - 3, column + 3);
        
        //This creates an IntStream containing integer values from 0 to 6 (inclusive).
        List<Point2D> diagonal1Points = IntStream.rangeClosed(0, 6)
        		//For each value i in the stream, a Point2D object is created by adding
        		//i to the x-coordinate (startPoint1.getX() + i) and subtracting i from 
        		//the y-coordinate (startPoint1.getY() - i). This effectively creates points 
        		//along a diagonal line where the x-coordinate increases, and the y-coordinate 
        		//decreases-BottomRight.
                .mapToObj(i -> startPoint1.add(i, -i))
                //The resulting Point2D objects are collected into a List<Point2D>
                .collect(Collectors.toList());
        
        //other direction
        Point2D startPoint2 = new Point2D(row - 3, column - 3);
        List<Point2D> diagonal2Points = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> startPoint2.add(i, i))
                .collect(Collectors.toList());
        
        /*
        The lists of points (verticalPoints, horizontalPoints, etc.) are then 
        used as arguments to the checkCombinations method. This method 
        checks if there is a winning combination of discs along the specified directions.
        */
        // Check for winning combinations in vertical, horizontal, and diagonal directions
        boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
                || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);

        return isEnded;
    }

    /*
	This method dynamically creates the structural grid of the game 
    board by adding circles at each cell position and subtracting them 
    from the base rectangle to create holes.
    */
    private Shape createGameStructuralGrid() {

    	//base shape of the board 
    	//Initializes a base rectangular shape representing the entire game board. 
    	//This rectangle is slightly larger than the actual board to account for 
    	//the spaces between circles.
        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * diameterOfCircle, (ROWS + 1) * diameterOfCircle);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle circle = new Circle();
                
                //really bringing out those geometry formulas with this one
                // Set up properties of the circle that was just created
                circle.setRadius(diameterOfCircle / 2);
                circle.setCenterX(diameterOfCircle / 2);
                circle.setCenterY(diameterOfCircle / 2);
                
                //look pretty
                circle.setSmooth(true);
                
                // Position the circle within the grid
                circle.setTranslateX(col * (diameterOfCircle + 5) + diameterOfCircle / 4);
                circle.setTranslateY(row * (diameterOfCircle + 5) + diameterOfCircle / 4);

                //inverts the selection to create "game spots" where pieces should go
                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        }

        // Fill all spaces with black except the circles, creating a board with holes
        rectangleWithHoles.setFill(Color.BLACK);

        return rectangleWithHoles;
    }
    
    /* This method is responsible for generating a list of rectangles 
      representing clickable columns in the Connect Four game. 
      Each rectangle corresponds to a column on the game board.
      
      This method dynamically generates a list of clickable columns, each represented 
      by a rectangle. These rectangles provide visual feedback on mouse interactions 
      and trigger the insertion of discs when clicked.
    */
    private List<Rectangle> createClickableColumns() {

        List<Rectangle> rectangleList = new ArrayList<>();

        for (int col = 0; col < COLUMNS; col++) {
        	
        	//Initializes a rectangle representing a clickable column. 
        	//The height of the rectangle is slightly larger than the game 
        	//board to allow for visual feedback.
            Rectangle rectangle = new Rectangle(diameterOfCircle, (ROWS + 1) * diameterOfCircle);
            // Set initial fill to transparent
            rectangle.setFill(Color.TRANSPARENT);
            // Set the position
            rectangle.setTranslateX(col * (diameterOfCircle + 5) + diameterOfCircle / 4);

            // Mouse event handlers for visual feedback
            //Provides visual feedback when the mouse enters and exits the rectangle. 
            //The fill changes to a semi-transparent color and reverts to transparent.
            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

            //Handles the mouse click event on the rectangle. If it's allowed to insert a disc, 
            //the insertDisc method is called with the corresponding column index.
            final int column = col;
            rectangle.setOnMouseClicked(event -> {
                if (isAllowedToInsert) {
                	// When disc is being dropped then no more disc will be allowed to beinserted
                	// Prevent inserting more discs during animation
                    isAllowedToInsert = false; 
                    
                    /*
                    new Disc(isPlayerOneTurn)
					It creates a new instance of the Disc class, representing a game disc.
					The constructor of the Disc class takes a boolean parameter (isPlayerOneMove), 
					indicating whether the disc belongs to Player One (true) or Player Two (false).
					isPlayerOneTurn is used to determine the current player's turn, 
					and it is passed as an argument to the constructor.
					insertDisc(..., column):
					
					The insertDisc method is called with the newly created disc 
					and the column index where the disc should be inserted.
					This method handles the animation and logic for dropping 
					the disc into the specified column.
                     */
                    
                    //Set The discColor
                    Color discColor;
                    if(isPlayerOneTurn) {
                    	discColor = discColor1;
                    }else {
                    	discColor = discColor2;
                    }
                    
                    try {
						insertDisc(new Disc(isPlayerOneTurn, discColor), column);
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            });
            
            //Adds the created rectangle to the list, which will be used 
            //to add rectangles to the game board.
            rectangleList.add(rectangle);
        }

        return rectangleList;
    }
    
    /* This method is responsible for resetting the Connect Four game to its initial state, 
     * allowing the players to start a new game.
     */
    public void resetGame() throws IOException {
    	//Method clears the game board, resets player-related flags, 
    	//updates the player label, and initializes the game area for a new Connect Four game.
    	
    	//reset the random seed for the Ai Player
    	//seed = new Random().nextInt();
    	
    	// clears the insertedDiscsPane to remove all the 
    	// previously inserted discs from the game board.
        insertedDiscsPane.getChildren().clear();    
        
        // Structurally, Make all elements of insertedDiscsArray[][] to null
        // iterate over the insertedDiscsArray and sets all elements to null. 
        // This array is used to keep track of the discs that have been inserted structurally.
        for (int row = 0; row < insertedDiscsArray.length; row++) { 
            for (int col = 0; col < insertedDiscsArray[row].length; col++) {
                insertedDiscsArray[row][col] = null;
            }
        }
        
        // Reset player flag to true
        // indicating that it's Player One's turn to start the new game.
        isPlayerOneTurn = true; 
        
        // set the text of playerNameLabel to the name of the first player 
        // (FirstPlayer), indicating that Player One starts the game.
        playerNameLabel.setText(FirstPlayer);

        //It initializes the game area again by calling the createPlayground method.
        //This method sets up the game board and clickable columns (mentioned prior).
        createPlayground();
        
        
    }
   
    /*This method is responsible for inserting a disc into the game board when a player makes a move.
      ensures that a disc is inserted into the game board, updates the game state, 
      and provides a smooth animation for the disc drop.
      
       this code ensures that after the dropping animation of a disc is complete, the 
       game state and player turn are updated accordingly, and the graphical representation of 
       the current player's status is updated on the label.
      */
    private void insertDisc(Disc disc, int column) throws IOException {
        
    	//starts from the bottom row and iterates upward until it finds 
    	//the first empty slot in the specified column where the disc can be inserted.
    	int row = getLastEmptyRow(column);
        
        // If the column is full, we cannot insert any more discs
        // (the previous loop was not interrupted)
        
        //Extreme Edgecase; GLITCHES BEWARE
        if (row < 0) {    
        	return;
        }
        
        // An empty position is found, the method updates the shadow data structure  
        // to mark that position as occupied.
        
        // Shadow Data Structure Update: Mark the position as occupied
        insertedDiscsArray[row][column] = disc;   
        
        // Graphics Get Updated: Add the disc to the game board
        insertedDiscsPane.getChildren().add(disc);

        //The disc is added to the graphical representation (insertedDiscsPane) of the game board
        
        // Set the initial position of the disc
        disc.setTranslateX(column * (diameterOfCircle + 5) + diameterOfCircle / 4);

        //Local Integer Copy of Col/Row because we can always use a reference (pointers who??)
        Integer currentRow = row;
        Integer currentCol = column;
        
        /*	An event handler is set to be called when the transition finishes. 
        	It allows the next player to insert a disc (isAllowedToInsert is set to true).
        	Checks if the current move resulted in the end of the game and calls gameOver() if necessary.
        	Switches the player's turn.
        	Updates the graphical representation of the current player's status (playerNameLabel).
        */
        
        // Create a TranslateTransition to animate the disc dropping into place
        // This code creates a TranslateTransition object, which is a JavaFX animation 
        // class used to smoothly move (translate) a JavaFX node (in this case, a disc) 
        // from one position to another over a specified duration. 
        
        //TranslateTransition will animate the disc node from its initial position to 
        //the specified Y-coordinate over a duration of 0.5 seconds. This creates a smooth 
        //dropping animation for the disc when it is inserted into the game board. The destination 
        //Y-coordinate is calculated based on the row where the disc is being inserted, ensuring that 
        //the disc drops to the correct position.
        
        //TranslateTransition is instantiated to create an animation that translates (moves) the specified 'disc' node.
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
        																	//Duration.seconds(0.5) sets the duration 
    																		//of the animation to 0.5 seconds. This means 
        																	//that the disc will move to its final position 
        																	//in half a second.
        
        //translateTransition.setToY(...) sets the destination Y-coordinate of the translation. 
        // The row * (diameterOfCircle + 5) + diameterOfCircle / 4 expression calculates the 
        // y-coordinate based on the row where the disc is being inserted.
        // row * (diameterOfCircle + 5) calculates the initial Y-coordinate based on the row.
        // + diameterOfCircle / 4 adjusts the final Y-coordinate to position the disc in the center of the row.
        translateTransition.setToY(row * (diameterOfCircle + 5) + diameterOfCircle / 4);
        
        // Set an event handler to be called when the transition finishes
        // translateTransition.setOnFinished(...) sets an event handler to be 
        // executed when the TranslateTransition finishes. The event handler is 
        // specified as a lambda expression (event -> { ... })
        translateTransition.setOnFinished(event -> {
        	// Finally, when disc is dropped allow next player to insert disc.
        	// After the disc is dropped, it signals that the next player is allowed to insert a disc.
            isAllowedToInsert = true;
            
            //Checks if the current move has resulted in the end of the game. 
            //If so, it triggers the game-over logic.
            //Calls the gameEnded method with the current row and column as arguments. 
            //This method is responsible for determining whether the game has reached 
            //a conclusion based on the latest move.

            //switch player turn
            //works the same for Ai
            isPlayerOneTurn = !isPlayerOneTurn;
            
            // Updates the graphical representation of the current player's status on the label (playerNameLabel).
            playerNameLabel.setText(isPlayerOneTurn? FirstPlayer: SecondPlayer);
        
            // Use logMove1 or logMove2 based on the current player's turn
			if (isPlayerOneTurn) {
				String logLine = FirstPlayer.toString() + " Moved Placed Piece at [" + currentRow.toString() + ", " + currentCol.toString()+ "]";
			    logMove1(logLine);
			} else {
				String logLine2 = SecondPlayer.toString() + " Moved Placed Piece at [" + currentRow.toString() + ", " + currentCol.toString()+ "]";
			    logMove1(logLine2);
			}
        });
        
        //initiates the execution of the translation animation. This starts the smooth movement of the disc to its final position.
        translateTransition.play();
        
        if (gameEnded(currentRow, currentCol)) {
            gameOver();
        }
    }
    
    // Constructor
    public Controller() {
        // Initialize logWriterAlpha here
        try {
            
            logWriterAlpha = new BufferedWriter(new FileWriter(LOG_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }

        // Other initialization code...
    }
    
    /*Defines a method named checkCombinations that takes a list of Point2D objects 
      as an argument and returns a boolean value*/
    private boolean checkCombinations(List<Point2D> points) {
    	
    	//Initializes a counter variable chain to keep track of the number of 
    	//consecutive discs in the specified direction.
        int chain = 0;
        
        //Iterates over the list of points provided, which represent the positions to check for consecutive discs.
        for (Point2D point: points) {

        	//Retrieves the indicies from the current point in the loop
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();

            Disc disc = getDiscIfPresent(rowIndexForArray, columnIndexForArray);
            
            // if the last inserted Disc belongs to the current player
            if (disc != null && disc.isPlayerOneMove == isPlayerOneTurn) {  

                chain++;
                //Four consecutive chains are found
                if (chain == 4) {
                    return true;
                }
            } else {
            	// Reset the chain if a different player's disc is encountered
            	// GLITCH PATCHED
                chain = 0;
            }
        }
        // No four consecutive discs found
        return false;
    }

    //This method assists with validation 
    private Disc getDiscIfPresent(int row, int column) {    
    	
    	// check statement for if row or column index is invalid
        if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0) {
        	//Debugging State
            //System.out.println("Invalid Disc Location - Try Again");
        	return null;
        }
        
        // Debugging State
        System.out.println("Checking Disc at Row: " + row + ", Column: " + column);

        
        //returns disc at specified part of shadow data structure
        return insertedDiscsArray[row][column];
    }

    //handles gameOver...Obviously
    //This method Handles checking the game state
    private void gameOver() {
    	
    	
    	//ternary operator (short-hand if-else): if isPlayerOneTurn True, assigns FirstPlayer to winner, else SecondPlayer is the winner
    	//similar to scheme: (if isPlayerOneTurn FirstPlayer SecondPlayer)
    	String winner = isPlayerOneTurn ? FirstPlayer : SecondPlayer;
        
        //Logging the Player
        //TODO add file IO for logging purposes
        System.out.println("Winner is: " + winner);
        //file io code

        //UI choice to give visual feedback to users
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The Winner is " + winner);
        alert.setContentText("Want to play again?");

        //Code for interactive window prompt
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No, Exit");
        
        //Updating the instance of the alert class
        //sets button types made declared in the previous 2 line
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Helps us to resolve IllegalStateException.
        Platform.runLater(() -> { 
        	
        	//creates a button that can be selected by user. the alert is then process when needed
            Optional<ButtonType> buttonClicked = alert.showAndWait();
            
            //ensuring the button clicked Exists and condition that checks if 'yes' was selected
            if (buttonClicked.isPresent() && buttonClicked.get() == yesButton ) {
                // user chose YES so RESET the game
                try {
					resetGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
            } else {
            	logMove1("The Winner is " + winner);
                // user chose NO 
                Platform.exit();
                //so Exit the Game
                System.exit(0);
            }
        });
    }
    
    /*this method creates a disc which, graphically will look like a circle
      has all properties of a Circle but extends with one more method */
    private static class Disc extends Circle {
    	//boolean of current player turn status
        private final boolean isPlayerOneMove;
    	@SuppressWarnings("unused")
		private final Color discColor; 
        
    	// Update Disc class constructor
        private Disc(boolean isPlayerOneMove, Color discColor) {
            this.isPlayerOneMove = isPlayerOneMove;
            this.discColor = discColor;
            setRadius(diameterOfCircle / 2);
            setFill(discColor);
            setCenterX(diameterOfCircle / 2);
            setCenterY(diameterOfCircle / 2);
        }

    }
    
    /*initialize() method is used in JavaFx framework to initialize the controller
      after the root element is processed by the FXML Loader
      Called automatically by the FXML Loader after controller is set 
    
      this method will perform actions:
      Set up UI elements, variables, event handlers
      Overeide is the hook for setting up tasks*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            logWriterAlpha = new BufferedWriter(new FileWriter(LOG_FILE_PATH));
            logWriterAlpha.write("Connect Four Log" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logMove1(String move) {
        try {
            if (logWriterAlpha != null) {
                // Write the move data to the file
                logWriterAlpha.write(move);
                logWriterAlpha.newLine(); // Add a new line for each move

                // Flush the writer to ensure the data is written immediately
                logWriterAlpha.flush();
            } else {
                // Log a message or throw an exception to indicate that the writer is not properly initialized
                System.err.println("Error: logWriterAlpha is not initialized");
                // Alternatively, throw a new exception if appropriate for your application
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }
    }

	public Object getMoveLog() {
		return null;
	}
    
}