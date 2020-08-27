package net.thedailycode.ep002

sealed trait Nat
case object Zero extends Nat
case class Add1[N <: Nat](n: N) extends Nat

object Nat {

  def get[N <: Nat](implicit n: N): N = n

  implicit val getZero: Zero.type = Zero

  implicit def getAdd1[N <: Nat](implicit n: N): Add1[N] = Add1(n)

}