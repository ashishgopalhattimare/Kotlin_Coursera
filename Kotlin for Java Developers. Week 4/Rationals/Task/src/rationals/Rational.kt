package rationals

import java.math.BigInteger

data class RationalRange(var r1 : Rational, var r2 : Rational) {
    operator fun contains(b: Rational): Boolean {

        val a : Rational = this.r1 ; val c : Rational = this.r2
        return ((a == b || a < b) && (b == c || b < c))
    }
}

data class Rational(var num : BigInteger, var den : BigInteger) {

    init {
        val eq = (num.signum() == den.signum())
        num = num.abs(); den = den.abs()

        val  gcd = num.gcd(den)
        this.num = num.div(gcd)
        this.den = den.div(gcd)

        if(!eq) this.num = -this.num
    }

    override fun toString(): String {

        if(den == 1.toBigInteger()) return "$num"
        if(num == 0.toBigInteger()) return "0"

        return "$num/$den"
    }

    operator fun compareTo(other: Rational): Int {

        val X : BigInteger = this.num.times(other.den) - other.num.times(this.den)
        if(X < 0.toBigInteger()) return -1
        else if(X > 0.toBigInteger()) return 1
        return 0
    }

    operator fun rangeTo(other: Rational): RationalRange {
        return RationalRange(this, other)
    }
}

operator fun Rational.plus(o:Rational) : Rational =
        Rational((this.num.times(o.den)).add((o.num.times(this.den))), (this.den.times(o.den)))

operator fun Rational.minus(o:Rational) : Rational =
        Rational((this.num.times(o.den)).minus((o.num.times(this.den))), (this.den.times(o.den)))

operator fun Rational.times(o:Rational) : Rational =
        Rational(this.num * o.num, this.den * o.den)

operator fun Rational.div(o:Rational) : Rational =
        Rational(this.num * o.den, this.den * o.num)

operator fun Rational.unaryMinus() = Rational(-this.num, this.den)

infix fun BigInteger.divBy(other: BigInteger): Any {
    return Rational(this, other)
}

infix fun Long.divBy(other: Long): Rational {
    return Rational(this.toBigInteger(), other.toBigInteger())
}

infix fun Int.divBy(other:Int) : Rational =
    Rational(this.toBigInteger(), other.toBigInteger())

fun String.toRational(): Rational {
    val x = this.split("/")
    var other : BigInteger

    other = if(x.size == 1) 1.toBigInteger()
    else x[1].toBigInteger()

    return Rational(x[0].toBigInteger(), other)
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    var a = "1/2".toRational()
    var b = "20395802948019459839003802001190283020/32493205934869548609023910932454365628".toRational()
    var c = "2/3".toRational()

    println(b in a..c)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}
