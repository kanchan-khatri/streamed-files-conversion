package op.assessment.ebay

object CreditLimit {

  import com.github.tototoshi.csv._

  val csvParser = new CSVParser(new DefaultCSVFormat {})
  val tsvParser = new CSVParser(new TSVFormat {})

  case class Record(
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
    case CsvRow => parseCsv(csvParser)
    case PrnRow => parseCsv(tsvParser)
    case IllegalRow => s => None
  }

  private def parseCsv(parser: CSVParser)(row: String): Option[Record] = {
    parser.parseLine(row).map(_.filter(_.nonEmpty)).filter(_.size == 6) map {
      line => {
        val fields = line.toVector
        Record(fields(0), fields(1), fields(2), fields(3), fields(4), fields(5))
      }
    }
  }
}
