import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FieldElementTest {

    @Test
    fun `test the sum of field elements`() {
        val p = 7.toBigInteger()
        val e1 = FieldElement(1.toBigInteger(), p)
        val e2 = FieldElement(2.toBigInteger(), p)
        val e = e1 + e2
        assertEquals(FieldElement(3.toBigInteger(), p), e)
    }

    @Test
    fun `test the sum of field elements updated`() {
        val p = 7.toBigInteger()
        val e1 = FieldElement(1.toBigInteger(), p)
        val e2 = FieldElement(6.toBigInteger(), p)
        val e = e1 + e2
        assertEquals(e, FieldElement(0.toBigInteger(), p))
    }
}