import org.junit.jupiter.api.Test

internal class FractionTest {

    @Test
    fun consBasic1() {
        val a = Fraction(1,2)
        assert(a.toString() == "1/2")
    }

    @Test
    fun consBasic2() {
        val a = Fraction(6,12)
        assert(a.toString() == "1/2")
    }

    @Test
    fun consNeg() {
        val a = Fraction(1,2, -1)
        assert(a.toString() == "-1/2")
    }

    @Test
    fun consReduce() {
        val a = Fraction(4,20)
        assert(a.toString() == "1/5")
    }

    @Test
    fun consReduceNeg() {
        val a = Fraction(4,20, -1)
        assert(a.toString() == "-1/5")
    }

    @Test
    fun compareToEq() {
        assert(Fraction(1,2).compareTo(Fraction(2,4)) == 0)
    }

    @Test
    fun compareToGt() {
        assert(Fraction(1,2).compareTo(Fraction(2,5)) == 1)
    }

    @Test
    fun compareToLt() {
        assert(Fraction(1,3).compareTo(Fraction(2,5)) == -1)
    }

    @Test
    fun negate1() {
        assert(Fraction(1,2).negate().toString() == "-1/2")
    }

    @Test
    fun negate2() {
        assert(Fraction(1,2, -1).negate().toString() == "1/2")
    }

    @Test
    fun add() {
        assert(Fraction(11,12).add(Fraction(1,6)) == Fraction(13,12))
    }

    @Test
    fun plus() {
        assert(Fraction(2,3) + Fraction(1,3) == Fraction(1,1))
    }

    @Test
    fun subtr() {
        assert(Fraction(1,2).subtr(Fraction(1,3)) == Fraction(1,6))
    }

    @Test
    fun minus() {
        assert(Fraction(1,2) - Fraction(1,3) == Fraction(1,6))
    }

    @Test
    fun mult() {
        assert(Fraction(1,2).mult(Fraction(1,3)) == Fraction(1,6))
    }

    @Test
    fun multNegative() {
        assert(Fraction(1,2,-1) * Fraction(2,3) == Fraction(1,3,-1))
    }

    @Test
    fun div() {
        assert(Fraction(1,2).div(Fraction(1,3)) == Fraction(3,2))
    }

    @Test
    fun addNegative() {
        assert(Fraction(1,2,-1) + Fraction(1,3) == Fraction(1,6,-1))
    }

    @Test
    fun subtractToNegative() {
        assert(Fraction(1,3) - Fraction(1,2) == Fraction(1,6,-1))
    }

    @Test
    fun multTwoNegatives() {
        assert(Fraction(1,2,-1) * Fraction(2,3,-1) == Fraction(1,3))
    }




}