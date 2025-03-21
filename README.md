# Space Invaders: Java
This is my attempt on a space invaders game using Java GUI, specifically the swing package. 

<div align="center">
  <img src="res/sample.gif" alt="demo" width="300"/>
</div>

## Approach
Every game entity was treated as a different object (player, alien, laser, bomb, etc.). The concepts of inheritance was pretty useful for this matter, as these all inherit the properties of what was defined as an "Entity" class, which contained a 2D position and a speed value (How many pixels will each object move per iteration).

A frame creates the window for the game, and a separate panel handles everything else related to graphics. You could think of this panel as a canvas, where everything is drawn. The runnable interface is also implemented to create a thread that theoretically updates the display 60 times per second (60fps).

## How to play

- **Move:** left and right arrow keys.
- **Shoot:** space key.

## Docs
Refer to the [documentation](https://github.com/m4mbo/space-invaders/tree/main/docs) directory for information on class interaction and composition.

## Build and Run

``` 
./run.sh
```
