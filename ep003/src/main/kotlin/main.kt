import arrow.core.*

fun div(n: Int, d: Int): Either<String, Int> =
    if (d == 0) "Division by zero".left()
    else (n / d).right()

fun main(args: Array<String>) {
    println("Hello World!")
}