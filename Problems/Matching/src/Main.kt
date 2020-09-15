import java.math.BigInteger

fun main() {
    val a = BigInteger(readLine()!!.replace("(\\+)".toRegex(), ""))
    val b = BigInteger(readLine()!!.replace("(\\+)".toRegex(), ""))
    val c = BigInteger(readLine()!!.replace("(\\+)".toRegex(), ""))

    if (a == b && b == c) println("3")
    else if (a != b && b == c ) println("2")
    else if (a == b && b != c) println("2")
    else if (a == c && b != c) println("2")
    else println("0")
}