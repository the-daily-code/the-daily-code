import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right

data class Err(
    val id: ErrId,
    val msg: String,
    val throwable: Throwable? = null,
    val tail: Error? = null
)

enum class ErrId {
    InvalidArgument,
    UserInvalidCredentials,
}

typealias Res<T> = Either<Err, T>

data class User(val id: String)

data class Token(val id: String)

fun fetchUser(userName: String): Res<User> =
    if (userName.isNotBlank() || userName.length > 6) User(userName.trim()).right()
    else Err(ErrId.InvalidArgument, "Invalid username $userName").left()

fun createToken(user: User, password: String): Res<Token> =
    if (password == "42") Token("foo").right()
    else Err(ErrId.UserInvalidCredentials, "Invalid credentials for $user").left()

fun login(userName: String, password: String): Res<Pair<User, Token>> = either.eager {
    val user = fetchUser(userName).bind()
    val token = createToken(user, password).bind()
    Pair(user, token)
}
