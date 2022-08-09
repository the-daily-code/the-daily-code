import arrow.core.zip
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Test {
    @Test
    fun `assert missiles are under control`() {
        val res = ::launchMissiles.safely {
            Err(ErrId.InvalidArgument, "Missiles got out of hand!")
        }

        assertTrue { res.isLeft() }

        println(res)
    }

    @Test
    fun `assert we can't divide by zero`() {
        val res = ::div.safely(42, 0) {
            Err(ErrId.InvalidArgument, "Failed to divide 42 by 0")
        }

        assertTrue { res.isLeft() }

        println(res)
    }
}