package op.assessment.ebay

import op.assessment.ebay.CreditLimit.Record

trait Table {

  def tableHeader: String
  def tableFooter: String
  def tableRow(record: Record): String
}

trait HtmlTable extends Table {

  def tableRow(record: Record): String =
    s"""
      | <tr>
      |   <td>${record.name}</td>
      |   <td>${record.address}</td>
      |   <td>${record.postcode}</td>
      |   <td>${record.phone}</td>
      |   <td>${record.limit}</td>
      |   <td>${record.birthday}</td>
      | </tr>
    """.stripMargin

  val tableHeader: String =
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

  val tableFooter: String =
    """
      |
      |</table>
      |
      |</body>
      |</html>
    """.stripMargin
}
