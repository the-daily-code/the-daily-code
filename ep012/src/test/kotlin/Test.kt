import arrow.core.zip
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class Test {
    @Test
    fun `assert we can aggregate errors`() {
        val respv1 = getUsername("    ")
        val respv2 = getPassword("1234")
        val respv3 = getAge(250)

        val res = respv1.zip(
            ErrSemigroup,
            respv2,
            respv3,
            ::User
        )

        assertTrue { res.isInvalid }

        println(res)
    }
}