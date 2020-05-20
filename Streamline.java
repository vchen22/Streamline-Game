/**
 * Author: Vicki Chen
 * CSE8B Login: cs8bwahm
 * Date: 2/12/18
 * File: Streamline.java
 * Source of Help: PA3 writeup, Piazza, CSE8B tutors
 *
 * This file contains the class Streamline
 * It creates a game of Streamlines and has methods that the user can command
 * to do
 * */

import java.util.*;
import java.io.*;

/**
 * This class contains methods that can create a new game from scratch or 
 * from a file and save game states and it has methods that allow the player
 * to interact with the game. It has the instance variables, currentState 
 * that is the current state of game the player is on and previousStates
 * which is a list of past states the player has been through
 * */
public class Streamline {

    final static int DEFAULT_HEIGHT = 6;
    final static int DEFAULT_WIDTH = 5;

    final static int ADD_OBSTACLE = 3;

    final static String OUTFILE_NAME = "saved_streamline_game";

    final static String PUT_INPUT = "> ";
    final static String MOVE_UP = "w";
    final static String MOVE_LEFT = "a";
    final static String MOVE_DOWN = "s";
    final static String MOVE_RIGHT = "d";
    final static String UNDO_MOVE = "u";
    final static String SAVE_FILE = "o";
    final static String QUIT_GAME = "q";

    GameState currentState;
    List<GameState> previousStates;

    public Streamline() {
        // TODO
        this.currentState = new GameState(DEFAULT_HEIGHT, DEFAULT_WIDTH, 
                DEFAULT_HEIGHT-1, 0, 0, DEFAULT_WIDTH-1);
        this.currentState.addRandomObstacles(ADD_OBSTACLE);
        this.previousStates = new ArrayList<GameState>();
    }

    public Streamline(String filename) {
        try {
            //creating game from a file
            loadFromFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void loadFromFile(String filename) throws IOException {
        // TODO
        
         this.previousStates = new ArrayList<GameState>();
         
         //extracting position data and intializing it
         Scanner sc = new Scanner(new File(filename));
         int height = Integer.parseInt(sc.next());
         int width = Integer.parseInt(sc.next());
         int pRow = Integer.parseInt(sc.next());
         int pCol = Integer.parseInt(sc.next());
         int gRow = Integer.parseInt(sc.next());
         int gCol = Integer.parseInt(sc.next());

         GameState gameBoard = new GameState(height, width, pRow, pCol, gRow,
                 gCol);

         sc.nextLine();
         //iterating to extract the content of the board from file
         for (int row = 0; row < gameBoard.board.length; row++)
         {
              String line = sc.nextLine(); 
              for (int col = 0; col < gameBoard.board[0].length; col++)
              {

                  gameBoard.board[row][col] = line.charAt(col);
              }
         }  

         this.currentState = gameBoard;
        
        //check if game is already over
        if (currentState.playerRow == currentState.goalRow && 
                currentState.playerCol == currentState.goalCol)
        {
            currentState.levelPassed = true;
        }         
                           
    }

    void recordAndMove(Direction direction) {
        // TODO

        GameState copy = new GameState(currentState);

        if (direction == null)
        {
            return;
        }      

        currentState.move(direction);
        
        //compare current to previous states
        if (!(currentState.equals(copy)))
        {
            previousStates.add(copy);
        }

    } 

    void undo() {
        // TODO
        
        if (previousStates.size() == 0)
        {
            return;
        }
        //take out from the last element of arraylist
        else
        {
            currentState = previousStates.get(previousStates.size()-1);
            previousStates.remove(previousStates.size()-1);
        }
            
    }


    void play() {
        // TODO
        Scanner sc = new Scanner(System.in);
        while (currentState.levelPassed != true)
        {
            System.out.println(currentState.toString());
            System.out.print(PUT_INPUT);

            String input = sc.nextLine();
            
            //call methods depending on user input
            if (input.equals(MOVE_UP))
            {
                recordAndMove(Direction.UP);
            }
            else if (input.equals(MOVE_LEFT))
            {
                recordAndMove(Direction.LEFT);
            }
            else if (input.equals(MOVE_DOWN))
            {
                recordAndMove(Direction.DOWN);
            }
            else if (input.equals(MOVE_RIGHT))
            {
                recordAndMove(Direction.RIGHT);
            }
            else if (input.equals(UNDO_MOVE))
            {
                undo();
            }
            else if (input.equals(SAVE_FILE))
            {
                saveToFile();
            }
            else if (input.equals(QUIT_GAME))
            {
                return;
            }
            else
            {
                System.out.println("input w, s, a, d, o, u, or q");
                continue;                
            }
            
            //game stops when level passed
            if (currentState.levelPassed == true)
            {
                System.out.println(currentState.toString());
                System.out.println("Level Passed!");
                return;
            }
        }

    }


    void saveToFile() {
        try {
            // TODO: use OUTFILE_NAME as the filename and save
            PrintWriter file = new PrintWriter(new File(OUTFILE_NAME));
            //creating first 4 lines with positions
            file.print(currentState.board.length);
            file.print(currentState.SPACE_CHAR);
            file.println(currentState.board[0].length);
            file.print(currentState.playerRow);
            file.print(currentState.SPACE_CHAR);
            file.println(currentState.playerCol);
            file.print(currentState.goalRow);
            file.print(currentState.SPACE_CHAR);
            file.println(currentState.goalCol);
            
            //putting all board content into the last few lines of file
            for (int index = 0; index < currentState.board.length; index++)
            {
                file.println(currentState.board[index]);
            } 
            
            file.close();
            System.out.println("Saved current state to: " 
                    + OUTFILE_NAME);
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

