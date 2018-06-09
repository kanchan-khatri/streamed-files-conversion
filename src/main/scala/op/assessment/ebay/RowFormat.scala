package op.assessment.ebay

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
