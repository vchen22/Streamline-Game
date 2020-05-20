/**
 * Author: Vicki Chen
 * CSE8B Login: cs8bwamh
 * Date: 3/5/19
 * File: GuiStreamline.java
 * Source of Help: PA6 write up, Piazza
 *
 * This file contains the subclass GuiStreamline that extends Application.
 * It adds graphics based on the events of the game Streamline.
 * */

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.animation.*;
import javafx.animation.PathTransition.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.util.Duration;

/**
 * This class includes methods that access the game's demensions and calculate
 * the graphic size based on it, add color and display graphics at certain
 * positions, reset and reload graphics, and take in user input that will
 * control the game.
 * */
public class GuiStreamline extends Application {
    static final double SCENE_WIDTH = 500;
    static final double SCENE_HEIGHT = 600;
    static final String TITLE = "CSE 8b Streamline GUI";
    static final String USAGE = 
        "Usage: \n" + 
        "> java GuiStreamline               - to start a game with default" +
            " size 6*5 and random obstacles\n" + 
        "> java GuiStreamline <filename>    - to start a game by reading g" +
            "ame state from the specified file\n" +
        "> java GuiStreamline <directory>   - to start a game by reading a" +
            "ll game states from files in\n" +
        "                                     the specified directory and " +
            "playing them in order\n";

    static final Color TRAIL_COLOR = Color.PALEVIOLETRED;
    static final Color GOAL_COLOR = Color.RED;
    static final Color OBSTACLE_COLOR = Color.DIMGRAY;

    // Trail radius will be set to this fraction of the size of a board square.
    static final double TRAIL_RADIUS_FRACTION = 0.1;

    // Squares will be resized to this fraction of the size of a board square.
    static final double SQUARE_FRACTION = 0.8;

    private static final double GRADIENT_FROM = 1.0;
    private static final double GRADIENT_TO = 0.5;
    
    Scene mainScene;
    Group levelGroup;                   // For obstacles and trails
    Group rootGroup;                    // Parent group for everything else
    Player playerRect;                  // GUI representation of the player
    RoundedSquare goalRect;             // GUI representation of the goal

    Shape[][] grid;                     // Same dimensions as the game board
    
    Streamline game;                    // The current level
    ArrayList<Streamline> nextGames;    // Future levels

    MyKeyHandler myKeyHandler;          // for keyboard input

    /**
     * Get the width of the board of current level
     * @param none
     * @return width of current level's board
     * */
    public int getBoardWidth() {
        /*TODO*/ 

        return game.currentState.board[0].length;
    }

    /**
     * Get height of the board of the current level
     * @param none
     * @return height of current level's board
     * */
    public int getBoardHeight() {
    	/*TODO*/

        return game.currentState.board.length;
    }
    
    /**
     * Calculate the smallest dimension of a single square of the board based 
     * on the current scene size. 
     * @param none
     * @return the smaller side of calculation for square's size
     * */
    public double getSquareSize() {
        /* For example, given a scene size of 1000 by 600 and a board size
           of 5 by 6, we have room for each square to be 200x100. Since we
           want squares not rectangles, return the minimum which is 100 
           in this example. */
        
        /*TODO*/
        double squareWidth = mainScene.getWidth()/getBoardWidth();
        double squareHeight = mainScene.getHeight()/getBoardHeight();

        //compare which is smaller
        if (squareWidth < squareHeight)
        {
            return squareWidth;
        }
        else
        {
            //if width and height same, still return height
            return squareHeight;
        }
    }
    
    // Destroy and recreate grid and all trail and obstacle shapes.
    // Assumes the dimensions of the board may have changed.
    /**
     * Empty out grid's contents and recreate all trail and obstacle shapes
     * and dimensions.
     * @param none
     * @return void
     * */
    public void resetGrid() {

        /*TODO*/

        // Hints: Empty out levelGroup and recreate grid.
        // Also makes sure grid is the right size, in case the size of the
        // board changed.

        //empty out contents of grid
        levelGroup.getChildren().clear();

        grid = new Shape[getBoardHeight()][getBoardWidth()];

        //iterate through whole board to see where to place shapes at what
        //position and add shapes to group
        for (int row = 0; row < getBoardHeight(); row++)
        {
            for (int col = 0; col < getBoardWidth(); col++)
            {
                //calculate the shape sizes to fit scene
                double circleSize = getSquareSize()*TRAIL_RADIUS_FRACTION;
                double rSquareSize = getSquareSize()*SQUARE_FRACTION;
                //calculate center points of shapes
                double[] centerPoints = boardIdxToScenePos(col, row);
                                
                if (game.currentState.board[row][col] == 
                        GameState.OBSTACLE_CHAR)
                {
                    //obstacle = rounded rectangle
                    //fill in color now because no helper method to fill it
                    RoundedSquare obstacle = new RoundedSquare(centerPoints[0],
                            centerPoints[1], rSquareSize);
                    obstacle.setFill(OBSTACLE_COLOR);
                    grid[row][col] = obstacle;
                    levelGroup.getChildren().add(obstacle);                  
                }
                else if (game.currentState.board[row][col] == 
                        GameState.TRAIL_CHAR)
                {
                    //trail = circle
                    Circle trail = new Circle(centerPoints[0], centerPoints[1],
                            circleSize);
                    grid[row][col] = trail;
                    levelGroup.getChildren().add(trail);
                }
                else 
                {
                    //emptyspace = circle
                    Circle emptySpace = new Circle(centerPoints[0],
                            centerPoints[1], circleSize);
                    grid[row][col] = emptySpace;
                    levelGroup.getChildren().add(emptySpace);
                }
            }
        }

        //call helper method to fill in color of the circles
        updateTrailColors();            
    }

    /**
     * Helper method that set colors of trails and empty spaces (circles)
     * @param none
     * @return void
     * */
    public void updateTrailColors() {
    	/*TODO*/
        
        //iterate through whole board for trails and empty spaces
        for (int row = 0; row < getBoardHeight(); row++)
        {
            for (int col = 0; col < getBoardWidth(); col++)
            {
                //check if instance of circle because shouldn't fill in
                //rounded rectangles
                if (grid[row][col] instanceof Circle)
                {
                    if (game.currentState.board[row][col] == 
                            GameState.TRAIL_CHAR)
                    {
                        grid[row][col].setFill(TRAIL_COLOR);
                    }

                    else
                    {
                        //make empty spaces not visible
                        grid[row][col].setFill(Color.TRANSPARENT);
                    }
                }
            }   
        }
    }
    
    /** 
     * Coverts the given board column and row into scene coordinates.
     * Gives the center of the corresponding tile.
     * 
     * @param boardCol a board column to be converted to a scene x
     * @param boardRow a board row to be converted to a scene y
     * @return scene coordinates as length 2 array where index 0 is x
     */
    static final double MIDDLE_OFFSET = 0.5;
    public double[] boardIdxToScenePos (int boardCol, int boardRow) {
        double sceneX = ((boardCol + MIDDLE_OFFSET) * 
            (mainScene.getWidth() - 1)) / getBoardWidth();
        double sceneY = ((boardRow + MIDDLE_OFFSET) * 
            (mainScene.getHeight() - 1)) / getBoardHeight();
        return new double[]{sceneX, sceneY};
    }

    /**
     * Updates colors of trails after every movement and display
     * player's new position
     * @param fromCol column player was previously on
     * @param fromRow row player was previously on
     * @param toCol column player goes to
     * @param toRow row player goes to
     * @return void
     * */
    public void onPlayerMoved(int fromCol, int fromRow, int toCol, int toRow, 
        boolean isUndo)
    {
        // If the position is the same, just return
        if (fromCol == toCol && fromRow == toRow) {
            return;
        }

        /*TODO*/
        //fill in trails on positions player moved through
        updateTrailColors();

        double squareSize = getSquareSize() * SQUARE_FRACTION;
        
        double[] playerPos = boardIdxToScenePos(toCol, toRow);            

        //recreate rectangle representing player at its moved position
        playerRect.setSize(squareSize);
        playerRect.setCenterX(playerPos[0]);
        playerRect.setCenterY(playerPos[1]);

        //if player on goal, call helper method to move on next level or
        //complete game
        if (game.currentState.levelPassed == true)
        {
            onLevelFinished();
        }
    }
    
    /**
     * Move player based on what key was pressed
     * @param keyCode Key pressed
     * @return void
     * */
    void handleKeyCode(KeyCode keyCode) {

        /*TODO*/
        int prevRow = game.currentState.playerRow;
        int prevCol = game.currentState.playerCol;

        switch (keyCode) {
        	/*TODO*/
            
            //call recordAndMove with direction according to what key was
            //pressed to move the player
            //Then call onPlayerMoved to fill in trails and set player's new
            //position
            case UP:
                game.recordAndMove(Direction.UP);
                onPlayerMoved(prevCol, prevRow, game.currentState.playerCol, 
                        game.currentState.playerRow, false);
                break;
            case DOWN:
                game.recordAndMove(Direction.DOWN);
                onPlayerMoved(prevCol, prevRow, game.currentState.playerCol,
                        game.currentState.playerRow, false);
                break;
            case RIGHT:
                game.recordAndMove(Direction.RIGHT);
                onPlayerMoved(prevCol, prevRow, game.currentState.playerCol, 
                        game.currentState.playerRow, false);
                break;
            case LEFT:
                game.recordAndMove(Direction.LEFT);
                onPlayerMoved(prevCol, prevRow, game.currentState.playerCol, 
                        game.currentState.playerRow, false);
                break;
            case U:        
                game.undo();
                onPlayerMoved(prevCol, prevRow, game.currentState.playerCol, 
                        game.currentState.playerRow, false);
                break;
            case O:
                //call helper method to save current state of game
                game.saveToFile();
                break;
            case Q:
                //quit window even if there are more levels
                System.exit(0);
                break;
                         
            //case if invalid key was pressed    
            default:
                System.out.println("Possible commands:\n w - up\n " + 
                    "a - left\n s - down\n d - right\n u - undo\n " + 
                    "q - quit level");
                break;

        }
        // Call onPlayerMoved() to update the GUI to reflect the player's 
        // movement (if any)
    }

    /*
     * Nested class to help determine what key was pressed and move according
     * to the key
     * */
    class MyKeyHandler implements EventHandler<KeyEvent> {
        /**
         * Extract key and call helper method handleKeyCode() to determine
         * how to move
         * @param e 
         * @return void
         * */
        @Override
        public void handle(KeyEvent e) {
            /*TODO*/
            KeyCode key = e.getCode();
            //helper method move player based on key
            handleKeyCode(key);
        }
    }

    /**
     * Method to load a new level and update UI
     * @param none
     * @return void
     * */
    public void onLevelLoaded() {
        resetGrid();

        double squareSize = getSquareSize() * SQUARE_FRACTION;

        // Update the player position
        double[] playerPos = boardIdxToScenePos(
            game.currentState.playerCol, game.currentState.playerRow
        );
        playerRect.setSize(squareSize);
        playerRect.setCenterX(playerPos[0]);
        playerRect.setCenterY(playerPos[1]);


        /*TODO*/
        //update goal position
        double[] goalPos = boardIdxToScenePos(game.currentState.goalCol, 
                game.currentState.goalRow);
        goalRect.setSize(squareSize);
        goalRect.setCenterX(goalPos[0]);
        goalRect.setCenterY(goalPos[1]);
    }

    static final double SCALE_TIME = 175;  // milliseconds for scale animation
    static final double FADE_TIME = 250;   // milliseconds for fade animation
    static final double DOUBLE_MULTIPLIER = 2;
    /**
     * Shows visual effects when level is finished and moving onto next
     * level or when game is quit
     * @param none
     * @return void
     * */
    public void onLevelFinished() {
        // Clone the goal rectangle and scale it up until it covers the screen

        // Clone the goal rectangle
        Rectangle animatedGoal = new Rectangle(
            goalRect.getX(),
            goalRect.getY(),
            goalRect.getWidth(),
            goalRect.getHeight()
        );
        animatedGoal.setFill(goalRect.getFill());

        // Add the clone to the scene
        List<Node> children = rootGroup.getChildren();
        children.add(children.indexOf(goalRect), animatedGoal);

        // Create the scale animation
        ScaleTransition st = new ScaleTransition(
            Duration.millis(SCALE_TIME), animatedGoal
        );
        st.setInterpolator(Interpolator.EASE_IN);
        
        // Scale enough to eventually cover the entire scene
        st.setByX(DOUBLE_MULTIPLIER * 
            mainScene.getWidth() / animatedGoal.getWidth());
        st.setByY(DOUBLE_MULTIPLIER * 
            mainScene.getHeight() / animatedGoal.getHeight());

        /*
         * This will be called after the scale animation finishes.
         * If there is no next level, quit. Otherwise switch to it and
         * fade out the animated cloned goal to reveal the new level.
         */
        st.setOnFinished(e1 -> {

            /* TODO: check if there is no next game and if so, quit */
                if (nextGames.size() == 0)
                {
                    System.exit(0);
                }

            /* TODO: update the instances variables game and nextGames 
                     to switch to the next level */
                game = nextGames.get(0);
                nextGames.remove(0);

            // Update UI to the next level, but it won't be visible yet
            // because it's covered by the animated cloned goal
            onLevelLoaded();

            /* TODO: use a FadeTransition on animatedGoal, with FADE_TIME as
                     the duration. Use setOnFinished() to schedule code to
                     run after this animation is finished. When the animation
                     finishes, remove animatedGoal from rootGroup. */
            FadeTransition fadeTrans = 
                new FadeTransition(Duration.millis(FADE_TIME), animatedGoal);
            //create a gradient after fade
            fadeTrans.setFromValue(GRADIENT_FROM);
            fadeTrans.setToValue(GRADIENT_TO);
            //play fade transition
            fadeTrans.play();
          
            fadeTrans.setOnFinished(e2 -> {
                    rootGroup.getChildren().remove(animatedGoal);
                    });               
        });
        
        // Start the scale animation
        st.play();
    }

    /** 
     * Performs file IO to populate game and nextGames using filenames from
     * command line arguments.
     * @param none
     * @param void
     */
    public void loadLevels() {
        game = null;
        nextGames = new ArrayList<Streamline>();
        
        List<String> args = getParameters().getRaw();
        if (args.size() == 0) {
            System.out.println("Starting a default-sized random game...");
            game = new Streamline();
            return;
        }

        // at this point args.length == 1
        
        File file = new File(args.get(0));
        if (!file.exists()) {
            System.out.printf("File %s does not exist. Exiting...", 
                args.get(0));
            return;
        }

        // if is not a directory, read from the file and start the game
        if (!file.isDirectory()) {
            System.out.printf("Loading single game from file %s...\n", 
                args.get(0));
            game = new Streamline(args.get(0));
            return;
        }

        // file is a directory, walk the directory and load from all files
        File[] subfiles = file.listFiles();
        Arrays.sort(subfiles);
        for (int i=0; i<subfiles.length; i++) {
            File subfile = subfiles[i];
            
            // in case there's a directory in there, skip
            if (subfile.isDirectory()) continue;

            // assume all files are properly formatted games, 
            // create a new game for each file, and add it to nextGames
            System.out.printf("Loading game %d/%d from file %s...\n",
                i+1, subfiles.length, subfile.toString());
            nextGames.add(new Streamline(subfile.toString()));
        }

        // Switch to the first level
        game = nextGames.get(0);
        nextGames.remove(0);
    }
    
    /**
     * The main entry point for all JavaFX Applications
     * Initializes instance variables, creates the scene, and sets up the UI
     * @param primaryStage Platform graphics are displayed
     * @return void
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Populate game and nextGames
        loadLevels();

        // Initialize the scene and our groups
        rootGroup = new Group();
        mainScene = new Scene(rootGroup, SCENE_WIDTH, SCENE_HEIGHT, 
            Color.GAINSBORO);
        levelGroup = new Group();
        rootGroup.getChildren().add(levelGroup);
        
        //TODO: initialize goalRect and playerRect, add them to rootGroup,
        //      call onLevelLoaded(), and set up keyboard input handling        

        goalRect = new RoundedSquare();
        goalRect.setFill(GOAL_COLOR);
        rootGroup.getChildren().add(goalRect);

        playerRect = new Player();
        rootGroup.getChildren().add(playerRect);

        //call to load given level
        onLevelLoaded();

        //set up key handling so user can put inputs
        myKeyHandler = new MyKeyHandler();
        mainScene.setOnKeyPressed(myKeyHandler);

        // Make the scene visible
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(mainScene);
        //starter code was false
        primaryStage.setResizable(true);
        primaryStage.show();
               
    }

    /** 
     * Execution begins here, but at this point we don't have a UI yet
     * The only thing to do is call launch() which will eventually result in
     * start() above being called.
     * @param args Arguments passed in through command line
     * @return void
     */
    public static void main(String[] args) {
        if (args.length != 0 && args.length != 1) {
            System.out.print(USAGE);
            return;
        }

        launch(args);
    }
}
