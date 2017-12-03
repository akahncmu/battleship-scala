# Battleship in scala
“Battleship is known worldwide as a pencil and paper game which dates from World War I. It was
published by various companies as a pad-and-pencil game in the 1930s, and was released as a plastic
board game by Milton Bradley in 1967. The game has spawned electronic versions, video games, smart
device apps and a film.”
In our simplified version of battleship, a board can be thought of as a grid of potentially attacked
positions along with one or more ships each of which occupy one or more positions.

## Running

Run this using [sbt](http://www.scala-sbt.org/).  If you downloaded this project from http://www.playframework.com/download then you'll find a prepackaged version of sbt in the project directory:

```
sbt run
```

at that point the webapp is running at http://localhost:9000 

## Testing

Run the tests using [sbt](http://www.scala-sbt.org/).

```
sbt test
```

## Controllers

- BoardController.scala:

  the game board

## Routes - all routes relative to http://localhost:9000, and all coordinates use inverse cartesian (0,0 is top left corner)

- GET /printBoard:
  prints the board

- GET /attack/:x/:y
  attack at coord (x,y)

- GET /addShip/:size/:startX/:startY/:vertical
  add a ship of the given size, at the given position.  vertical ships go down from there, horizontal go right.

- GET /clearBoard
  clear the board for a new game
