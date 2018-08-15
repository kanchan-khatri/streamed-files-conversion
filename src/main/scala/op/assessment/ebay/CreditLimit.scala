package op.assessment.ebay

object CreditLimit {

	final case class Record(
			name: String,
			address: String,
			postcode: String,
			phone: String,
			limit: String,
			birthday: String
		)

	type RowParse = String => Option[Record]

	/**
		* Try to determine a type of a workbook and get corresponding parser.
		*
		* @param path path to workbook.
		* @return parse function for a type determined
		*/
	def workBookParse(path: String): RowParse = path match {
		case csv if csv.endsWith(".csv") => parse[CsvType]
		case prn if prn.endsWith(".prn") => parse[TsvType]
		case _ => _ => None
	}

	private def parse[F <: FormatType : RowFormat]: RowParse = {
		def toRecord(formatted: Option[List[String]]): Option[Record] =
			formatted.map(_.filter(_.nonEmpty)).filter(_.size == 6) map {
				line => {
					val fields = line.toVector
					Record(
						name = fields(0),
						address = fields(1),
						postcode = fields(2),
						phone = fields(3),
						limit = fields(4),
						birthday = fields(5))
				}
			}

		RowFormat[F].format _ andThen toRecord
	}
}
