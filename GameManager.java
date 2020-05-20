/**
 * This file is provided for you to test out your Streamline program
 * while you work on PA 3. It is not required for submission.
 */
import java.io.*;

/**
 * A manager class that runs the Streamline game. It parses command line
 * arguments, initializes the game accordingly then play it level by level
 * @author Junshen Kevin Chen
 */
public class GameManager{

    static final String USAGE = 
        "Usage: \n" + 
        "> java GameManager             - to start a game with default size 6*5 and random obstacles \n" + 
        "> java GameManager <filename>  - to start a game by reading game state from the specified file \n" +
        "> java GameManager <directory> - to start a game by reading all game states from files in the specified directory \n" +
        "                                 and playing them in order\n";

    /**
     * Main method. Parses command line arg, load games, run games.
     * @param args an array of command line args in Strings
     */
    public static void main(String[] args) {

        if (args.length != 0 && args.length != 1) {
            System.out.print(USAGE);
            return;
        }

        if (args.length == 0) {
            System.out.println("Starting a default-sized random game..");
            Streamline game = new Streamline();
            game.play();
            return;
        }

        // at this point args.length == 1
        
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.printf("File %s does not exist. Exiting..", args[0]);
            return;
        }

        // if is not a directory, read from the file and start the game
        if (!file.isDirectory()) {
            System.out.printf("Loading single game from file %s..\n", args[0]);
            Streamline game = new Streamline(args[0]);
            game.play();
            return;
        }

        // file is a directory, walk the directory and load from all files
        File[] subfiles = file.listFiles();
        for (int i=0; i<subfiles.length; i++) {
            File subfile = subfiles[i];
            
            // in case there's a directory in there, skip
            if (subfile.isDirectory()) continue;

            // assume all files are properly formatted games, 
            // create a new game for each file, play it, move on to the next one
            System.out.printf("Loading game %d/%d from file %s..\n",
                i+1, subfiles.length, subfile.toString());
            Streamline game = new Streamline(subfile.toString());
            game.play();            
        }       
        
    }
}
