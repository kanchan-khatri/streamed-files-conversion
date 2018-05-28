package op.assessment.ebay

import java.nio.file.Paths
import cats.effect.Sync
import fs2.{Stream, io, text}
import op.assessment.ebay.CreditLimit._
import scala.language.higherKinds

trait TablesConversion  extends Table {

  def tablePath: String

  def converter[F[_]](sources :Seq[String])(implicit F: Sync[F]): F[Unit] = {

    val header: Stream[F, String] =
      Stream.emit(tableHeader).intersperse("\n")

    def table(source: String): Stream[F, String] = {
      val parse = rowParse(RowType(source))
      io.file.readAll[F](Paths.get(source), chunkSize = 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .tail
        .flatMap(s => Stream(parse(s)).collect {
          case Some(row) => row
        })
        .map(tableRow)
        .intersperse("\n")
    }

    val tables = sources.foldLeft[Stream[F, String]](Stream.empty) {
      (acc, source) => acc ++ table(source)
    }

    val footer: Stream[F, String] =
      Stream.emit(tableFooter).intersperse("\n")

    (header ++ tables ++ footer)
      .through(text.utf8Encode)
      .through(io.file.writeAll(
        Paths.get(tablePath)
      ))
      .compile.drain
  }
}
