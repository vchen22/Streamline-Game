/** 
 * Author: Vicki Chen
 * CSE8B Login: cs8bwamh
 * Date: 2/11/19
 * File: GameState.java
 * Sources of Help: PA3 write up, Piazza, CSE8B tutors
 *
 * This file contains the class Gamestate.
 * It creates the backend functionality of the game Streamline
 * */

import java.util.*;

/** This class have methods that creates/set up the map of the game,
 * add obstacles to the board, control the movement of the player
 * Instance variables: board is the map of the game, playerRow and playerCol
 * is the position of the player in the game, goalRow and goalCol is the
 * position of the goal in the game, and levelPassed determines if the player
 * succeeded at getting to the goal
 * */
public class GameState {

    // Used to populate char[][] board below and to display the
    // current state of play.
    final static char TRAIL_CHAR = '.';
    final static char OBSTACLE_CHAR = 'X';
    final static char SPACE_CHAR = ' ';
    final static char CURRENT_CHAR = 'O';
    final static char GOAL_CHAR = '@';
    final static char NEWLINE_CHAR = '\n';
    
    final static int DOUBLE_ROW = 2;
    final static int ADD_THREE = 3;
    final static char TOP_BOT_BORDER = '-';
    final static char SIDE_BORDER = '|';
    final static int ROTATE_BACK = 4;
    
    // This represents a 2D map of the board
    char[][] board;

    // Location of the player
    int playerRow;
    int playerCol;

    // Location of the goal
    int goalRow;
    int goalCol;

    // true means the player completed this level
    boolean levelPassed;

    /**
     * Intializes the board with given dimensions and empty spaces, 
     * playerRow, playerCol, goalRow, goalCol, and state of game
     * @param height Height of board
     * @param width Width of board
     * @param playerRow Row in board where player is
     * @param playerCol Column in board where player is
     * @param goalRow Row in board where goal is
     * @param goalCol Column in board where goal is
     * @return        none.
     * */
    public GameState(int height, int width, int playerRow, int playerCol, 
                     int goalRow, int goalCol) {
        // TODO
        
        this.board = new char[height][width];
        //iterate through whole array to fill each row & col with SPACE_CHAR
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                board[row][col] = SPACE_CHAR;
            }
        }

        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.levelPassed = false;
                
    }

    /**
     * Create a GameState object that has the same instance field values
     * @param other Object getting deep-copied
     * @return      none.
     */
    public GameState(GameState other) {
        // TODO
        
        this.board = new char[other.board.length][other.board[0].length];
        //iterate through other's board and copy content to instance vars
        for (int row = 0; row < other.board.length; row++)
        {
            for (int col = 0; col < other.board[0].length; col++)
            {
                this.board[row][col] = other.board[row][col];
            }
        }

        this.playerRow = other.playerRow;
        this.playerCol = other.playerCol;
        this.goalRow = other.goalRow;
        this.goalCol = other.goalCol;
        this.levelPassed = other.levelPassed;
    }

    /**
     * Add a random number of obstacles into board based on
     * number of spaces left on board
     * @param count Number of empty spaces in board
     * @return      none.
     **/
    void addRandomObstacles(int count) {
        // TODO
        
        int emptySpaces = 0;
        Random rand1 = new Random(); 
        int randRow;
        Random rand2 = new Random();
        int randCol;
        
        if (count <= 0)
        {
            return;
        }
        else
        {
            //iterate through board to count the number of empty spaces
            for (int row = 0; row < board.length; row++)
            {
                for (int col = 0; col < board[0].length; col++)
                {
                    if (board[row][col] == SPACE_CHAR &&
                            !(row == playerRow && col == playerCol) &&
                            !(row == goalRow && col == goalCol))
                    {
                        emptySpaces++;
                    }
                }
            }
            
            if (count > emptySpaces)
            {
                return;
            }
            else
            {
                //generate random position to place obstacle as long it's not
                //the player's or goal's position
                while (count > 0)
                {
                    randRow = rand1.nextInt(board.length);
                    randCol = rand2.nextInt(board[0].length);

                    if (board[randRow][randCol] == SPACE_CHAR &&
                            !(randRow == playerRow && randCol == playerCol) &&
                            !(randRow == goalRow && randCol == goalCol))
                    {
                        board[randRow][randCol] = OBSTACLE_CHAR;
                        count--;
                    }
                }
            }     
        }

    }

    /**
     * Rotates the board and its instance variables clockwise
     * */
    void rotateClockwise() {
        // TODO

        int tempPRow = this.playerRow;
        int tempPCol = this.playerCol;
        int tempGRow = this.goalRow;
        int tempGCol = this.goalCol;

        char[][] rotatedArr = new char[board[0].length][board.length];

        //iterate through whole board and store it's clockedwised position
        //to another array
        for (int row = 0; row < rotatedArr.length; row++)
        {
            for (int col = 0; col < rotatedArr[0].length; col++)
            {
                rotatedArr[row][col] = board[board.length-col-1][row];
            }
        }
        
        //swapping rows and cols to its clockwised position
        this.playerCol = board.length-tempPRow-1;
        this.playerRow = tempPCol;

        this.goalCol = board.length-tempGRow-1;
        this.goalRow = tempGCol;

        this.board = rotatedArr;
        
    }
    
    /**
     * Move the player to the right reached to an obstacle, edge of the board,
     * and trail of dots
     * */
    void moveRight() {
        // TODO
        //conditions that will allow to move right
        while (playerCol+1 < board[0].length &&
                board[playerRow][playerCol+1] != OBSTACLE_CHAR && 
                board[playerRow][playerCol+1] != TRAIL_CHAR)
        {
            playerCol++;
            
            //player at goal position
            if (playerRow == goalRow && playerCol == goalCol)
            {
                board[playerRow][playerCol-1] = TRAIL_CHAR;
                levelPassed = true;
                return;
            }
            else
            {
                board[playerRow][playerCol-1] = TRAIL_CHAR;
            }
        }
        
    }

    /**
     * Move the player in the given direction
     * @param direction Given direction to move
     * @return          none.
     * */
    void move(Direction direction) {
        // TODO
        int count = direction.getRotationCount();

        //rotate until player can move right
        for (int index = 0; index < count; index++)
        {
            rotateClockwise();
        }

        moveRight();

        //number of times to rotate back to original set up
        for (int index = 0; index < ROTATE_BACK - count; index++)
        {
            rotateClockwise();
        }

    }


    @Override
    /**
     * Compare two GameState objects if their instance field is exactly
     * the same
     * @param other The other GameState object being compared to
     * @return boolean true or false
     * */
    public boolean equals(Object other) {

        // TODO: check for any conditions that should return false

        // We have exhausted all possibility of mismatch, they're identical
                
        if (other == null)
        {
            return false;
        }

        if (!(other instanceof GameState))
        {
            return false;
        }
        else
        {
            //check for nulls and if board lengths are the same
            GameState obj = (GameState) other;
            if ((board == null && obj.board != null) || 
                    (obj.board == null && board != null) || 
                    obj.board.length != this.board.length || 
                    obj.board[0].length != this.board[0].length)
            {
                return false;
            }
            //if instance field the same, compare the contents of the boards
            else if ((obj.playerRow == this.playerRow) && (obj.playerCol == 
                    this.playerCol) && (obj.goalRow == this.goalRow) && 
                    (obj.goalCol == this.goalCol) && 
                     (obj.levelPassed == this.levelPassed))
            {
                for (int row = 0; row < obj.board.length; row++)
                {
                    for (int col = 0; col < obj.board[0].length; col++)
                    {
                        if (obj.board[row][col] != this.board[row][col])
                        {
                            return false;
                        }
                    }
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        
    }


    @Override
    /**
     * Format the board and its instance field
     * */
    public String toString() {
        // TODO
        
        StringBuilder gameBoard = new StringBuilder();

        //create top border of game
        for (int index = 0; index < DOUBLE_ROW*board[0].length+ADD_THREE; 
                index++)
        {
            gameBoard.append(TOP_BOT_BORDER);
        }

        //next line
        gameBoard.append(NEWLINE_CHAR);
        
        //put side borders and all contents of the board and space chars
        //between side borders and contents
        for (int row = 0; row < board.length; row++)
        {
            gameBoard.append(SIDE_BORDER);
            gameBoard.append(SPACE_CHAR);

            for (int col = 0 ; col < board[0].length; col++)
            {
                if (row == playerRow && col == playerCol)
                {
                    gameBoard.append(CURRENT_CHAR);
                }
                else if (row == goalRow && col == goalCol)
                {
                    gameBoard.append(GOAL_CHAR);
                }
                else
                {
                    gameBoard.append(board[row][col]);
                }

                gameBoard.append(SPACE_CHAR);    
            }
            
            gameBoard.append(SIDE_BORDER);
            gameBoard.append(NEWLINE_CHAR);
        }

        //create bottom border
        for (int index = 0; index < DOUBLE_ROW*board[0].length+ADD_THREE; 
                index++)
        {
            gameBoard.append(TOP_BOT_BORDER);
        }
       
        gameBoard.append(NEWLINE_CHAR);
        
        return gameBoard.toString();
    }
                    
}
