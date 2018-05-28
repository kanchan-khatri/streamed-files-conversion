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

  sealed trait RowType
  case object CsvRow extends RowType
  case object PrnRow extends RowType

  type RowParse = String => Record

  def rowParse(rt: RowType): RowParse = {
    s => Record("John Doe", "NY", "1111", " 06-28938945", "100", "1983/03/21")
  }

  def htmlTableRow(record: Record): String =
    """
      | <tr>
      |   <td>John Doe111</td>
      |   <td>NY</td>
      |   <td>1111</td>
      |   <td>06-28938945</td>
      |   <td>$100</td>
      |   <td>ok</td>
      | </tr>
    """.stripMargin

  val htmlHeader: String =
    """
      |<html>
      |<body>
      |<table style="width:100%">
      |  <tr>
      |    <th>Name</th>
      |    <th>Address</th>
      |    <th>Postcode</th>
      |    <th>Phone</th>
      |    <th>Credit Limit</th>
      |    <th>Birthday</th>
      |  </tr>
      |
   """.stripMargin

  val htmlFooter: String =
    """
      |
      |</table>
      |
      |</body>
      |</html>
    """.stripMargin
}
