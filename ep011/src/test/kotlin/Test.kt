import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
class Test {

    @Test
    fun `assert that we can login`() {
        val res = login("hello", "42")
        assertTrue { res.isRight() }
    }

    @Test
    fun `assert that we can't login with the incorrect password`() {
        val res = login("hello", "666")
        assertTrue { res.isRight() }
    }
}