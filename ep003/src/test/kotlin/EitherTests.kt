import arrow.core.Either
import arrow.core.Either.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.fail

class EitherTests {

    val five: Either<String, Int> = div(10, 2)

    val error: Either<String, Int> = div(10, 0)

    @Test
    fun helloEither() {
        assertTrue { five.isRight() }
        assertFalse { error.isRight() }
    }

    @Test
    fun patternMatching() {
        when (five) {
            is Left ->
                fail("Expected five but got error ${five.a}")
            is Right ->
                assertEquals(5, five.b)
        }
    }

    @Test
    fun foldAllTheThings() {
        five.fold(
            { fail("Expected five but got error $it") },
            { assertEquals(5, it) }
        )
    }
}