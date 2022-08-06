import arrow.core.Either
import arrow.typeclasses.Semigroup

data class Err(
    val id: ErrId,
    val msg: String,
    val throwable: Throwable? = null,
    val tail: Err? = null
)

enum class ErrId {
    InvalidArgument,
    UserInvalidCredentials,
}

typealias Res<T> = Either<Err, T>

object ErrorSemigroup : Semigroup<Err> {
    override fun Err.combine(b: Err): Err =
        if (tail == null) copy(tail = b)
        else copy(tail = tail.combine(b))
}