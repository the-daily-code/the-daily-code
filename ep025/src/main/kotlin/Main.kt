import java.math.BigInteger

data class FieldElement(val n: BigInteger, val p: BigInteger) {

    init {
        assert(n >= BigInteger.ZERO && n < p) { "Invalid argument n must be >= 0 and n < p" }
    }

    operator fun plus(o: FieldElement): FieldElement {
        assert(o.p == p) { "Invalid argument p values don't match" }
        return FieldElement((n + o.n).mod(p), p)
    }

    operator fun minus(o: FieldElement): FieldElement {
        assert(o.p == p) { "Invalid argument p values don't match" }
        return FieldElement((n - o.n).mod(p), p)
    }

    operator fun times(o: FieldElement): FieldElement {
        assert(o.p == p) { "Invalid argument p values don't match " }
        return FieldElement((n * o.n).mod(p), p)
    }

    operator fun times(i: BigInteger): FieldElement =
        this * FieldElement(i, this.p)

    infix fun pow(e: BigInteger): FieldElement =
        FieldElement(n.modPow(e, p), p)

    /**
     * Fermat's Little Theorem
     * n^(p - 1) = 1
     *
     * a / b = a *f b^(-1)
     *
     * b^(-1) * 1 = b^(-1) * b^(p - 1) = b^(p - 2)
     *
     * a / b = a *f b^(p - 2)
     */
    operator fun div(o: FieldElement): FieldElement {
        assert(o.p == p) { "Invalid argument p values don't match " }
        val b = FieldElement(
            o.n.modPow((p - BigInteger.TWO), p),
            p
        )
        return this.times(b)
    }
}

operator fun BigInteger.times(o: FieldElement): FieldElement =
    FieldElement(this, o.p) * o


fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}