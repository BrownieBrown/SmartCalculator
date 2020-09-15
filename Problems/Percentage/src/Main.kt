import java.math.BigInteger

fun main() {
    val a = BigInteger(readLine()!!)
    val b = BigInteger(readLine()!!)
    val hundred = BigInteger("100")
    val one = BigInteger("1")

    println("${a * hundred / (b + a)}% ${b * hundred / (b + a)}%")
}

