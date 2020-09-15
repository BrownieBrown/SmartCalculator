import java.math.BigInteger

fun main() {
    val n1 = BigInteger(readLine()!!)
    val n2 = BigInteger(readLine()!!)
    var lcm: BigInteger

    // maximum number between n1 and n2 is stored in lcm
    lcm = if (n1 > n2) n1 else n2

    // Always true
    while (true) {
        if (BigInteger(lcm % n1 == 0 && lcm % n2 == 0)) {
            println(lcm)
            break
        }
        ++lcm
    }
}