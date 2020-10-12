import arrow.core.Either
import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right

val e1: Either<String, Int> = 42.right()

val e2: Either<String, Boolean> = e1.map { it % 2 == 0 }

fun divideByTwo(i: Int): Int = i / 2
fun isEven(i: Int): Boolean = i % 2 == 0
fun evenOrOdd(b: Boolean): String =
    if (b) "Even" else "Odd"

val e3 = e1
    .map(::divideByTwo)
    .map(::isEven)
    .map(::evenOrOdd)

val error: Either<String, Int> = "Error found".left()
val noop = error.map { i -> i / 2 }

fun doubleIfEven(i: Int): Either<String, Int> =
    if (i % 2 == 0) (i * 2).right()
    else "$i is odd".left()

val e4: Either<String, Int> = e1.flatMap(::doubleIfEven)

val error41 = 41.right().flatMap(::doubleIfEven)

val e5: Either<String, Int> = e1.flatMap { i1 ->
    doubleIfEven(i1).flatMap { i2 ->
        doubleIfEven(i2).map { i3 ->
                i1 + i2 + i3
        }
    }
}

val e52: Either<String, Int> = either.eager {
    val i1 = e1.bind()
    val i2 = doubleIfEven(i1).bind()
    val i3 = doubleIfEven(i2).bind()
    i1 + i2 + i3
}