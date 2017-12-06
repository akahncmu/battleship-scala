package controllers

sealed trait StrikeResult[Ship]
object StrikeResult {
  case class Hit[Ship](ship:Ship) extends StrikeResult[Ship]
  case class Already_Taken[Ship]() extends StrikeResult[Ship]
  case class Win[Ship](ship:Ship) extends StrikeResult[Ship]
  case class Sunk[Ship](ship:Ship) extends StrikeResult[Ship]
  case class Miss[Ship]() extends StrikeResult[Ship]
}

object Board {
  def addShip[Position,Ship](ship:Ship, positions:Set[Position], shipLocations: Map[Position,Ship]): Option[Map[Position,Ship]] = {
    if(shipLocations.valuesIterator.contains(ship)) None
    else if (positions.exists(shipLocations.contains)) None
    else {
      Some(shipLocations ++ positions.map(p => (p, ship)))
    }
  }
}

case class Board[Position, Ship](shotsTaken: Set[Position], shipLocations: Map[Position, Ship]) {

  def hasWon(): Boolean = {
    shipLocations.keySet.forall(shotsTaken.contains)
  }

  def isSunk(s:Ship): Boolean ={
    shipLocations.filter{ case (_,ship) => (ship == s)}.keySet.forall(shotsTaken.contains)
  }

  def attack(p: Position): (Board[Position, Ship], StrikeResult[Ship]) =  {
    if (shotsTaken contains p) {
      (this,StrikeResult.Already_Taken())
    }
    else {
      (Board(shotsTaken + p, shipLocations),
      shipLocations.get(p) match {
        case Some(ship) => {
            if (hasWon) (StrikeResult.Win(ship))
            else if (isSunk(ship))StrikeResult.Sunk(ship)
            else StrikeResult.Hit(ship)
        }
        case None => StrikeResult.Miss()
      }
      )}
  }
}
