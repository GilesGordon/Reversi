# Overview

This program implements a hexagonal interpretation of
Reversi, a two-player strategy board game.

The game is implemented using a Model-View-Controller design
pattern. Currently, the implementation has very little extensibility in terms of board design,
as the coordinate system was specialized for a hexagonal tile system
and grid shape. However, the implementation can be molded to include more players or more
restrictive game rules such as a minimum requirement of tiles to flip or different game-over
conditions.

# Getting Started

In order to start a game, two Player objects and a board size are needed.
Player objects may be initialized like so:

> Player p1 = new HumanPlayer();
>
> Player p2 = new RobotPlayer();

Then, a game object is needed:

> ReversiModel game = new BasicReversi();

Lastly, in order to play the game using its methods, the game must first
be started with a given board size greater than 0, otherwise an error will be thrown.

> int boardSize = 2;
>
> game.startGame(boardSize, p1, p2)

Users may now call game commands such as:
> game.pass();
>
> game.selectTile(new HexaCoordinate(0, 0));
>
> game.moveOnQAxis(-1);
>
> game.moveOnRAxis(1);
>
> game.placeTile();

### Coordinates

The game's coordinate system works on a Q and R axis. The Q axis stems from the right
(positive) to the left (negative). The R axis stems from the bottom right corner (positive)
to the top left corner (negative). The origin of the board (0,0) is at the center of
the two axis, and the size of the board is the number of tiles between the center and the edge of
the board, not including the center. The game's origin is centered at the board to allow for
easy scaling of the size of the board in all directions. Hence, the size of the board is measures
by how many tiles from the center the board goes in each axis direction until the edge is reached.

The game additionally uses a helper method, convertTileCoordsToLocation(HexaCoordinate input) which
takes in a board coordinate (where the origin is (0,0)) and converts it to a coordinate that may
be used on the board's object representation as a list of lists. This new coordinate now acts as
the index in which specific Tiles from the board may be accessed.

This is done through observing the relationship between the board's coordinates a list's
coordinates mapped onto a hexagonal board of the same shape and size. In order to get the new
Q coordinate (which makes up the outer list), all that needs to be done is add the size of the
board. This is because the Q coordinates begin at the lower left corner which is one board size
away from the center. As for the R coordinate, this is more tricky. If the Tile being converted is
below the Q axis, in order to convert it, the new Q coordinate representing the index of the list
should be added to the R coordinate. However, if it is at or beyond the Q axis, then the size
of the board should be added to the R coordinate.

### Placing Tiles

Users may control the game by using methods focused around selecting a tile such as:

> selectTile(HexaCoordinate);
>
> moveOnQAxis(int);
>
> moveOnRAxis(int);

in order to move the cursor to select a new tile. Users may then attempt to place their
respective color's tile on the currently selected tile using:

> placeTile();

This move may only be allowed under reversi game rules.

# Components

Though not fully implemented yet, the program consists of a model, controller, and view packages.
The controller communicates between the model (which contains the core gameplay mechanics) and the
view (which contains the graphics players see visually) while taking in input from the players.
It is responsible for displaying to players the results of their actions (in-game moves) and
upholding the integrity of the game and its rules.

### Model

The model consists of a board represented with a list of lists of TileStates. These tile states
can be occupied by a black or white tile or remain empty, where players may place their
respective tiles onto.
The model may be found under /src/model/ which contains two separate interfaces for the model.
The first interface, ReversiModelReadOnly, is an observation-geared interface that allows users to
inquire about the state of the game (such as the size of the board, whether the game has been won,
the state of a given tile, and so on). This interface is implemented under
/src/model/ObservableBasicReversi/ and contains the basic constructors and observations for the
game.
The second interface, ReversiModel, extends the first and allows for
commands that mutate the state of the game such as placing a tile or passing a turn.
This interface is implemented under /src/model/BasicReversi/ and extends
ObservableBasicReversi, now allowing for both mutation and observation.

The purpose of this split is to prevent mutability of the data of the game while rendering a
rather test-oriented textual view of the game, still giving it access to interpret the state
of the game. Thus, the textual view receives a read-only object of the game.

### View

The view provides a render of the current state of the game. 

A text-based view can be found under /src/view/ReversiView/. 
The view is implemented as an ASCII textual render of the state
of the board of the game where white tiles are represented with "O", black tiles are represented
with "X", empty tiles are "_", and the selected tile is surrounded by parenthesis.
This implementation may be found under /srs/view/ReversiTextualView/.

A GUI-based view can be found under /src/view/ReversiViewGUI/.
This view promises basic functions such as refreshing the view and setting the view visible.
Additionally, it provides a way to display to the player that an attempted move is invalid. 
Lastly, it takes in a listener which in this case is designated to be the controller. 
The GUI view is implemented under /src/view/ReversiGraphicView/ and extends JFrame, making use
of Java's Swing library. 
The implementation uses an extension of JPanel found under /src/view/JReversiPanel/ which
takes in a ReversiModelReadOnly to render the view.
This graphical view renders a grid of hexagons and displays tokens for each player as a black
or white circle.
The selected tile is blue.


### Controller

The controller mediates actions between the view of the game and the model of the game, allowing
players to take their turns sequentially. The controller interface can be found under 
/src/reversi/controller/ReversiController/ and the implementation can be found  under
/src/reversi/controller/ReversiGraphicController.
The interface extends two other interfaces: ModelStatusListener and PlayerActionListener, thus
rendering itself a listener to the Model status updates (ModelStatusFeatures)
and to Players actions (PlayerActionFeatures).

For more information on how the controller works, see Functionality: How does the controller work?

### Player

The players are represented as a Player objects and are planned to be able to execute game
moves. The Player interface is not yet implemented and can be found under /src/player/.
Additionally, a Player may be a HumanPlayer (found under /src/player/HumanPlayer/) controlled by
human input or a RobotPlayer (found under /src/player/RobotPlayer/) that will automatically
execute its move. Most methods within the player class are found within the Abstract Player,
this was because all methods but takeTurn were the same within each implementation of Player. 
Abstract player takes in a model, a gameState that represents when the player can move, and a 
strategy. Eventually the Human player will have a strategy that interfaces with the controller,
so that's why the strategy is not speisific to the robotPlayer.

> ableToMove() throws an Illegal State Exception if the player is unable to move
> setComponents() sets the model and state of the game in runtime
> setStrategy() sets the strategy of the game in runtime, this was kept seperate from 
setComponents to allow for the setting of a strategy even if the player is not currently in a game.
> takeTurn() runs the strategy for the player. 

takeTurn is also not implemented in the HumanPlayer class since we have no controller. In the
 RobotPlayer it tries to run the strategy on every availible move, or pass if there is no move. 

# Functionality: How does the Model work?

The model cycles between different game states of Reversi. The game could not be started,
be a player's turn, or be over (either one player wins or there's a tie).

### Not Started

When the game has not been started yet, no methods (either mutation or observation based)
are available other than inquiring the state of the game.

### Player's Turn

Once the game is started, and before it is over, it cycles between the players' turns.
The Black pieces go first, then alternate between Black and White pieces.
During a player's turn, they may choose to pass their turn or place their color's piece on
the board. Pieces may only be placed on an empty tile where they would surround the opposite
player's linear spread of pieces on either side in the direction of the line.

This is calculated by taking the currently selected piece, and when placeTile() is called,
go in a line in each of the six directions of the selected piece.

For each direction, check for the next tile's state in that direction, noting the current
player's tile color (and thus the opposite tile color). This is implemented in

> getValidRow(int rMod, int qMod)

which takes in the directions in the r and q axis to go.

In order to check the next tile's state, getValidRow calls the recursive method,

> validRowHelper(HexaCoordinate currentTile, int rMod, int qMod,
> TileState oppositeColor, TileState thisColor,
> ArrayList<HexaCoordinate> recursiveList)

which takes in the current tile being stemmed from, the direction, the current player's
tile color and opposite tile color, and a list of tiles to be flipped thus far.

The method calculates the coordinate of the next tile in the given direction.
If it is empty or reaches an edge, no tiles are to be flipped.
If it is the opposite color's tile, add that tile to a temporary list of tiles to be flipped
and keep going in that direction, thus recursing. If another tile is eventually reached that
is the same color as the player's tiles, then every tile in between that was added to the
temporary list is to be flipped, returning back to placeTile() which calls flip()
on each tile accumulated.

Lastly, the number of passes back-to-back is reset back to 0 (since the turn was not passed) and
game state is flipped, calling flipState(). If the game is over, the winner is calculated.
Otherwise, the game's state is set to the next player's turn.

### Game Over

The game ends when every player passes their turn consecutively. Since there are two players,
this means the game is over when pass() is called twice in a row.
In a situation where there are no more empty board tiles, players have no choice but to pass
their turn.

The game calculates the winner through getWinner(), returning the state of the game as
either Black winning, White winning, or the game being a tie. A count begins for the number
of black tiles and number of white tiles placed on the board. Empty tiles are not counted.
If the number of tiles are the same, the game is a tie, otherwise, the color with more tiles
wins.

# Functionality: How does the view work?

The view should accept a ReversiModel and some form of readable object. For example
ReversiTextualView accepts a ReversiModel and an Appendable. There is only one method that
should be called from ReversiView and that is render.

> render() returns nothing but it modifies the readable object in some way so that a view
> can be made. ReversiTextualView does so by appending the ToString to an Appendable object.

As for the GUI view, a new interface, /src/view/ReversiViewGUI/, can be found, which contains
simple methods such as adding listeners, refreshing the view, and so on.
The implementation may be found under /src/view/ReversiGraphicView/ which also extends JFrame,
making use of Java's swing library. 
The constructor instantiates an extension of JPanel found under /src/view/JReversiPanel.
This class has two functions: Render the view and accept user interactions with the window.

In order to render the view, the class overrides the paintComponent method. 
The method uses a helper, drawHexagonAtHexacoord, that takes in a Graphics2D object 
to paint the hexagon onto and a Hexacoordinate location to show where to paint the Hexagon.
Before the hexagons are painted, the Graphics2D object is transformed to be more developer
friendly, making the given Hexacoordinate be the new (0,0) so that the hexagon can be painted
at the "center" of the view. The hexagon is created using Point2D.Double to allow for a 
more precise hexagon. Each vertex of the hexagon is calculated using points such as (0, x) or
(-x * sqrt(3)/2, -x/2). The hexagon is additionally filled and generated using a thin line.

In order to draw each hexagon in the model, each hexacoordinate is iterated through. For each
HexaCoordinate, we calculate where the center of that hexagon belongs on the XY view. 
The X coordinate is calculated by taking the width of a hexagon, multiplying it by the Q value
(how many cells left or right the coordinate is), then adding an offset of a multiple of
half the width of a hexagon depending on the R value (how many rows up or down it is). 
This offset is needed to allow a row of hexagons to be half a hexagon off from the row above
and below it. 
The Y coordinate is much simpler, calculated by taking one and a half height of a hexagon times
the r value. Additionally, the X, X offset, and Y values incorporate a static GAP_SPACE to 
allow for a separation of each hexagon. 

For the second function of handling user interactions, the class contains two subclasses for
mouse inputs and user inputs: MouseEventsListener (which extends MouseInputAdapter) and
KeyboardListener (which extends KeyAdapter). 

MouseEventsListener overrides the mouseReleased method so that when a mouse is released over
a tile, that tile is selected and its HexaCoordinate is documented. In order to find the 
HexaCoordinate of where the mouse clicked, each tile in the model is checked to see if the
mouse's click coordinates overlap with it. 
To check this, a helper method coordInHexagon is used which takes in the coordinate of the
mouse click and a given HexaCoordinate to check. First, a rectangle is drawn around the 
Hexacoordinate to eliminate an obvious outside mouse click. 
If the click is inside the rectangle, then it is checked which part of the hexagon it is in.
The hexagon is split down the center and horizontally from the top left vertex to the top right
vertex and the bottom left vertex to the bottom right vertex. Checking the two center 
sections is simple, but to check for each corner section, the slope of the line from the corner
vertex to the center vertex is calculated. Then, the mouse coordinate is checked to see if it is
under the line for the upper sections or over the line for the lower sections.

The view keeps track of the selected tile and updates the tile to the new selected coordinate.
If the user clicks outside the board or on the selected tile, then it is deselected. 

KeyboardListener overrides the keyPressed method so that when a key is pressed, it is checked
to be either "P" or "Enter" using the key's key code. 
If the key pressed is "P", then this signals a player's desire to pass their turn.
If the key pressed is "Enter", then this signals a player's desire to place a tile where there
is a current selection.


# Functionality: How do the strategies work?

All of the strategy objects can be found in src.player.aiStrategies. Each strategy extends the 
ReversiStrategies interface. 
This interface only contains one method which returns the best move according to the strategy.

> chooseMove(ReversiModelReadOnly model, ArrayList<HexaCoordinate> possibleCoords) 



## The different strategies 

> AvoidNextToCorners is a strategy class whose chooseMove will remove any coordinate that is 
adjacent to a corner from the supplied list of possible moves before returning the rest.
> 
> MaxCaptureStrat is a strategy class whose chooseMove will return any moves that would return the
highest amount of points from the supplied list of possible moves. 
> 
> TakeCornersStrat is a strategy class whose chooseMove will return any moves that are 
corners from the supplied list of possible moves.
> 
> MinimaxStrategy is a strategy class whose chooseMove will return any moves where the next 
player's turn's most viable strategy is minimized, thus making an assumption on the next 
player's strategy (which is passed into the constructor of this strategy).
> 
> TryTwo is a strategy class that tries two possible options, running them both if possible, 
and only running one if it is not. 

Note that these strategies return a list of equally viable HexaCoordinates where the player
could place their move.
The lists are ordered in such a way that the earlier in the list a coordinate is, the more 
"top left" on the board it is. 
By "top left", we mean that it is the highest row (most negative R value), and amongst all the
coordinates in that row, ti is the leftmost (most negative Q value).
Thus, the first element in the list is the top leftmost coordinate and the last element is the
bottom rightmost element. 

There will also be some kind of strategy that interfaces with the controller so that the player 
can make inputs. This strategy will be by default applied to the player.

# Functionality: How does the controller work?

As mentioned above, the controller interface extends two other interfaces: 
ModelStatusListener and PlayerActionListener.

ModelStatusFeatures include notifying its listeners that it is a given player's turn and that
the game is over. Whenever a turn is taken in the model, the model determines who's turn it is
next. If the game is not over, it will then tell its listeners whose turn it is using
notifyTurnChange(Player p). If the game is over, it will tell its listeners that the game is
over using notifyGameOver().
Additionally, it contains a method to allow the enlistment of ModelStatusListeners.
ModelStatusListeners includes mirroring functions that allow the
subject (ModelStatusFeatures) to notify it when a turn has been taken through
notifyPlayerToTakeTurn(Player p) and to notify when the game is over through gameOver().

PlayerActionFeatures include notifying its listeners that the Player it represents would like to
pass their turn or place their tile at a given HexaCoordinate.
Additionally, PlayerActionFeatures has a method to allow the enlistment of PlayerActionListeners.
PlayerActionListeners have similar mirroring methods that either pass the turn or place the
tile at the given HexaCoordinate by mutating the model.

The ModelStatusFeatures interface is extended by the ReversiModel interface and thus
is implemented in the BasicReversi class. Therefore, the controller listens to the model,
allowing itself to be notified whenever the turn changes or whenever the game is over.

The PlayerActionFeatures interface is extended by the Player interface (and thus implemented in
all the Player classes) as well as implemented by the JReversiPanel class. This allows
two sources of Player turns as a solution to different types of Players. A robot player is able
to instantaneously play a move whenever it is told to whereas a human player takes time to
process where it would like to move. This is reflected in their implementations where a RobotPlayer
object instantly casts its turn when it is notified whereas a HumanPlayer does nothing when
it is notified. Instead, human players play their turn through the java swing's view by
clicking on the view and pressing specific keys (p to pass the turn and enter to place their tile).
Thus, the controller can be notified of a turn occurring by both RobotPlayers directly and
HumanPlayers indirectly through the view. Thus, both Players and the JReversiPanel emit
player actions.



# Reversi - Main
The main method supports playing a game of reversi and allows the user to pass Command-Line
arguments into the program to customize their experience. The arguments must be formatted as follows.
argument for PlayerCreator.createFromArgs, argument for PlayerCreator.createFromArgs, boardSize. 
(For valid arguments for PlayerCreator.createFromArgs see the section below labeled PlayerCreator, and
boardSize must be a positive int just as it must be in model.) Any number of these arguments can be left out,
but the program will always interpret the first and second as an argument for PlayerCreator.createFromArgs, and
the third as boardsize. If no argument for a player is passed they will default to a human player with an
empty name, and the boardSize will default to 5. 

## PlayerCreator 

PlayerCreator is a class that facilitates the creation of players from a string, so that main
can start the game more esially based on command line arguments. It has one method.

> createFromArgs(String args) is a method that takes in a string and returns a player based on that
> input. To return a robot player, use one of the following strings

> -"easy" a robot with the MaxCaptureStrat

> -"medium" a robot who will do MaxCaptureStrat, but prioritize avoiding spaces next to corners
>   as seen in AvoidCorners.

> -"hard" a robot who will do the same things as the medium strategy, but prioritize taking corners
>   as seen in takeCornersStrat.

> -"expert" a robot who will use MinMaxStrat to predict the optimal move based on the hard strategy, 
>   and fall back on the hard strategy to break any ties. 


# Changes For part 2

Several methods were added to the model that previously did not exist due to a more limited scope.

> copyGame() returns a ReversiModel with the exact same saved data as the current model. Editing
> the copy will not edit the current model.
>
> getBoard() returns a copy of the board coordinates, again preventing mutation.
> 
> isMoveValid(HexaCoordinate coords) checks if placing a move at the given coordinate based
> on the current players turn is valid, returning a boolean.
> 
> getScoreForPlayer(Player player) returns current the score for the given player as an integer.
> 
> anyLegalMoves() returns whether the current player can make any moves, otherwise having to pass.
> 
> getNumberOfTilesToFlipAtCoord(HexaCoordinate coords) returns how many tiles would be flipped if
> the current player placed a tile at the given coordinate.
>
> getCorners() returns an ArrayList of HexaCoordinates that represent the corners of any given board. 
>
> getAllPossibleMoves() returns an ArrayList of all possible moves that the current player could make. 

Additionally, the notion of a selected tile has been removed from the model and transferred
solely into the view. Several methods in the model were updated to take in a HexaCoordinate
argument (such as placeTile) to allow for a tile to be placed at a desires location.

Several methods were also added to the player interface, more info is in the player portion of this 
readme.


# Changes for Part 3

There were a few minor changes from our Part 2 submission. 

First, the controller package was
slightly adjusted to match the assignment requirements rather than what we were anticipating
would be needed. At first, it was expected to have a textual controller, so changes included
renaming the interface and implementation and README information.

Second, the lines to print out whenever a key was pressed or coordinate was clicked was moved
from the JReversiPanel class to the Listener implementation (controller) rather than remaining
in the view, based off feedback from the grader. 

Third, a method in JReversiPanel was shortened by use of a helper due to the immense size of the
method, also based off feedback from the grader. 

Fourth, the MinimaxStrategy was slightly adjusted due to a bug that would cause it to instantly
win the game when taking its turn. 

Fifth, the infrastructural methods in JReversiPanel such as displaying an invalid move or 
displaying the game is over were properly implemented using message dialogue and labels. 