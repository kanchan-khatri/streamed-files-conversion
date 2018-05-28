package op.assessment.ebay

import java.nio.file.Paths
import cats.effect.{IO, Sync}
import fs2.{io, text, _}
import op.assessment.ebay.CreditLimit._
import scala.language.higherKinds

object Main extends App {

  def rowType(path: String): RowType = CsvRow

  val input = "Workbook2.csv"
  val csvParse: RowParse = rowParse(rowType(input))

  def converter[F[_]](implicit F: Sync[F]): F[Unit] = {

    val header: Stream[F, String] =
      Stream.emit(htmlHeader).intersperse("\n")

    val table: Stream[F, String] =
      io.file.readAll[F](Paths.get(input), chunkSize = 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .map(csvParse andThen htmlTableRow)
        .intersperse("\n")

    val footer: Stream[F, String] =
      Stream.emit(htmlFooter).intersperse("\n")

    (header ++ table ++ footer)
      .through(text.utf8Encode)
      .through(io.file.writeAll(
        Paths.get("report.html")
      ))
      .compile.drain
  }

  converter[IO].unsafeRunCancelable {
    case Left(th) => th.printStackTrace()
    case _ => ()
  }
}
