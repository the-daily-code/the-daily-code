package net.thedailycode.ep001

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class NatSpec extends AnyFlatSpec with Matchers {

  "The Nat class" should "do the basics" in {
    import Nat._

    val zero = Zero
    val one = Add1(zero)
    val two = Add1(one)
    val three = Add1(two)

    toLongR(zero) shouldEqual 0L
    toLongR(one) shouldEqual 1L
    toLongR(two) shouldEqual 2L
    toLongR(three) shouldEqual 3L

    toLong(zero) shouldEqual 10L
    toLong(one) shouldEqual 1L
    toLong(two) shouldEqual 2L
    toLong(three) shouldEqual 3L
  }
}
