import arrow.core.Either
import arrow.core.Validated
import arrow.core.invalid
import arrow.core.valid
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

fun getUsername(username: String): ResV<String> =
    if (username.isNotBlank()) username.trim().valid()
    else Err(ErrId.InvalidArgument, "Invalid username $username").invalid()
fun getPassword(password: String): ResV<String> =
    if (password.isNotBlank() && password.length > 6) password.valid()
    else Err(ErrId.InvalidArgument, "Invalid password $password").invalid()
fun getAge(age: Int): ResV<Int> =
    if (age in 18..100) age.valid()
    else Err(ErrId.InvalidArgument, "Invalid age $age").invalid()

data class User(
    val username: String,
    val password: String,
    val age: Int,
)

