import arrow.core.Either
import arrow.core.Validated
import arrow.core.handleErrorWith
import arrow.core.left
import arrow.typeclasses.Semigroup

data class Err(
    val id: ErrId,
    val msg: String,
    val throwable: Throwable? = null,
    val tail: Err? = null,
)

enum class ErrId {
    InvalidArgument,
    UserInvalidCredentials,
}

typealias Res<T> = Either<Err, T>

typealias ResV<T> = Validated<Err, T>

object ErrSemigroup : Semigroup<Err> {
    override fun Err.combine(b: Err): Err =
        if (tail == null) copy(tail = b)
        else copy(tail = tail.combine(b))
}

fun <T> (() -> T).safely(err: () -> Err): Res<T> =
    Either.catch { this() }.handleErrorWith { err().copy(throwable = it).left()  }

fun launchMissiles(): Int = throw Exception("BOOM!")

fun <A, B> ((A) -> B).safely(a: A, err: () -> Err): Res<B> =
    { this (a) }.safely(err)

fun <A, B, C>((A, B) -> C).safely(a: A, b: B, err: () -> Err): Res<C> =
    { this (a, b) }.safely(err)

fun div(a: Int, b: Int): Int = a / b