/**
 * Author: Vicki Chen
 * CSE8B Login: cs8bwamh
 * Date: 3/5/19
 * File: Player.java
 * Source of Help: PA6 write up, Piazza
 *
 * This file contains the Player class that extends RoundedSquare
 * Creates player objects with RoundedSquare attributes
 * */

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

/**
 * This class creates a player object with its rightful color and has method
 * that makes a border around the rectangle that represents player
 * */
public class Player extends RoundedSquare {
    final static double STROKE_FRACTION = 0.1;

    /**
     * Intializes a player object by filling in color of roundedSquare and
     * and its stroke
     * @param none
     * @return Player object created
     * */
    public Player() {
        //TODO: set a fill color, a stroke color, and set the stroke type to
        //      centered

        //set color of player object with a stroke border
        setFill(Color.LIGHTBLUE);
        setStroke(Color.BLUE);
        //center the stroke to be in middle of object
        setStrokeType(StrokeType.CENTERED);
    }
    
    /**
     * Create a border on the roundedSquare
     * @param size Given size wanted for border
     * @return void
     * */
    @Override
    public void setSize(double size) {
        //TODO: 1. update the stroke width based on the size and 
        //         STROKE_FRACTION
        //      2. call super setSize(), bearing in mind that the size
        //         parameter we are passed here includes stroke but the
        //         superclass's setSize() does not include the stroke
       
        //calculate stroke size and set it as the stroke width
        double strokeWidth = size*STROKE_FRACTION;
        setStrokeWidth(strokeWidth);
        //to make border of rounded rectangle
        super.setSize(size-strokeWidth);
    }
}
