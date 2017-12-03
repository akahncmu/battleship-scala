package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class BoardController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  object StrikeResult extends Enumeration {
    val Hit, Miss, Already_Taken, Sunk, Win = Value
  }

  //each ship needs only to keep track of how big it is and how many times it has been hit
  //since shotsTaken already guarantees that no coordinate can be hit twice
  class Ship(size: Int) {
    var health = size

    def strike(): Boolean = {
      health -= 1
      return (health == 0)
    }
  }

  //currently only using this for the pretty printer
  final val DIMENSION: Int = 10
  var numShips: Int = 0

  //Set of coordinates that have already been shot at
  var shotsTaken: Set[(Int,Int)] = Set[(Int,Int)]()

  //map of coordinates to ships
  var shipLocations: Map[(Int,Int), Ship] = Map()

  //TODO: currently no input validation
  def addShip(size: Int, startX: Int, startY: Int, vertical: Boolean) = Action {
    numShips = numShips + 1
    val ship = new Ship(size)
    for(i <- 0 until size)
      if(vertical)
        shipLocations += ((startX, startY + i) -> ship)
      else
        shipLocations += ((startX + i, startY) -> ship)
    Ok(s"There are now $numShips ships")
  }

  def clearBoard() = Action {
    numShips = 0
    shotsTaken = Set[(Int,Int)]()
    shipLocations = Map[(Int,Int), Ship]()
    Ok(Json.toJson("Board cleared"))
  }


  override def toString(): String = {
    var result = ""
    for(i <- 0 until this.DIMENSION) {
      for(j <- 0 until this.DIMENSION) {
        if(shotsTaken contains (j,i))
          result += "X"
        else if (shipLocations contains (j,i))
          result += "S"
        else
          result += "O"
        if(j < (DIMENSION - 1)) result += " "
      }
      if(i < (DIMENSION - 1)) result += "\n"
    }
    return result
  }

  //
  def printBoard() = Action {Ok(this.toString())}

  def attack(x: Int, y: Int) = Action {
    if (shotsTaken contains (x, y)) {
      Ok(Json.toJson(StrikeResult.Already_Taken))
    }
    else {
      shotsTaken += ((x, y))
      shipLocations.get(x,y) match {
         case Some(ship) => {
           if(ship.strike()) { //sunk that ship
             numShips -= 1
             if (numShips == 0)
               Ok(Json.toJson(StrikeResult.Win))
             else
               Ok(Json.toJson(StrikeResult.Sunk))
           }
           else
             Ok(Json.toJson(StrikeResult.Hit))
         }
         case None => Ok(Json.toJson(StrikeResult.Miss))
      }
    }
  }
}
