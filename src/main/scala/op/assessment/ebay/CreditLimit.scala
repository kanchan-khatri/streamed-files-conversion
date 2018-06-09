package op.assessment.ebay

object CreditLimit {

  import com.github.tototoshi.csv._

	trait FormatType
	trait CsvType extends FormatType
	trait TsvType extends FormatType

  trait RowFormat[F <: FormatType] {
		def parser: CSVParser
		def format(row: String): Option[List[String]] = parser.parseLine(row)
	}

	object RowFormat {

		def apply[F <: FormatType : RowFormat]: RowFormat[F] = {
			implicitly[RowFormat[F]]
		}

		implicit object csvFormat extends RowFormat[CsvType] {
			override val parser: CSVParser = new CSVParser(new DefaultCSVFormat {})
		}

		implicit object tsvFormat extends RowFormat[TsvType] {
			override val parser: CSVParser = new CSVParser(new TSVFormat {})
		}
	}

  final case class Record(
      name: String,
      address: String,
      postcode: String,
      phone: String,
      limit: String,
      birthday: String
    )

  object RowType {

    def apply(path: String): RowType = path match {
      case csv if csv.endsWith(".csv") => CsvRow
      case prn if prn.endsWith(".prn") => PrnRow
      case _ => IllegalRow
    }
  }

  sealed trait RowType
  case object CsvRow extends RowType
  case object PrnRow extends RowType
  case object IllegalRow extends RowType

  type RowParse = String => Option[Record]

  def rowParse(rt: RowType): RowParse = rt match {
    case CsvRow => parse[CsvType]
    case PrnRow => parse[TsvType]
    case IllegalRow => _ => None
  }

	private def parse[F <: FormatType: RowFormat]: RowParse = {
		def toRecord(formatted: Option[List[String]]): Option[Record] =
			formatted.map(_.filter(_.nonEmpty)).filter(_.size == 6) map {
				line => {
					val fields = line.toVector
					Record(
						name			= fields(0),
						address		= fields(1),
						postcode	= fields(2),
						phone			= fields(3),
						limit			= fields(4),
						birthday 	= fields(5))
				}
			}

		RowFormat[F].format _ andThen toRecord
	}
}
