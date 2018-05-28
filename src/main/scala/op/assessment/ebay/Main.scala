package op.assessment.ebay

import cats.effect.IO
import scala.language.higherKinds

object Main extends App with TablesConversion with HtmlTable {

  val input = List("Workbook2.csv")
  val tablePath = "report.html"

  converter[IO](input).unsafeRunCancelable {
    case Left(th) => th.printStackTrace()
    case _ => ()
  }
}
