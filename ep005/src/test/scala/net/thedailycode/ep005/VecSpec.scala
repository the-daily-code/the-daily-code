package net.thedailycode.ep005

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class VecSpec extends AnyFlatSpec with Matchers {

  "The Vec collection" should "do the basics" in {
    import Vec._
    val vec0 = rep("Foo", Zero)
    vec0 shouldEqual VNil

    val vec1 = rep(42, Add1(Zero))
    vec1 shouldEqual VCons(42, VNil)

    val vec2 = rep(true, Add1(Add1(Zero)))
    vec2 shouldEqual VCons(true, VCons(true, VNil))

    // compile time error
    //val vec2a: Vec[Boolean, Add1[Zero.type]] = vec2
  }
}
