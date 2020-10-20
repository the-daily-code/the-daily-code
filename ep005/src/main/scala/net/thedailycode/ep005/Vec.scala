package net.thedailycode.ep005

sealed trait Nat
case object Zero extends Nat
case class Add1[N <: Nat](n: N) extends Nat

sealed trait Vec[+T, N <: Nat]
case class VCons[+T, N <: Nat](h: T, t: Vec[T, N])
  extends Vec[T, Add1[N]]
case object VNil extends Vec[Nothing, Zero.type]

object Vec {

  def rep[T, N <: Nat](e: T, n: N)(implicit f: T => Vec[T, N]): Vec[T, N] = f(e)

  implicit def fZero[T]: T => Vec[Nothing, Zero.type] = (_: T) => VNil

  implicit def fAdd1[T, N <: Nat](implicit f: T => Vec[T, N]):
    T => Vec[T, Add1[N]] = (t: T) => VCons(t, f(t))
}