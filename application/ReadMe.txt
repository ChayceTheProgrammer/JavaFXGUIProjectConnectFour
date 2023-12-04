# Connect Four Game

This is a JavaFX implementation of the classic Connect Four game. 
The game features a graphical user interface, AI player, and log file functionality.
Class CSCE314

Tasks To Complete:
-Sophisticated AI (Simple AI is Complete but works quasi-randomly)
-Update GUI in SceneBuilder
-Save/Load Feature

Note From Chayce The Programmer:
I imagine if you are reading this, you may be in need of assistance,
Luckily For you, This project is overly documented to an unhealthy degree
but take a look at the controller And you will see 2 weeks worth of staring 
at a computer screen unfold so thank you for listening to my ted talk and 
viewing my delusional code :)

## Getting Started
To run the Connect Four game, you will need Java installed on your machine. Follow the steps below:
You can customize certain aspects of the game by modifying constants in the Controller.java file:

Game Rules:
The game follows the traditional rules of Connect Four. Players take turns dropping discs into a grid, 
aiming to connect four of their discs vertically, horizontally, or diagonally.

1. Clone the repository to your local machine.
2. Compile and run the `Main.java` file.

Lightweight Customization:
COLUMNS and ROWS: Number of columns and rows on the game board.
diameterOfCircle: Diameter of the discs.
discColor1 and discColor2: Colors of the player discs.
Player names: Modify FirstPlayer, SecondPlayer, and AiPlayerName as desired.

LogFile:
The game log is stored in the connet4gamelog.txt file. Each move is recorded 
with details about the player, column, and row.
-File is accessible after game is terminated
-Future updates will update after every turn

Save/Load:
-Feature is still under Development
-Goal: Have any logfile be able to remake the board of a previous game

Contributors:
Chayce Leonard

