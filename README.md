/**
 * Author: Vicki Chen
 * CSE8B Login: cs8bwamh
 * Date: 3/5/19
 * File: README.md
 * Source of Help: PA6 write up, Piazza
 *
 * This file contains all the answers to PA6 README questions
 * */

## Extra Credit
None

## Program Description
This program creates the visual graphics for the game Streamline. First it 
will create a window to put all the objects into it and calculate all the 
sizing of the objects so it is proportionate to the screen. When playing the
game, based on the positions of all the objects, it will take in the updated 
positions and create new objects in that new position and clear out the 
previous states. The new positions are determined based on the keys 
that the user presses to move the player. For example, if the player is on the
bottom left corner and it moves to the top left corner, it will fill in the
colors that the player moved through, delete the old player at the bottom
left corner and create a new player on the top left.

## Short Response
Unix/Linux Questions:
1. mkdir -p fooBar/dirDir
2. javac *.java means it will compile all java files. * indicates all
java files
3. ls -R

JavaFX Questions:
1. Instead of creating a nested class, we will have GuiStreamline
implement EventHandler<KeyEvent>. Then in Guistreamline, we will
create a overriden method handle() in the class body.
2. The purpose of the Group class on my JavaFx GUI is to add the
objects to the stage, which you can then add that group to a scene
that will be added to the stage to visualize the graphics of the
objects.

