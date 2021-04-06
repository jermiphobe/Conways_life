# Conways_life

[Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life)

This started out as a simple board to simulate the Game of Life.  It was a 2D array of 'town' objects that I would update.  Once I got the simulation up and running, I started making more features.  They started out as keyboard commands with an optional button menu, but I eventually replaced the keyboard command with a button menu that's always there.

### Controls
You can click on squares to make a dead sdquare alive or an alive square dead.  
You can also click and drag to draw a pattern on the board.    
You can draw on the board anytime, even if the simulation is in progress.  

Play/Pause - This will start and stop the current simulation.  
Increment - If paused, this will move the simulation forward one generation.  
New color - this will change the towns to a random color.  
Color -> Yellow - this will change the towns back to the default color.  
Save to File - You can save the current board state to a file.  
Load from File - You can load a saved board to simulate (e.g. a glider gun preset).  
Clear Board - This will pause the simulation and clear the board.  
New Board - This will generate a random board.  
Reset Board - This will revert the board to the state it was when you either first loaded a file or created a random board.  
Select Simulation Speed - This is how you change the speed of the simulation.  It will take effect immediately.  
