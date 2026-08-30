class Fraction(
    numerator: Int,
    denominator: Int,
    private val sign: Int = 1
) : Comparable<Fraction> {

    // Calculate the greates common divisor so that
    // the fraction can be reduced to its simplest from.
    private val gcd = gcd(numerator, denominator)

    // Store the reduced numerator and denominator
    // These properties are private and val, so the Fraction is immutable
    private val numerator: Int = numerator / gcd
    private val denominator: Int = denominator / gcd

    // Algorithm to calculate the greatest common divisor
    private fun gcd(a: Int, b: Int) : Int {
        // Base case
        if (b == 0) {
            return a
        }
        // Recursive case
        return gcd(b, a % b)
    }

    // Compare two fractions
    // Returns 0 if the fractions are equal,
    // >0 if the first fraction is greater,
    // <0 if the second fraction is greater
    override fun compareTo(other: Fraction) : Int {
        // Compare the fractions by cross multiplying
        // sign is included because a Fraction can be negative
        val left = sign * numerator * other.denominator
        val right = other.sign * other.numerator * denominator

        return left.compareTo(right)
    }
    // Defines structural equality
    // This allows == to compare the values instead of object references
    override fun equals(other: Any?): Boolean {

        // If both variables refer to exactly the same object,
        // they are equal
        if (this === other) return true
        // If the other object is not a Fraction, they are not equal
        if (other !is Fraction) return false

        // Compare the values of the numerator, denominator, and sign
        return numerator == other.numerator &&
                denominator == other.denominator &&
                sign == other.sign
    }

    // When equals is overridden, hashCode must also be overridden
    // Equal fraction objects should have the same hash code
    override fun hashCode(): Int{
        var result = numerator
        result = 31 * result + denominator
        result = 31 * result + sign
        return result
    }

    // Returns a string representation of the fraction
    override fun toString(): String {
        return "${sign * numerator}/${denominator}"
    }

    // Returns a new Fraction with the same value but opposite sign
    fun negate(): Fraction {
        return Fraction(numerator, denominator, -sign)
    }

    // Adds two fractions together
    fun add(other: Fraction): Fraction {
        val newNumerator =
            sign * numerator * other.denominator + other.sign * other.numerator * denominator

        val newDenominator =
        denominator * other.denominator

    val newSign = if (newNumerator < 0) -1 else 1

        return Fraction(
            kotlin.math.abs(newNumerator),
            newDenominator,
            newSign
        )
    }

    // Operator overloading
    operator fun plus(other: Fraction): Fraction {
        return add(other)
    }

    // Subtracts two fractions
    fun subtr(other: Fraction): Fraction {
        return add(other.negate())
    }

    operator fun minus(other: Fraction): Fraction {
        return subtr(other)
    }

    // Multiplies two fractions
    fun mult(other: Fraction): Fraction {
        return Fraction(
            numerator * other.numerator,
            denominator * other.denominator,
            sign * other.sign
        )
    }

    // Operator overloading
    operator fun times(other: Fraction): Fraction {
        return mult(other)
    }

    // Divides two fractions
    fun div(other: Fraction): Fraction {
        require(other.numerator != 0) {
            "Cannot divide by zero"
        }

        return Fraction(
            numerator * other.denominator,
            denominator * other.numerator,
            sign * other.sign
        )
    }

    // Operator overloading
    operator fun unaryMinus(): Fraction {
        return negate()
    }


}

fun main() {
    val a = Fraction(1,2,-1)

    println(a)
    println(a.add(Fraction(1,3)))
    println(a.mult(Fraction(5,2,-1)))
    println(a.div(Fraction(2,1)))
    println(-Fraction(1,6) + Fraction(1,2))
    println(Fraction(2,3) * Fraction(3,2))
    println(Fraction(1,2) > Fraction(2,3))
}

