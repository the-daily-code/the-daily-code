package net.thedailycode.ep001

sealed trait Nat
case object Zero extends Nat
case class Add1(s: Nat) extends Nat

object Nat {

  def toLongR(n: Nat): Long = n match {
    case Zero => 0L
    case Add1(nMinus1) => 1L + toLongR(nMinus1)
  }

  // primitive
  def recNat[T](n: Nat, base: T, step: (Nat, T) => T): T = n match {
    case Zero => base
    case Add1(nMinus1) => step(nMinus1, recNat(nMinus1, base, step))
  }

  // derived
  def iterNat[T](n: Nat, base: T, step: T => T): T =
    recNat(n, base, (_: Nat, t: T) => step(t))

  // derived
  def whichNat[T](n: Nat, base: T, step: Nat => T): T =
    recNat(n, base, (n: Nat, _: T) => step(n))

  def toLong(n: Nat): Long =
    iterNat(n, 0L, (l: Long) => l + 1)

}

