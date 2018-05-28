package op.assessment.ebay

import org.scalatest.FunSuite

class CreditLimitTest extends FunSuite {

  import CreditLimit._

  test("CsvRow") {
    assert(
      rowParse(RowType("input.csv")) {
        """"Gibson, Mal",Vredenburg 21,3209 DD,06-48958986,54.5,09/11/1978"""
      } === Some(
        Record(
          name = "Gibson, Mal",
          address = "Vredenburg 21",
          postcode = "3209 DD",
          phone = "06-48958986",
          limit = "54.5",
          birthday = "09/11/1978")))

    assert(
      rowParse(RowType("input.ccc")) {
        """"Gibson, Mal",Vredenburg 21,3209 DD,06-48958986,54.5,09/11/1978"""
      } === None)

    assert(
      rowParse(RowType("input.csv")) {
        """"Gibson, Mal",3209 DD,06-48958986,54.5,09/11/1978"""
      } === None)

    assert(
      rowParse(RowType("input.csv")) {
        """"***sadfvdfv!!!"""
      } === None)
  }

  test("RrnRow") {
    assert(
      rowParse(RowType("input.prn")) {
        """Johnson, John	Voorstraat 32		3122gg		020 3849381		1000000	19870101"""
      } === Some(
        Record(
          name = "Johnson, John",
          address = "Voorstraat 32",
          postcode = "3122gg",
          phone = "020 3849381",
          limit = "1000000",
          birthday = "19870101")))


    assert(
      rowParse(RowType("input.prn")) {
				"""Johnson, John	Voorstraat 32		020 3849381		1000000	19870101"""
      } === None)

    assert(
      rowParse(RowType("input.prn")) {
        """"***sadfvdfv!!!"""
      } === None)
  }
}
