package net.thedailycode.ep002

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class NatSpec extends AnyFlatSpec with Matchers {

  "The Nat class" should "do the basics" in {
    import Nat._
    val zero = get[Zero.type]
    zero shouldEqual Zero

    val one = get[Add1[Zero.type]]
    one shouldEqual Add1(zero)

    val two = get[Add1[Add1[Zero.type]]]
    two shouldEqual Add1(one)
  }
}
