package op.assessment.ebay

object CreditLimit {

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
    case CsvRow => s => Some(Record("John Doe", "NY", "1111", " 06-28938945", "100", "1983/03/21"))
    case PrnRow => s => Some(Record("John Doe", "NY", "1111", " 06-28938945", "100", "1983/03/21"))
    case IllegalRow => s => None
  }
}
