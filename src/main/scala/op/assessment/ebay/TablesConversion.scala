package op.assessment.ebay

import java.nio.file.{Path, Paths}
import cats.effect.Sync
import fs2.{Stream, io, text}
import op.assessment.ebay.CreditLimit._
import scala.language.higherKinds

trait TablesConversion  extends Table {

  def converter[F[_]](
			sources :Seq[String],
			out: Path)(implicit F: Sync[F]): F[Unit] = {

    val header: Stream[F, String] =
      Stream.emit(tableHeader).intersperse("\n")

    def table(source: String): Stream[F, String] = {
      val parse = workBookParse(source)
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
      .through(io.file.writeAll(out))
      .compile.drain
  }
}
