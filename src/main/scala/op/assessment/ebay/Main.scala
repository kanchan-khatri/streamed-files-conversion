package op.assessment.ebay

import java.nio.file.Paths
import cats.effect.IO
import scala.language.higherKinds

object Main extends App with TablesConversion with HtmlTable {

	def arguments:  Either[Throwable, Seq[String]] = {
		if (args.length < 2) {
			Left(new IllegalArgumentException(
				"Wrong arguments. Usage: " +
					"<path to source1> [ <path to source2> ...[<path to sourceN>]] " +
					"<path to result>"))
		} else {
			Right(args)
		}
	}

	val program = for {
		filePaths <- IO.fromEither(arguments)
		_ <- converter[IO](filePaths.init, Paths.get(filePaths.last))
	} yield ()

	program.unsafeRunCancelable {
    case Left(th) => th.printStackTrace()
    case _ => ()
  }
}
