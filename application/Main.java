package application;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("connect4.fxml"));
        GridPane rootGridPane = loader.load();

        //instantiate the controller
        controller = loader.getController();
        
        //most coded function
        controller.createPlayground();

        //create menu on window
        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        //create base window
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        
        //add menuPane to window
        menuPane.getChildren().add(menuBar);

        //view of the connect four game
        Scene scene = new Scene(rootGridPane);

        //edit graphical properties
        primaryStage.setScene(scene);
        primaryStage.setTitle("CSCE 314 Connect Four By Chayce Leonard");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //graphically create the menu bar
    private MenuBar createMenu() {

        // File Menu
        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New game");
        newGame.setOnAction(event -> controller.resetGame());

        //explicitly reset game when pressed
        MenuItem resetGame = new MenuItem("Reset game");
        resetGame.setOnAction(event -> controller.resetGame());
        
        //TODO: Implement Move Log
        MenuItem getMoveLog = new MenuItem("Move Log");
        getMoveLog.setOnAction(event -> controller.getMoveLog());

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        
        MenuItem exitGame = new MenuItem("Exit game");
        exitGame.setOnAction(event -> exitGame());

        fileMenu.getItems().addAll(newGame, resetGame, separatorMenuItem, exitGame, getMoveLog);

        // Help Menu
        Menu helpMenu = new Menu("Help");

        MenuItem aboutGame = new MenuItem("About Connect4");
        aboutGame.setOnAction(event -> aboutConnect4());

        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("About Chayce Creates");
        aboutMe.setOnAction(event -> aboutMe());

        helpMenu.getItems().addAll(aboutGame, separator, aboutMe);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        return menuBar;
    }

    //Information Buttons When the user selects some part of the menu bar
    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Title: About The Developer - Chayce Creates' Connect 4");
        alert.setHeaderText("Header: aboutMe");
        alert.setContentText("Content: aboutMe");
        alert.show();
    }

    private void aboutConnect4() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Title: About Connect Four");
        alert.setHeaderText("Header: Sample Header Text - aboutConnect4");
        alert.setContentText("Content: Content Text - aboutConnect4");
        alert.show();
    }

    //method to end program
    private void exitGame() {
    	//stop the program
        Platform.exit();
        //0 exit was fine, not 0: not fine, some error occured
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
