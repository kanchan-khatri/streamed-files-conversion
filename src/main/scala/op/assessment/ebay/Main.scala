package op.assessment.ebay

import java.nio.file.Paths
import cats.effect.{IO, Sync}
import fs2.{io, text, _}
import op.assessment.ebay.CreditLimit._
import scala.language.higherKinds

object Main extends App {

  def rowType(path: String): RowType = CsvRow

  val input = "Workbook2.csv"

  def converter[F[_]](sources :Seq[String])(implicit F: Sync[F]): F[Unit] = {

    val header: Stream[F, String] =
      Stream.emit(htmlHeader).intersperse("\n")

    def table(source: String): Stream[F, String] = {
      val parse = rowParse(rowType(source))
      io.file.readAll[F](Paths.get(source), chunkSize = 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .tail
        .flatMap(s => Stream(parse(s)).collect {
          case Some(row) => row
        })
        .map(htmlTableRow)
        .intersperse("\n")
    }

    val tables = sources.foldLeft[Stream[F, String]](Stream.empty) {
        (acc, source) => acc ++ table(source)
      }

    val footer: Stream[F, String] =
      Stream.emit(htmlFooter).intersperse("\n")

    (header ++ tables ++ footer)
      .through(text.utf8Encode)
      .through(io.file.writeAll(
          Paths.get("report.html")
        ))
      .compile.drain
  }

  converter[IO](List(input)).unsafeRunCancelable {
    case Left(th) => th.printStackTrace()
    case _ => ()
  }
}
