import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._

/**
 * Functional tests start a Play application internally, available
 * as `app`.
 */
class FunctionalSpec extends PlaySpec with GuiceOneAppPerSuite {

    "BoardController" should {

      "start with empty board" in {
        val board = route(app, FakeRequest(GET, "/printBoard")).get
        val emptyBoard: String =
          """O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O""".stripMargin

        status(board) mustBe Status.OK
        contentType(board) mustBe Some("text/plain")
        contentAsString(board) mustBe (emptyBoard)
      }

      "place two ships, and attack" in {
        val hit = "\"Hit\""
        val miss = "\"Miss\""
        val sunk = "\"Sunk\""
        val already_taken = "\"Already_Taken\""
        val win = "\"Win\""

        var ship = route(app, FakeRequest(GET, "/addShip/2/0/0/true")).get
        status(ship) mustBe Status.OK
        contentType(ship) mustBe Some("text/plain")
        contentAsString(ship) mustBe ("There are now 1 ships")

        ship = route(app, FakeRequest(GET, "/addShip/3/2/2/false")).get
        status(ship) mustBe Status.OK
        contentType(ship) mustBe Some("text/plain")
        contentAsString(ship) mustBe ("There are now 2 ships")

        var board = route(app, FakeRequest(GET, "/printBoard")).get
        var boardString: String =
          """S O O O O O O O O O
            |S O O O O O O O O O
            |O O S S S O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O
            |O O O O O O O O O O""".stripMargin

        status(board) mustBe Status.OK
        contentType(board) mustBe Some("text/plain")
        contentAsString(board) mustBe (boardString)

        var attack = route(app, FakeRequest(GET, "/attack/0/0")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (hit)

        attack = route(app, FakeRequest(GET, "/attack/0/0")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (already_taken)

        attack = route(app, FakeRequest(GET, "/attack/9/9")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (miss)

        attack = route(app, FakeRequest(GET, "/attack/9/9")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (already_taken)

        attack = route(app, FakeRequest(GET, "/attack/0/1")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (sunk)

        attack = route(app, FakeRequest(GET, "/attack/2/2")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (hit)

        attack = route(app, FakeRequest(GET, "/attack/4/2")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (hit)

        attack = route(app, FakeRequest(GET, "/attack/3/2")).get
        status(attack) mustBe Status.OK
        contentType(attack) mustBe Some("application/json")
        contentAsString(attack) mustBe (win)

        boardString =
        """X O O O O O O O O O
          |X O O O O O O O O O
          |O O X X X O O O O O
          |O O O O O O O O O O
          |O O O O O O O O O O
          |O O O O O O O O O O
          |O O O O O O O O O O
          |O O O O O O O O O O
          |O O O O O O O O O O
          |O O O O O O O O O X""".stripMargin

        board = route(app, FakeRequest(GET, "/printBoard")).get
        status(board) mustBe Status.OK
        contentType(board) mustBe Some("text/plain")
        contentAsString(board) mustBe (boardString)
      }
    }
}
